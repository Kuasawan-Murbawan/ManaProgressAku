import axios from "axios";
import { useAuthStore } from "../store/auth";

const API = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

// Ensure every API call automatically includes the JWT Token
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

API.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const message = error.response?.data?.message?.toLowerCase();

    if (
      (status === 401 || status === 403) &&
      message &&
      (message.includes("expired") || message.includes("invalid token"))
    ) {
      console.warn("Token expired or invalid, logging you out..");
      const authStore = useAuthStore.getState();
      authStore.logout();
    }

    return Promise.reject(error);
  },
);

export default API;
