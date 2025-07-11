import React, { useEffect, useState } from "react";
import { useExerciseStore } from "../store/exercise";
import { Box, Text, useDisclosure } from "@chakra-ui/react";
import ExerciseDetailModal from "./ExerciseDetailModal";
const ExerciseListComponent = () => {
  const { fetchAllExercises, exercise } = useExerciseStore();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [selectedExercise, setSelectedExercise] = useState(null);

  useEffect(() => {
    fetchAllExercises();
  }, [fetchAllExercises]);

  const handleClick = (exercise) => {
    setSelectedExercise(exercise);
    console.log(exercise.exerciseType);
    onOpen();
  };

  return (
    <div>
      <h2>Exercise List</h2>

      {exercise.map((exercise, index) => (
        <Box
          key={exercise.exerciseID}
          p={4}
          bg="gray.100"
          mb={2}
          borderRadius="md"
          cursor="pointer"
          onClick={() => handleClick(exercise)}
        >
          <Text>
            {exercise.exerciseID}: {exercise.exerciseName}
          </Text>
        </Box>
      ))}

      <ExerciseDetailModal
        isOpen={isOpen}
        onClose={onClose}
        currentExercise={selectedExercise}
      />
    </div>
  );
};

export default ExerciseListComponent;
