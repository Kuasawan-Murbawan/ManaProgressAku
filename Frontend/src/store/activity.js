import { create } from "zustand";
import { persist } from "zustand/middleware";

export const useActivityStore = create(
  persist((set, get) => ({
    activities: [],
    // sessionActivities: [],
    addActivity: async (newActivity) => {
      try {
        const res = await fetch("/api/insertActivity", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(newActivity),
        });

        const data = await res.json();

        if (data.status === "SUCCESS") {
          set(() => ({
            activities: [...get().activities, data.data],
          }));
          return { success: true, message: "Activity saved successfully!" };
        } else {
          console.log("sum ting wong");
          return {
            success: false,
            message: data.message || "Failed to save activity.",
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
        const res = await fetch(`/api/deleteActivity/${activityID}`, {
          method: "DELETE",
        });

        const responseData = await res.json();

        if (res.ok && responseData.status === "SUCCESS") {
          console.log("Activity deleted ", responseData.data.activityID);

          set((state) => ({
            activities: state.activities.filter(
              (activity) => activity.activityID !== activityID
            ),
          }));

          return {
            success: true,
            message: responseData.message || "Activity Deleted",
          };
        } else {
          return {
            success: false,
            message: responseData.errorMessage || "Failed to delete activity",
          };
        }
      } catch (error) {
        console.error("Error deleting activity:", error);
        return { success: false, message: error.message };
      }
    },

    deleteActivitiesBySession: async (sessionID) => {
      try {
        const res = await fetch(
          `/api/deleteActivitiesBySessionID/${sessionID}`,
          {
            method: "DELETE",
          }
        );

        const responseData = await res.json();

        if (res.ok && responseData.status === "SUCCESS") {
          console.log("All activities deleted for session:", sessionID);

          set({ activities: [] });

          return {
            success: true,
            message: responseData.message || "Deleted all activities",
          };
        } else {
          return {
            success: false,
            message: responseData.errorMessage || "Failed to delete activities",
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
        const res = await fetch(`/api/sessionActivities/${sessionID}`);
        const responseData = await res.json();

        if (res.ok) {
          if (responseData.data.length === 0) {
            console.log("No activities found for this session.");
            set({ activities: [] });
          } else {
            set({ activities: responseData.data });
          }
        } else {
          console.error("Failed to fetch activities:", responseData.message);
        }
      } catch (error) {
        console.error("Error fetching activities:", error);
      }
    },
  }))
);
