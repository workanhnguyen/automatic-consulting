// @ts-ignore
import Cookies from 'js-cookie';
import axios from 'axios';

import AuthApi from './AuthApi';

const axiosClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_HOST,
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.request.use(
  async (config) => {
    const token = Cookies.get('token');
    config.headers.Authorization = `Bearer ${token || ''}`;
    return config;
  },
  async (error) => Promise.reject(error)
);

axiosClient.interceptors.response.use(
  async (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = Cookies.get('refreshToken');
      if (refreshToken) {
        const res = await AuthApi.refreshToken(refreshToken);
        Cookies.set('token', res.data.token);
        Cookies.set('refreshToken', res.data.refreshToken);

        originalRequest.headers['Authorization'] = `Bearer ${res.data.token}`;
        return axiosClient(originalRequest);
      } else {
        Cookies.remove('refreshToken');
        Cookies.remove('token');
        localStorage.removeItem('isFirstVisit');
        return Promise.reject(error);
      }
    } 
    else return Promise.reject(error);
  }
);

export default axiosClient;
