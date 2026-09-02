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

	clearProfile: () => {
		set({ profile: null });
	},
}));
