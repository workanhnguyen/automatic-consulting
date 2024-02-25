// @ts-ignore
import Cookies from "js-cookie";
import axios from "axios";

import AuthApi from "./AuthApi";

const axiosClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_HOST,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosClient.interceptors.request.use(
  (config) => {
    const token = Cookies.get("token");
    config.headers.Authorization = `Bearer ${token || ""}`;
    return config;
  },
  (error) => Promise.reject(error)
);

axiosClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = Cookies.get("refreshToken");
      if (refreshToken) {
        const res = await AuthApi.refreshToken(refreshToken);
        Cookies.set("token", res.data.token);
        Cookies.set("refreshToken", res.data.refreshToken);

        originalRequest.headers["Authorization"] = `Bearer ${res.data.token}`;
        return axiosClient(originalRequest);
      } else {
        Cookies.remove("refreshToken");
        Cookies.remove("token");
        return Promise.reject(error);
      }
    } else if (error.response?.status === 403) {
      Cookies.remove("refreshToken");
      Cookies.remove("token");
      window.location.reload();
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

export default axiosClient;
