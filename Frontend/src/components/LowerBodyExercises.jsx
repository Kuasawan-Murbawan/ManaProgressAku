import React, { useEffect } from "react";
import {
  Box,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalHeader,
  ModalOverlay,
  VStack,
  Text,
} from "@chakra-ui/react";
import { useExerciseStore } from "../store/exercise";
import { useNavigate } from "react-router-dom";

const LowerBodyExercises = ({ isOpenLowerBody, onCloseLowerBody }) => {
  const { exercise, fetchAllExercises } = useExerciseStore();

  useEffect(() => {
    if (exercise.length < 10) {
      fetchAllExercises();
    }
  }, [exercise, fetchAllExercises]);

  function lowerBodyFilter(exercises) {
    return exercises.filter((item) => item.exerciseType === "2");
  }

  const LowerBodyExercises = lowerBodyFilter(exercise);

  const navigate = useNavigate();

  const handleClick = (exercise) => {
    onCloseLowerBody();
    navigate("/currentActivity", {
      state: {
        exercise: exercise,
      },
    });
  };

  return (
    <Modal isOpen={isOpenLowerBody} onClose={onCloseLowerBody}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Lower Body Exercises</ModalHeader>
        <ModalCloseButton />
        <VStack spacing={5} align="start">
          {LowerBodyExercises.map((exer) => (
            <Box
              key={exer.exerciseID}
              h={"12"}
              bg={"blue.300"}
              w={"full"}
              onClick={() => handleClick(exer)}
            >
              <Text textAlign={"center"}>
                {exer.exerciseID}. {exer.exerciseName}
              </Text>
            </Box>
          ))}
        </VStack>
      </ModalContent>
    </Modal>
  );
};

export default LowerBodyExercises;
