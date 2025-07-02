import React from "react";
import {
  Button,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Text,
} from "@chakra-ui/react";

const ExerciseDetailModal = ({ isOpen, onClose, currentExercise }) => {
  if (!currentExercise) return null;

  return (
    <div>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>{currentExercise.exerciseName}</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Text>Exercise Image</Text>
            <Text>{currentExercise.generalInfo}</Text>
          </ModalBody>
          <ModalFooter>
            <Button onClick={onClose}>Okay</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </div>
  );
};

export default ExerciseDetailModal;
