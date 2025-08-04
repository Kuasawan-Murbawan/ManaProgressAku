import { create } from "zustand";
import { persist } from "zustand/middleware";

export const useSessionStore = create(
  persist((set) => ({
    sessionID: "",
    sessions: [],
    createSession: async (newSession) => {
      const res = await fetch(`/api/insertSession`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newSession),
      });

      const responseData = await res.json();

      const newSessionID = responseData.data?.sessionID;

      if (newSessionID) {
        set({ sessionID: newSessionID });
        console.log("Session created: ", responseData.data.sessionID);

        return { success: true, message: "Session created!" };
      } else {
        return { success: false, message: "Failed to create session" };
      }
    },
    deleteSession: async () => {
      const { sessionID } = useSessionStore.getState();

      const res = await fetch(`/api/deleteSession/${sessionID}`, {
        method: "DELETE",
      });

      const responseData = await res.json();

      if (res.ok && responseData.status === "SUCCESS") {
        console.log("Session deleted: ", sessionID);

        set({ sessionID: "" });
        return { success: true, message: responseData.message };
      } else {
        return {
          success: false,
          message: responseData.errorMessage || "Failed to delete session",
        };
      }
    },
    clearSession: () => {
      set({ sessionID: "" });
    },
    fetchAllSessions: async () => {
      try {
        const res = await fetch("/api/getAllSessions");
        const responseData = await res.json();
        set({ sessions: responseData.data || [] });
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
