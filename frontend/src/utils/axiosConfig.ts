// src/utils/axiosConfig.ts
import axios from 'axios';

// Настроим базовый URL для всех запросов
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // Замените на ваш URL сервера
    timeout: 100   , // Таймаут для запросов
});

export default axiosInstance;
