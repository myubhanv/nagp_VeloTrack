package com.nagp.ms.orderService.controller;

import com.nagp.ms.orderService.model.ProductEntry;
import com.nagp.ms.orderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("shop")
public class OrderController {
    private static final String EMPTY_CART ="Cart is empty! Add products to place order.";
    @Autowired
    OrderService orderService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestParam String username,
                            @RequestParam String productId,@RequestParam int quantity) {
        return new ResponseEntity<>(orderService.addToCart(username, productId, quantity), HttpStatus.OK);
    }
    @GetMapping("/viewCart")
    public ResponseEntity<List<ProductEntry>> viewCart(@RequestParam String username) {
        List<ProductEntry> cart = orderService.getCart(username);
        if (cart.isEmpty()){
            System.out.println(EMPTY_CART);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestParam String username) {
        return new ResponseEntity<>(orderService.placeOrder(username), HttpStatus.OK);
    }

}