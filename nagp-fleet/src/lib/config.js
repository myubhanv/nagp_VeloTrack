export const API_BASE_URL =
    import.meta.env.MODE === 'development'
        ? '/api'                 // For local dev → uses Vite proxy
        : '/api'; // because nginx handles the proxy
