import { create } from "zustand";
import { persist } from "zustand/middleware";
import API from "../api/axios.js";

export const useExerciseStore = create(
  persist((set, get) => ({
    exercise: [],
    fetchAllExercises: async () => {
      try {
        const res = await API.get("/getAllExercises"); // frontend will use Vite proxy

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
    insertExercise: async (newExercise) => {
      try {
        const res = await API.post("/insertExercise", newExercise);
        await get().fetchAllExercises();
      } catch (error) {
        console.error("Failed to add new exercise", error);
      }
    },
  })),
);
