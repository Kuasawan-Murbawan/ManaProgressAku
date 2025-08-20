import React, { useEffect, useState } from "react";
import { useExerciseStore } from "../store/exercise";
import {
  Box,
  Text,
  useDisclosure,
  SimpleGrid,
  Heading,
  Badge,
  Card,
  CardBody,
  VStack,
  Divider,
} from "@chakra-ui/react";
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
    onOpen();
  };

  // Split exercises by type
  const upperBodyExercises = exercise.filter((ex) => ex.exerciseType === "1");
  const lowerBodyExercises = exercise.filter((ex) => ex.exerciseType === "2");

  // A reusable grid
  const ExerciseGrid = ({ title, items, color }) => (
    <Box p={3}>
      <Heading mb={4} fontSize="xl" color={color}>
        {title}
      </Heading>
      <SimpleGrid columns={[1, 2, 3]} spacing={6}>
        {items.map((exercise) => (
          <Card
            key={exercise.exerciseID}
            borderRadius="2xl"
            shadow="sm"
            bg={`${color}.50`}
            _hover={{
              shadow: "xl",
              transform: "scale(1.04)",
              transition: "0.2s ease-in-out",
              bg: `${color}.100`,
            }}
            cursor="pointer"
            onClick={() => handleClick(exercise)}
          >
            <CardBody>
              <VStack align="start" spacing={2}>
                <Text
                  fontSize="lg"
                  fontWeight="semibold"
                  color={`${color}.700`}
                >
                  {exercise.exerciseName}
                </Text>

                <Text fontSize="sm" color="gray.600">
                  {exercise.generalInfo}
                </Text>
              </VStack>
            </CardBody>
          </Card>
        ))}
      </SimpleGrid>
    </Box>
  );

  return (
    <Box p={6} bg={"gray.300"}>
      <Heading mb={8} fontSize="2xl" textAlign="center">
        Exercise Library
      </Heading>

      {/* Upper Body Section */}
      <ExerciseGrid
        title="Upper Body"
        items={upperBodyExercises}
        color="blue"
      />

      {/* Lower Body Section */}
      <ExerciseGrid
        title="Lower Body"
        items={lowerBodyExercises}
        color="teal"
      />

      <ExerciseDetailModal
        isOpen={isOpen}
        onClose={onClose}
        currentExercise={selectedExercise}
      />
    </Box>
  );
};

export default ExerciseListComponent;
