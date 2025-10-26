<script>
	import '../app.css';
	import { authStore } from '$lib/stores/auth';
  	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';

	let { children } = $props();

	/*const auth = $state(authStore); 
	let username = auth.username || 'Guest';

  onMount(() => {
    const savedAuth = localStorage.getItem('auth');
    if (savedAuth) {
      const { token, username: savedUsername } = JSON.parse(savedAuth); 
      authStore.set({
        isAuthenticated: true,
        token,
        username: savedUsername
      });
    } 
	 });*/
	authStore.subscribe(auth => {
		if (auth.isAuthenticated) {
			console.log('User is authenticated:', auth.username);
		} else {
			console.log('User is not authenticated');
		}
	});

	function logout() {
		authStore.set({
			isAuthenticated: false,
			token: null,
			username: null
		});
		localStorage.removeItem('auth'); 
		alert('You have been logged out successfully.');
		goto('/');
	}

</script>

<div class="app">
	<header> 
		<a href="/"> HOME </a>
		{#if $authStore.isAuthenticated}
    		<p>Hi, {$authStore.username}</p>
			<button on:click={logout}>Logout</button>
  		{:else}
			<p>Welcome, Guest!</p>
    		<a href = "/login"> Login </a>OR<a href = "/register"> Register </a>
  		{/if}
		
	</header>
	<slot/>
	<footer> 
		<a href = "/about"> About </a>
		<p> NAGP VeloTrack </p>
		<div>
			{#if $authStore.isAuthenticated}
				<a href = "/cart"> Cart </a>
			{:else}
				<p>Login to place order!</p>
			{/if}
		</div>
	</footer>
</div>

<style>
	.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding-top: 70px; /* Space for fixed header */
  align-items: center; /* Horizontal center */
  justify-content: center; /* Vertical center */
  text-align: center;
}

/* Header */
header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #ffffff;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

header a {
  text-decoration: none;
  color: #333;
  font-weight: bold;
  font-size: 16px;
}

header a:hover {
  color: orange;
}

/* Footer */
footer {
  position: fixed;   
  bottom: 0;
  left: 0;
  width: 100%;
  display: flex;
  justify-content: space-between; 
  align-items: center;
  padding: 10px 20px;
  background-color: #f8f8f8;
  border-top: 1px solid #ccc;
}

footer p {
  margin: 0;
  text-align: center;
  flex: 1;            
}

footer a {
  text-decoration: none;
  color: #333;
}

footer div {
  display: flex;
  align-items: center;
}
</style>
