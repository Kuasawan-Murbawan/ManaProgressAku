import { create } from "zustand";
import { persist } from "zustand/middleware";

export const useActivityStore = create(
  persist((set, get) => ({
    activities: [],

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
    deleteAllActivities: async (activityID) => {
      const { activities } = useActivityStore.getState();
      let allSuccess = true;
      let messages = [];

      for (const activity of activities) {
        try {
          const res = await fetch(
            `/api/deleteActivity/${activity.activityID}`,
            {
              method: "DELETE",
            }
          );

          const responseData = await res.json();

          if (res.ok && responseData.status === "SUCCESS") {
            console.log("Activity deleted: ", activity.activityID);
            messages.push(`Deleted: ${activity.activityID}`);
          } else {
            allSuccess = false;
            messages.push(
              responseData.errorMessage ||
                `Failed to delete activity ${activity.activityID}`
            );
          }
        } catch (err) {
          allSuccess = false;
          messages.push(`Error deleting activity ${activity.activityID}`);
        }
      }

      // Optionally clear local store
      if (allSuccess) {
        useActivityStore.setState({ activities: [] });
      }

      return {
        success: allSuccess,
        message: messages.join("\n"),
      };
    },
    clearActivities: () => {
      set({ activities: [] });
    },
  }))
);
