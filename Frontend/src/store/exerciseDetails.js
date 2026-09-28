import { create } from "zustand";
import API from "../api/axios.js";

export const useExerciseDetailsStore = create((set, get) => ({
	detailsByExerciseId: {},
	loadingByExerciseId: {},
	searchResults: [],
	isSearching: false,

	fetchDetails: async (exerciseID) => {
		// Avoid duplicate in-flight fetches for the same exercise within one page's lifetime
		// useEffect usually rerenders the page
		if (get().loadingByExerciseId[exerciseID]) {
			return { success: true, skipped: true };
		}

		set((state) => ({
			loadingByExerciseId: { ...state.loadingByExerciseId, [exerciseID]: true },
		}));

		try {
			const res = await API.get(`/getExerciseDetails/${exerciseID}`);
			set((state) => ({
				detailsByExerciseId: {
					...state.detailsByExerciseId,
					[exerciseID]: res.data.data,
				},
			}));
			return { success: true, data: res.data.data };
		} catch (error) {
			// Per the backend's graceful-degradation design, this endpoint
			// itself should always return 200 with available:false rather
			// than erroring — a network/auth failure here is the one
			// genuinely exceptional case.
			console.error("Failed to fetch exercise details:", error);
			return { success: false };
		} finally {
			set((state) => ({
				loadingByExerciseId: {
					...state.loadingByExerciseId,
					[exerciseID]: false,
				},
			}));
		}
	},

	// Admin search if exist the same exercise
	searchExerciseDbMatches: async (query) => {
		set({ isSearching: true });
		try {
			const res = await API.get("/searchExerciseDbMatches", {
				params: { query },
			});
			set({ searchResults: res.data.data });
			return { success: true, data: res.data.data };
		} catch (error) {
			return {
				success: false,
				message: error.response?.data?.errorMessage || "Search failed",
			};
		} finally {
			set({ isSearching: false });
		}
	},

	clearSearchResults: () => {
		set({ searchResults: [] });
	},
}));
