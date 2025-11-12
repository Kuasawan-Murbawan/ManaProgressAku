import { create } from "zustand";
import { persist } from "zustand/middleware";
import API from "../api/axios.js";

export const useActivityStore = create(
  persist((set, get) => ({
    activities: [],
    // sessionActivities: [],
    addActivity: async (newActivity) => {
      try {
        const res = await API.post("/insertActivity", newActivity);

        if (res.status === 200) {
          set(() => ({
            activities: [...get().activities, res.data.data],
          }));
          return { success: true, message: "Activity saved successfully!" };
        } else {
          console.log("sum ting wong");
          return {
            success: false,
            message: res.data.message || "Failed to save activity.",
          };
        }
      } catch (err) {
        console.error("Activity save error:", err);
        return {
          success: false,
          message: "Something went wrong while saving activity.",
        };
      }
    },

    deleteActivity: async (activityID) => {
      try {
        const res = await API.delete(`/deleteActivity/${activityID}`);

        if (res.status == 200) {
          console.log("Activity deleted ", res.data.data.activityID);

          set((state) => ({
            activities: state.activities.filter(
              (activity) => activity.activityID !== activityID
            ),
          }));

          return {
            success: true,
            message: res.data.message || "Activity Deleted",
          };
        } else {
          return {
            success: false,
            message: res.data.errorMessage || "Failed to delete activity",
          };
        }
      } catch (error) {
        console.error("Error deleting activity:", error);
        return { success: false, message: error.message };
      }
    },

    deleteActivitiesBySession: async (sessionID) => {
      try {
        const res = await API.delete(
          `/deleteActivitiesBySessionID/${sessionID}`
        );

        if (res.status === 200) {
          console.log("All activities deleted for session:", sessionID);

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

    clearActivities: () => {
      set({ activities: [] });
    },

    updateActivity: async (updatedActivity) => {},

    fetchActivityBySession: async (sessionID) => {
      try {
        const res = await API.get(`/sessionActivities/${sessionID}`);

        if (res.status == 200) {
          if (res.data.data.length === 0) {
            console.log("No activities found for this session.");
            set({ activities: [] });
          } else {
            set({ activities: res.data.data });
          }
        } else {
          console.error("Failed to fetch activities:", res.message);
        }
      } catch (error) {
        console.error("Error fetching activities:", error);
      }
    },
  }))
);
