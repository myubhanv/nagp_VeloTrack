<script>
import {page} from '$app/stores';
import { goto } from '$app/navigation';
import { productStore } from '$lib/stores/productStore';
import { cartStore } from '$lib/stores/cartStore';

let productId;
let productDetails;
let quantity = 1;

function addToCart() {
    const item = {
        id: productDetails.id,
        name: productDetails.name,
        price: productDetails.price,
        stock: productDetails.stock,
        quantity
    };
    cartStore.update(cart => {
        const existingItem = cart.find(i => i.id === item.id);
        if (existingItem) {
            if(existingItem.quantity + quantity > productDetails.stock) {
                alert(`Cannot add more than ${productDetails.stock} items to the cart.`);
            }
            existingItem.quantity = Math.min(existingItem.quantity + quantity, productDetails.stock);
        } else {
            cart.push(item);
        }
        return cart;
    });
    goto('/cart'); // Redirect to cart after adding
    alert(`${item.name} has been added to your cart.`);
}

$: productId = $page.params.productId;
console.log('Current page:', $page);

 $: productDetails = $productStore.find(p => String(p.id) === String(productId));
</script>


<h1> This is product details Page </h1>
<p> Here you can find the details of the product you selected. </p>
<p>Product ID: {productId}</p>

{#if productDetails}
  <h2>{productDetails.name}</h2>
  <p>{productDetails.description}</p>
  <p>Price: ${productDetails.price}</p>
  <p>Category: {productDetails.category}</p>
  {#if productDetails.stock > 0}
    <p>Only {productDetails.stock} left in stock</p>
    <label for="quantity">Quantity:</label>
    <select id="quantity" bind:value={quantity}>
        {#each Array(productDetails.stock) as _, i}
            <option value={i + 1}>{i + 1}</option>
        {/each}
    </select>

  <button on:click={addToCart}>Add to Cart</button>
  {:else}
    <p>Out of Stock</p>
  {/if}
  
{:else}
  <p>Loading or Product not found</p>
{/if}

<a href="/product">Back to Product list page!</a>

