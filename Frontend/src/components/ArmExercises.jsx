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

const ArmExercises = ({ isOpenArm, onCloseArm }) => {
  const { exercise, fetchAllExercises } = useExerciseStore();

  useEffect(() => {
    if (exercise.length === 0) {
      fetchAllExercises();
    }
  }, [exercise, fetchAllExercises]);

  function armFilter(exercises) {
    return exercises.filter((item) => item.exerciseType === "1");
  }

  const ArmExercises = armFilter(exercise);

  const navigate = useNavigate();

  const handleClick = (exercise) => {};

  return (
    <Modal isOpen={isOpenArm} onClose={onCloseArm}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Arm Exercises</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <VStack spacing={5} align="start">
            {ArmExercises.map((exer) => (
              <Box
                key={exer.exerciseID}
                h={"12"}
                bg={"yellow.300"}
                w={"full"}
                onClick={handleClick}
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

export default ArmExercises;
