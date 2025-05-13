// src/utils/axiosConfig.ts
import axios from 'axios';

// Настроим базовый URL для всех запросов
const axiosInstance = axios.create({
    baseURL: 'https://cardealershipwebsite.onrender.com', // Замените на ваш URL сервера
});

export default axiosInstance;
