import { create } from "zustand";
import { persist } from "zustand/middleware";

export const useAuthStore = create(
  persist(
    (set) => ({
      token: localStorage.getItem("token" || null),
      setToken: (token) => {
        localStorage.setItem("token", token);
        set({ token });
      },
      logout: () => {
        localStorage.removeItem("token");
        set({ token: null });
        window.location.href = "/login";
      },
    }),
    { name: "auth=store" }
  )
);
