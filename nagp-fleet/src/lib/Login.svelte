<script>
    import {createEventDispatcher} from 'svelte';
    let username = '';
    let password = '';
    const dispatch =createEventDispatcher();

    const handleSubmit = (event) => {
        dispatch('login',{username,password});
    }
</script>
<div class="login-container">
  <form class="login-form" on:submit|preventDefault={handleSubmit}>
    <h2>ENTER LOGIN CREDENTIALS</h2>
    
    <div class="form-group">
      <label for="username">Username</label>
      <input type="text" id="username" bind:value={username} required />
    </div>
    
    <div class="form-group">
      <label for="password">Password</label>
      <input type="password" id="password" bind:value={password} required />
    </div>
    
    <button class="login-button" type="submit">Login</button>
  </form>
</div>

<style>
    .login-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: linear-gradient(to right, #f9f9f9, #ececec);
    font-family: 'Segoe UI', Tahoma, sans-serif;
    }

    .login-form {
    background: #ffffff;
    padding: 30px 40px;
    border-radius: 12px;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
    width: 320px;
    }

    h2 {
    text-align: center;
    color: #333;
    margin-bottom: 20px;
    }

    .form-group {
    margin-bottom: 15px;
    }

    label {
    display: block;
    font-weight: 600;
    margin-bottom: 5px;
    color: #555;
    }

    input {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #ccc;
    border-radius: 8px;
    font-size: 14px;
    outline: none;
    transition: border-color 0.3s;
    }

    input:focus {
    border-color: orange;
    box-shadow: 0 0 4px rgba(255, 165, 0, 0.3);
    }

    .login-button {
    width: 100%;
    background-color: #ff9800;
    color: #fff;
    padding: 12px;
    border: none;
    border-radius: 8px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.2s;
    }

    .login-button:hover {
    background-color: #e68900;
    transform: translateY(-1px);
    }

    .login-button:active {
    transform: translateY(1px);
    }

</style>