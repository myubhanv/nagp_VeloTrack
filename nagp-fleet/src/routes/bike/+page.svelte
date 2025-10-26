<script>
    import { onMount } from 'svelte';
    import { productStore } from '$lib/stores/productStore';
    import { goto } from '$app/navigation';
    import { API_BASE_URL } from '$lib/config';

  let products = [];
  let searchKey = '';
  let sortPrice = 'asc';
  let loading = true;

  onMount(async () => {
    try{
      const res = await fetch(`${API_BASE_URL}/product/allProducts`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
      });
      products = await res.json();
      productStore.set(products); 
      console.log('Fetched products:', products);
    } catch (error) {
      console.error('Error during fetching products:', error);
    } finally {
      loading = false;
    }
    
  });

  $: filteredProducts = products.filter(product => {
    const lowerKey = searchKey.toLowerCase();
    return (
      product.name.toLowerCase().includes(lowerKey) ||
      product.category.toLowerCase().includes(lowerKey)
    );
  });

  $: sortedProducts = [...filteredProducts].sort((a, b) => {
    return sortPrice === 'asc' ? a.price - b.price : b.price - a.price;
  });

  function toggleSortByPrice() {
    sortPrice = sortPrice === 'asc' ? 'desc' : 'asc';
    console.log("Sort order changed to:", sortPrice);
  }

</script>

<h1> This is bike listing Page. Here you can find a variety of bikes to choose from!</h1>
{#if loading}
  <p>Loading bikes...</p>
{:else}
  
<div class="controls">
  <div class="filter">
    <label for="search">Search Bikes:</label>
    <input type="text" placeholder="Search by name or category..." bind:value={searchKey} class="search-bar"/>
  </div>
  <div class="sorting">
    <button on:click={toggleSortByPrice}>
      Sort by Price {sortPrice === 'asc' ? 'Low to High' : 'High to Low'}
    </button>
  </div>
</div>
{#if filteredProducts.length === 0}
  <p>No bikes found with this search term.</p>
{:else}
  <p>Click on a bike to view details.</p>
{/if}

<div class="product-grid">
  {#each sortedProducts as product}
    <div class="product-card" on:click={() => goto(`/product/${product.id}`)}>
      <h3>{product.name}</h3>
      <p>Price: ${product.price}</p>
      <p>Category : {product.category}</p>
      <p>{product.availability}</p>
      <button>View Details</button>
    </div>
  {/each}
</div>
{/if}
<style>
  .product-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    margin-top: 1rem;
  }

  .product-card {
    background: #fff;
    border: 1px solid #ddd;
    border-radius: 10px;
    padding: 1rem;
    text-align: center;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: transform 0.2s ease-in-out;
  }

  .product-card:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  }

  .product-card button {
    margin-top: 10px;
    padding: 5px 10px;
    background-color:rgb(179, 255, 0);
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .product-card button:hover {
    background-color: #0056b3;
  }

  .controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding: 0.5rem;
  }
  .search-bar {
    width: 300px;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  
</style>