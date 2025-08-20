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
    <Modal
      isOpen={isOpenLowerBody}
      onClose={onCloseLowerBody}
      size="md"
      isCentered
    >
      <ModalOverlay />
      <ModalContent rounded="xl" shadow="xl">
        <ModalHeader fontWeight="bold" fontSize="2xl" textAlign="center">
          Lower Body Exercises
        </ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <VStack spacing={4} w="full" pb={4}>
            {LowerBodyExercises.map((exer, index) => (
              <Box
                key={exer.exerciseID}
                w="full"
                p={4}
                rounded="lg"
                shadow="sm"
                bg={index % 2 === 0 ? "pink.100" : "teal.100"} // alternating pastel
                cursor="pointer"
                _hover={{
                  bg: index % 2 === 0 ? "pink.200" : "teal.200",
                  transform: "scale(1.02)",
                }}
                transition="all 0.2s"
                onClick={() => handleClick(exer)}
              >
                <Text textAlign="center" fontWeight="medium" fontSize="lg">
                  {exer.exerciseName}
                </Text>
              </Box>
            ))}
          </VStack>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
};

export default LowerBodyExercises;
