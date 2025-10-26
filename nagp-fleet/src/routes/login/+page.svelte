<script>
  import Login from '$lib/Login.svelte';
  import { goto } from '$app/navigation';
  import { authStore } from '$lib/stores/auth';
  import { API_BASE_URL } from '$lib/config';

 const handleLogin = async (event) => {
  const { username, password } = event.detail;
  console.log('Login attempt with username:', username, 'and password:', password);

  try {
    const response = await fetch(`${API_BASE_URL}/user/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'  
      },
      body: JSON.stringify({
        username: username,
        password: password
      })
    });

    const message = await response.text();

    if (response.ok) {
      console.log(message);
      const token = message.accessToken;

      authStore.set({
        isAuthenticated: true,
        token: token,
        username
      });

      localStorage.setItem('auth', JSON.stringify({
        token: token,
        username
      }));

      alert('Login successful! Redirecting to bikes list page...');
      goto('/bike'); 
    }
    else {
      const message = data.error || 'Unknown error';
      if (message.includes('Invalid') || message.includes('Wrong')) {
        console.warn('Unauthorized access:', message);
        alert(message);
      } else {
        alert(`Error: ${message}`);
      }
    }
  } catch (error) {
    console.error('Error during login:', error);
    alert('Something went wrong. Please try again later.');
  }
};
</script>

<Login on:login={handleLogin} />