package com.nagp.ms.orderService.service;

import com.nagp.ms.orderService.model.Order;
import com.nagp.ms.orderService.model.Order.OrderStatus;
import com.nagp.ms.orderService.model.Order.PaymentStatus;
import com.nagp.ms.orderService.model.ProductEntry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {
    private static final String EMPTY_CART ="Cart is empty! Add products to place order.";
    private static final String INSUFFICIENT_STOCK ="Due to insufficient stock, ";
    private static final String PAYMENT_fAILED ="Due to payment failure, ";
    private static final String ORDER_CANCELLED =" your order has been cancelled";
    private static final String ORDER_PLACED ="Your order has been successfully placed, your order id is ";
    private static final String STOCK_ERROR ="Product Out of Stock";
    private static final String ADDED_TO_CART ="Successfully added to cart.";
    private static final String PRODUCT_URL ="http://product-service/product/getStock/";
    private final Map<String, List<ProductEntry>> userCarts = new HashMap<>();
    private final List<Order> orders = new ArrayList<>();
    @Value("${server.port}")
    private int port;

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    MiscellaneousService miscellaneousService;

    @Autowired
    private RestTemplate restTemplate;

    public String addToCart(String username, String productId, int quantity) {
        System.out.println("Working from port " + port +" of order Service");
        if (quantity <= 0) throw new RuntimeException("Invalid product or quantity");
        if (getStockByProductId(productId) == 0) {
            return STOCK_ERROR;
        }
        else {
            userCarts.computeIfAbsent(username, k -> new ArrayList<>())
                    .add(new ProductEntry(productId, quantity));
            return ADDED_TO_CART;
        }
    }

    public List<ProductEntry> getCart(String username) {
        return userCarts.getOrDefault(username, new ArrayList<>());
    }

    public String placeOrder(String username) {
        System.out.println("Working from port " + port +" of order Service");
        List<ProductEntry> cart = getCart(username);
        if (cart.isEmpty()) {
            return EMPTY_CART;
        }

        double total = 0;
        for (ProductEntry item : cart) {

            if (getStockByProductId(item.getProductId()) < item.getQuantity()) {
                String stockErrorMsg ="Hi " + username +", " + INSUFFICIENT_STOCK + " for product: " + item.getProductId()+ ORDER_CANCELLED;
                miscellaneousService.sendNotification(stockErrorMsg);
                System.out.println(stockErrorMsg);
                createOrder(username, cart, 0.0, OrderStatus.CANCELLED, PaymentStatus.NOT_PAID);
                return stockErrorMsg;
            }
        }

        if (!miscellaneousService.mockPayment()) {
            String paymentErrorMsg ="Hi " + username +", " + PAYMENT_fAILED + ORDER_CANCELLED;
            miscellaneousService.sendNotification(paymentErrorMsg);
            System.out.println(paymentErrorMsg);
            createOrder(username, cart, 0.0, OrderStatus.CANCELLED, PaymentStatus.FAILED);
            return paymentErrorMsg;
        }

        // cart should get empty after order placement
        userCarts.remove(username);

        String orderId =  createOrder(username, cart, total, OrderStatus.CONFIRMED,PaymentStatus.PAID );
        String orderPlacedMsg = "Hi " + username +", " + ORDER_PLACED + orderId;
        System.out.println(orderPlacedMsg);
        miscellaneousService.sendNotification(orderPlacedMsg);
        return  orderPlacedMsg;
    }

    private String createOrder(String username, List<ProductEntry> cart, double total, OrderStatus orderStatus , PaymentStatus paymentStatus) {
        Order order = new Order("NAGP" + ThreadLocalRandom.current().nextInt(100, 999), username, new ArrayList<>(cart), total, orderStatus, paymentStatus);
        orders.add(order);
        return order.getId();
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "getStockWithFallback")
    public Integer getStockByProductId(String productId){
        //String productUrl = loadBalancerClient.choose("productService").getUri().toString() + "/getStock/" + productId;
        String productUrl =  PRODUCT_URL + productId;
        System.out.println("getting stock from product-service with url:"+productUrl);

        ResponseEntity<Integer> response = restTemplate.exchange(productUrl, HttpMethod.GET, null, Integer.class);
        return response.getBody(); // assuming 2xx status, else exception triggers fallback

  /*  try {
        response = restTemplate.exchange(productUrl, HttpMethod.GET, null, Integer.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Integer stock = response.getBody();
            System.out.println("Fetched product stock: " + stock);
            return stock;
        } else {
            System.out.println("Unexpected response: " + response.getStatusCode());
            throw new RuntimeException("Non-200 response from product service");
        }
    } catch (Exception ex){
        System.out.println("Exception occurred" + ex.getMessage());
        throw new RuntimeException("Unable to connect from product service");
    }
*/
    }
    public Integer getStockWithFallback(String productId, Throwable t){
        System.out.println("Fallback triggered, reason: " + t.getMessage());
        return 0;
    }



}
