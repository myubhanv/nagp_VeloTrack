<script>
  import Register from '$lib/Register.svelte';
  import { goto } from '$app/navigation';
  import { authStore } from '$lib/stores/auth';
  import { API_BASE_URL } from '$lib/config';

 const handleRegister = async (event) => {
  const { username, password, email, phone } = event.detail;
  console.log('Register attempt with username:', username, 'and password:', password);

  try {
    const response = await fetch(`${API_BASE_URL}/user/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'  
      },
      body: JSON.stringify({
        username: username,
        password: password,
        email: email,
        phone: phone
      })
    });

    const message = await response.text();

    if (response.ok) {
      console.log(message);
      
      alert('Registered successfully! Redirecting to login page...');
      goto('/login');
    }
    else if ((response.ok && message.includes('Wrong'))) {
      console.warn('Unauthorized access:', message);
      alert(`${message}`);
    }
    else {
      console.warn('Registration failed:', message);
      alert(`Backend not supported`);
    }
  } catch (error) {
    console.error('Error during registration:', error);
    alert('Something went wrong. Please try again later.');
  }
};
</script>

<Register on:register={handleRegister} />