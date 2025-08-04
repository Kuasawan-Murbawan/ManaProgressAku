import { create } from "zustand";
import { persist } from "zustand/middleware";

export const useExerciseStore = create(
  persist((set) => ({
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
    getExerciseName: (id) => {
      const { exercise } = useExerciseStore.getState();
      const found = exercise.find((e) => e.exerciseID === id);
      return found ? found.exerciseName : `Unknown Exercise: ${id}`;
    },
  }))
);
