import { create } from "zustand";
import API from "../api/axios.js";

export const useSetStore = create((set) => ({
	isLoading: false,

	addSet: async (newSet) => {
		try {
			set({ isLoading: true });
			const res = await API.post(`/insertSet`, newSet);

			return {
				success: true,
				message: "Set inserted successfully",
				set: res.data.data,
			};
		} catch (error) {
			return {
				success: false,
				message: error,
			};
		} finally {
			set({ isLoading: false });
		}
	},
}));
