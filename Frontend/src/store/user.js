import API from "../api/axios.js";
import { create } from "zustand";
import { persist } from "zustand/middleware";
import { useAuthStore } from "./auth.js";

/*
Purpose: full user profile
Data: name, phone, preferences, etc
Source: API, /users/me
*/

export const useUserStore = create(
  persist(
    (set, get) => ({
      currentUser: null,

      fetchCurrentUser: async () => {
        try {
          const token = useAuthStore.getState().token;
          if (!token) return;

          const res = await API.get("/users/me");
          set({ currentUser: res.data });
        } catch (error) {
          console.error("Error fetching user", error);
          set({ currentUser: null });
        }
      },

      clearUser: () => set({ currentUser: null }),
    }),
    {
      name: "user-store",
    },
  ),
);
