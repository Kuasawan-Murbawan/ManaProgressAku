import { create } from "zustand";
import API from "../api/axios.js";

export const useProfileStore = create((set, get) => ({
	profile: null,
	isLoading: false,

	fetchProfile: async () => {
		try {
			set({ isLoading: true });
			const res = await API.get("/getProfile");
			set({ profile: res.data.data });
			return { success: true };
		} catch (error) {
			console.error("Failed to fetch profile:", error);
			return { success: false, message: "Failed to fetch profile" };
		} finally {
			set({ isLoading: false });
		}
	},

	updateProfile: async (updates) => {
		try {
			set({ isLoading: true });
			const res = await API.put("/updateProfile", updates);
			set({ profile: res.data.data });
			return { success: true, message: res.data.message };
		} catch (error) {
			console.error("Failed to update profile:", error);
			return {
				success: false,
				message:
					error.response?.data?.errorMessage || "Failed to update profile",
			};
		} finally {
			set({ isLoading: false });
		}
	},

	deleteAccount: async (password) => {
		try {
			// axios DELETE needs the body passed via config.data, not as a second positional arg
			const res = await API.delete("/deleteUser", { data: { password } });
			return { success: true, message: res.data.message };
		} catch (error) {
			return {
				success: false,
				message:
					error.response?.data?.errorMessage || "Failed to delete account",
			};
		}
	},

	// Returns { success, message, fieldErrors }.
	// fieldErrors is the backend's errorDetails, for example
	// { currentPassword: "Current password is incorrect" }, so the form can
	// show each message under the right input.
	changePassword: async (passwords) => {
		try {
			const res = await API.put("/changepw", passwords);
			return { success: true, message: res.data.message, fieldErrors: {} };
		} catch (error) {
			const data = error.response?.data;
			return {
				success: false,
				message: error.response
					? data?.errorMessage || "Failed to change password"
					: "Could not reach the server. Check your connection and try again.",
				fieldErrors: data?.errorDetails ?? {},
			};
		}
	},

	clearProfile: () => {
		set({ profile: null });
	},
}));
