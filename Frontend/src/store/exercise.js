import { create } from "zustand";

export const useExerciseStore = create((set) => ({
  exercise: [],
  fetchAllExercises: async () => {
    try {
      const res = await fetch("/api/admin/getAllExercises"); // frontend will use Vite proxy
      const responseData = await res.json(); // ✅ await here
      set({ exercise: responseData.data || [] });
    } catch (error) {
      console.error("Failed to fetch exercises:", error);
      set({ exercise: [] });
    }
  },
}));
