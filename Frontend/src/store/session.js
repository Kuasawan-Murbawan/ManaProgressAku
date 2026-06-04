import { create } from "zustand";
import { persist } from "zustand/middleware";
import API from "../api/axios.js";

export const useSessionStore = create(
	persist((set, get) => ({
		sessionID: "",
		sessions: [],
		isLoading: false,
		currentSessionDetails: null,

		createSession: async (newSession) => {
			try {
				set({ isLoading: true });

				const res = await API.post("/insertSession", newSession);

				// Set current session ID
				const newSessionID = res.data.data?.sessionID;
				if (newSessionID) {
					set({ sessionID: newSessionID });

					return { success: true, message: "Session created!" };
				} else {
					return { success: false, message: "Failed to create session" };
				}
			} catch (error) {
				if (error.response?.status === 409) {
					return {
						success: false,
						message: "Active session exists!",
					};
				}
				return {
					success: false,
					message: "Error creating session.",
				};
			} finally {
				set({ isLoading: false });
			}
		},

		activeSession: async () => {
			try {
				set({ isLoading: true });
				const res = await API.get(`/session/active`);

				if (res.status == 200) {
					if (res.data.data.hasActiveSession) {
						set({ sessionID: res.data.data.sessionID });
						return true;
					}
					return false;
				}
			} catch (error) {
				console.error("Failed to check active session", error);
				return false;
			} finally {
				set({ isLoading: false });
			}
		},

		finishSession: async () => {
			try {
				set({ isLoading: true });
				const sessionID = get().sessionID;
				const res = await API.patch(`/finishSession/${sessionID}`);
				if (res.status == 200) {
					set({ sessionID: "" });
					return { success: true, message: res.data.message };
				}
			} catch (error) {
				console.error("Failed to finish session.", error);
			} finally {
				set({ isLoading: false });
			}
		},

		deleteSession: async (sessionID) => {
			const res = await API.delete(`/deleteSession/${sessionID}`);
			if (res.status == 200) {
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
				set({ isLoading: true });
				const res = await API.get("/getUserSessions");
				set({ sessions: res.data.data || [] });
			} catch (error) {
				console.error("Failed to fetch sessions", error);
				set({ sessions: [] });
			} finally {
				set({ isLoading: false });
			}
		},

		fetchSessionDetails: async (sessionID) => {
			try {
				set({ isLoading: true });

				const res = await API.get(`/sessions/${sessionID}/details`);

				set({ currentSessionDetails: res.data.data });

				return {
					success: true,
					message: "Successfully fetched session details",
				};
			} catch (error) {
				console.error("Failed to fetch session details", error);

				return {
					success: false,
					message: "Failed to fetch session details",
				};
			} finally {
				set({ isLoading: false });
			}
		},
	})),
);

if (typeof window !== "undefined") {
	window.useSessionStore = useSessionStore;
}
