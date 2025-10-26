import { writable } from 'svelte/store';

export const authStore = writable({
  isAuthenticated: false,
  token: null,
  username: null
});
