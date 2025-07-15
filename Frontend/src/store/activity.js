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
          console.log("Saved successfully!", get().activities);
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
  }))
);
