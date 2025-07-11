import { create } from "zustand";

export const useSessionStore = create((set) => ({
  sessionID: "",
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
      console.log(responseData.data.message);
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
      set({ sessionID: "" });
      return { success: true, message: responseData.message };
    } else {
      return {
        success: false,
        message: responseData.errorMessage || "Failed to delete session",
      };
    }
  },
}));
