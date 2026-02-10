import API from "../api/axios.js";
import { create } from "zustand";
import { persist } from "zustand/middleware";

export const useUserStore = create(
  persist((set, get) => ({
    currentUser: null,

    fetchCurrentUser: async () => {
      try {
        const res = await API.get("/users/me");
        console.log(res.data);
        set({ currentUser: res.data });
      } catch (error) {
        console.error("Error fetching user", error);
        set({ currentUser: null });
      }
    },
  })),
);
