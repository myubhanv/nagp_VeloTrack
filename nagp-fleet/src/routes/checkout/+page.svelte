<script>
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import { authStore } from '$lib/stores/auth';
  import { cartStore } from '$lib/stores/cartStore';
  import { get } from 'svelte/store';
import { API_BASE_URL } from '$lib/config';
  let errorFlag = false;
  let total = 0;
  let userloggedIn = $authStore.isAuthenticated;
  let user = ''; 

  onMount(async () => {
    const cartItems = get(cartStore);

    try {
      if(userloggedIn) {
        user = $authStore.username;
        for(const item of cartItems) {
        total += item.price * item.quantity;

        const params = new URLSearchParams({
            username: user,
            productId: item.id,
            quantity: item.quantity
        });
        const response = await fetch(`${API_BASE_URL}/shop/addToCart?${params.toString()}`, {
            method: 'POST'
        });
        const message = await response.text();

        if (response.ok && message.includes('Successfully')) {
            console.log(`${item.name}: ${message}`);
        } else {
            errorFlag = true;
            console.warn(`${item.name}: ${message}`);
            console.error('Failed to save cart.');
        }
    }
    if(errorFlag) {
      alert('Error saving cart. Please try again later.');
    } else {
      console.log('Cart saved successfully!');
      cartStore.set([]); // Clear the cart after saving to BE
    }
      }
  } catch (error) {
    console.error('Error:', error);
    alert('Error connecting to backend.');
  }
});
</script>
<h2>Checkout Page</h2>
{#if !userloggedIn}
  <p>Please log in to proceed with checkout.</p>
  <button on:click={() => goto('/login')}>Login</button>
{:else}
 
    <p>Thank you for shopping with us, {user}!</p>
    <p>Confirm your order details below:</p>

    {#if errorFlag}
    <p style="color: red;">There was an error processing your order. Please try again later.</p>
    {:else}
        <table>
            <thead>
            <tr>
                <th>Product</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Total</th>
            </tr>
            </thead>
            <tbody>
            {#each get(cartStore) as item}
                <tr>
                <td>{item.name}</td>
                <td>${item.price}</td>
                <td>{item.quantity}</td>
                <td>${item.price * item.quantity}</td>
                </tr>
            {/each}
            </tbody>
        </table>
        <p>Your total is: ${total}</p>  
        <p> Delivery will be made to your registered address.</p>
        <p> payment mode : mock payment</p> 
        <button on:click={goto('/order')}>Confirm Order</button>
    {/if}
{/if}


