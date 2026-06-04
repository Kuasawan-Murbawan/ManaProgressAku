import { create } from "zustand";
import { persist } from "zustand/middleware";
import API from "../api/axios.js";

export const useSetStore = create(
	persist((set, get) => ({
		sets: [],
		isLoading: false,

		addSet: async (newSet) => {
			try {
				set({ isLoading: true });
				const res = await API.post(`/insertSet`, newSet);
				set(() => ({
					sets: [...get().sets, res.data.data],
				}));
				return {
					success: true,
					message: "Set inserted successfully",
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
	})),
);
