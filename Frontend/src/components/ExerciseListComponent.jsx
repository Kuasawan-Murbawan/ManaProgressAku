import React, { useEffect } from "react";
import { useExerciseStore } from "../store/exercise";
import { GridItem } from "@chakra-ui/react";
const ExerciseListComponent = () => {
  const { fetchAllExercises, exercise } = useExerciseStore();

  useEffect(() => {
    fetchAllExercises();
    console.log("All exercise: " + exercise);
  }, [fetchAllExercises]);

  return (
    <div>
      <h2>Exercise List</h2>

      {exercise.map((exercise, index) => (
        <div key={exercise.exerciseID}>
          <span>
            {exercise.exerciseID}: {exercise.exerciseName}
          </span>
        </div>
      ))}
    </div>
  );
};

export default ExerciseListComponent;
