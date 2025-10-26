<script>
  import { onMount } from 'svelte';
  import { goto } from '$app/navigation';
  import { authStore } from '$lib/stores/auth';
  import { cartStore } from '$lib/stores/cartStore';
  import { get } from 'svelte/store';
import { API_BASE_URL } from '$lib/config';

  let orderMsg = '';
  let userloggedIn = $authStore.isAuthenticated;
  let user = ''; 


  onMount(async () => {
    const cartItems = get(cartStore);

    try {
        if(userloggedIn) {
            user = $authStore.username;
            const params = new URLSearchParams({
                username: user
            });
            const response = await fetch(`${API_BASE_URL}/shop/placeOrder?${params.toString()}`, {
                method: 'POST'
            });
            const message = await response.text();
            console.log('Response:', message);

            if (response.ok && message.includes('successfully')) {
                orderMsg = message;
                cartStore.set([]);
            } else if (response.ok && message.includes('insufficient stock')) {
                console.warn(`{message}`);
                cartStore.set([]);
                alert('Opps! Insufficient stock for one or more items in your cart. Please adjust your quantities and try again.');
                goto('/product'); 
            } else if (response.ok && message.includes('Cart is empty')) {
                alert(`${message} - Please add items to your cart before placing an order.`);
                cartStore.set([]); 
                goto('/product'); 
            } else if (response.ok && message.includes('payment failure')) {
                alert(`${message} - trying again!.`);
                goto('/product'); 
            } else {
                console.warn(`${item.name}: ${message}`);
                console.error('Failed to place order.');
                alert('Unexpected error occurred. Please try again later.');
                goto('/product'); 
            }
        }
    } catch (error) {
    console.error('Error:', error);
    alert('Error connecting to backend.');
    }
});
</script>
<h2>Order Page</h2>
{#if !userloggedIn}
  <p>Please log in to place Order.</p>
  <button on:click={() => goto('/login')}>Login</button>
{:else}
    {#if orderMsg == ''}
      <p>Hi, {user}!</p>
      <p>Comfirming your order...</p>
    {:else}
        <p>Hi, {user}!</p>
        <p> {orderMsg} </p>
        <p>Thank you for your order!</p>
        <a href="/product">Shop more!</a>
    {/if}
{/if}