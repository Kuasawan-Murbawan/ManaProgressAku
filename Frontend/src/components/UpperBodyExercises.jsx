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
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useExerciseStore } from "../store/exercise";

const UpperBodyExercises = ({ isOpenUpperBody, onCloseUpperBody }) => {
  const { exercise, fetchAllExercises } = useExerciseStore();

  useEffect(() => {
    if (exercise.length <= 10) {
      fetchAllExercises();
    }
  }, [exercise, fetchAllExercises]);

  function upperBodyFilter(exercises) {
    return exercises.filter((item) => item.exerciseType === "1");
  }

  const UpperBodyExercises = upperBodyFilter(exercise);

  const navigate = useNavigate();

  const handleClick = (exercise) => {
    onCloseUpperBody();
    navigate("/currentActivity", {
      state: {
        exercise: exercise,
      },
    });
  };

  return (
    <Modal isOpen={isOpenUpperBody} onClose={onCloseUpperBody}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Uper Body Exercises</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <VStack spacing={5} align="start">
            {UpperBodyExercises.map((exer) => (
              <Box
                key={exer.exerciseID}
                h={"12"}
                bg={"yellow.300"}
                w={"full"}
                onClick={() => handleClick(exer)}
              >
                <Text textAlign={"center"}>
                  {exer.exerciseID}. {exer.exerciseName}
                </Text>
              </Box>
            ))}
          </VStack>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
};

export default UpperBodyExercises;
