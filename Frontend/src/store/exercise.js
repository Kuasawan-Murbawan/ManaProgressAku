import { create } from "zustand";
import { persist } from "zustand/middleware";
import API from "../api/axios.js";

export const useExerciseStore = create(
  persist((set) => ({
    exercise: [],
    fetchAllExercises: async () => {
      try {
        const res = await API.get("/admin/getAllExercises"); // frontend will use Vite proxy

        set({ exercise: res.data.data || [] });
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
