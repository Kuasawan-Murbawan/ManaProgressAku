import { create } from "zustand";
import { persist } from "zustand/middleware";
import API from "../api/axios.js";

export const useSessionStore = create(
  persist((set) => ({
    sessionID: "",
    sessions: [],
    createSession: async (newSession) => {
      const res = await API.post("/insertSession", newSession);

      // Set current session ID
      const newSessionID = res.data.data?.sessionID;

      if (newSessionID) {
        set({ sessionID: newSessionID });
        console.log("Session created: ", res.data.data.sessionID);

        return { success: true, message: "Session created!" };
      } else {
        return { success: false, message: "Failed to create session" };
      }
    },
    deleteSession: async (sessionID) => {
      const res = await API.delete(`/deleteSession/${sessionID}`);
      if (res.status == 200) {
        console.log(res);
        console.log("Session deleted: ", sessionID);

        set({ sessionID: "" });
        return { success: true, message: res.data.message };
      } else {
        return {
          success: false,
          message: res.data.errorMessage || "Failed to delete session",
        };
      }
    },
    clearSession: () => {
      set({ sessionID: "" });
    },
    fetchUserSessions: async () => {
      try {
        const res = await API.get("/getUserSessions");
        set({ sessions: res.data.data || [] });
      } catch (error) {
        console.error("Failed to fetch sessions", error);
        set({ sessions: [] });
      }
    },
  }))
);

if (typeof window !== "undefined") {
  window.useSessionStore = useSessionStore;
}
