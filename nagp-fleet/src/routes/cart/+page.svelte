<script>
import { cartStore } from '$lib/stores/cartStore';
import { goto } from '$app/navigation';

let cartItems = [];
let total = 0;

cartStore.subscribe(items => {
    cartItems = items;
    total = items.reduce((sum, item) => sum + item.price * item.quantity, 0);
});
 
  function updateQuantity(id, newQty, stock) {
    cartStore.update(cart => {
      return cart.map(item =>
        item.id === id
          ? { ...item, quantity: Math.min(Math.max(newQty, 1), stock) }
          : item
      );
    });
  }

  function removeItem(id) {
    cartStore.update(cart => cart.filter(item => item.id !== id));
  }
</script>

<h2>Your Shopping Cart</h2>

{#if cartItems.length > 0}
  <table>
    <thead>
      <tr>
        <th>Product</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Total</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
      {#each cartItems as item}
        <tr>
          <td>{item.name}</td>
          <td>${item.price}</td>
          <td>
            <input
              type="number"
              min="1"
              max={item.stock}
              value={item.quantity}
              on:input={(e) => updateQuantity(item.id, +e.target.value, item.stock)}
            />
          </td>
          <td>${item.price * item.quantity}</td>
          <td>
            <button on:click={() => removeItem(item.id)}>Remove</button>
          </td>
        </tr>
      {/each}
    </tbody>
  </table>
  
    <button on:click={() => cartStore.set([])}>Clear Cart</button>
    <button on:click={() => goto('/checkout')}>Checkout</button>
{:else}
  <p>Your cart is empty.</p>
{/if}
<p>Total: ${total}</p>
<a href="/product">Continue Shopping</a>
