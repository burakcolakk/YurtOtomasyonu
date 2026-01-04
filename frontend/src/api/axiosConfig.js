// Dosya: src/api/axiosConfig.js
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8181/api', // Backend portun farklıysa burayı değiştir!
});

// Her isteğe otomatik Token ekleme (Login olduktan sonra lazım olacak)
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default api;