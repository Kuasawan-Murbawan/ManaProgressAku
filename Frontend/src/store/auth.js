import { create } from "zustand";
import { persist } from "zustand/middleware";
import { jwtDecode } from "jwt-decode";

/*
Purpose: authentication, authorization, UI decision
Data : token, (id, email, role)
Source: JWT payload
*/

export const useAuthStore = create(
  persist(
    (set) => ({
      // token: localStorage.getItem("token" || null),
      token: null,
      user: null,

      setToken: (token) => {
        // localStorage.setItem("token", token);
        // set({ token });

        const decoded = jwtDecode(token);

        const user = {
          id: decoded.userID,
          role: decoded.role,
          email: decoded.sub,
        };

        set({ token, user });
      },
      logout: () => {
        // localStorage.removeItem("token");
        set({ token: null, user: null });
        window.location.href = "/login";
      },
    }),
    { name: "auth=store" },
  ),
);
