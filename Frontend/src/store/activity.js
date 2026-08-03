import { create } from "zustand";
import { persist } from "zustand/middleware";
import API from "../api/axios.js";

export const useActivityStore = create(
	persist((set, get) => ({
		// activityID: "",
		activities: [],
		isLoading: false,

		setActivities: (activities) => {
			set({ activities });
		},

		clearActivities: () => {
			set({ activities: [] });
		},

		addActivity: async (newActivity) => {
			try {
				set({ isLoading: true });
				const res = await API.post(`/insertActivity`, newActivity);
				// set({ activityID: res.data.data.activityID });
				return {
					success: true,
					message: "Activity saved successfully",
					activityID: res.data.data.activityID, // return the id instead of saving it globally
				};
			} catch (error) {
				console.error("Error when saving activity", error);
				return {
					success: false,
					message: error,
				};
			} finally {
				set({ isLoading: false });
			}
		},

		deleteActivity: async (activityID) => {
			try {
				const res = await API.delete(`/deleteActivity/${activityID}`);

				return {
					success: true,
					message: res.data.message || "Activity Deleted",
				};
			} catch (error) {
				console.error("Error deleting activity:", error);
				return { success: false, message: error.message };
			}
		},

		deleteActivitiesBySession: async (sessionID) => {
			try {
				const res = await API.delete(
					`/deleteActivitiesBySessionID/${sessionID}`,
				);

				if (res.status === 200) {
					set({ activities: [] });

					return {
						success: true,
						message: res.data.message || "Deleted all activities",
					};
				} else {
					return {
						success: false,
						message: res.data.errorMessage || "Failed to delete activities",
					};
				}
			} catch (error) {
				console.error("Error deleting activities:", error);
				return { success: false, message: error.message };
			}
		},

		updateActivity: async (updatedActivity) => {},
	})),
);
