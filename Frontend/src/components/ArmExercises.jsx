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
import React from "react";
import { useNavigate } from "react-router-dom";

const ArmExercises = ({ isOpenArm, onCloseArm }) => {
  /*
        TODO: We use dummy data bc now, the exercise only include id, name and info
            - but we need to include exercise type to differentiate between arm, leg,
            - so new data would be:
                {   
                    exerciseID: 1
                    exerciseType: "arm"
                    exerciseName: "Bench Press"    
                    generalInfo: "Bench press ialah"
                }
    */

  const dummyData = [
    { exerciseID: 1, exerciseName: "Bicep Curl" },
    { exerciseID: 2, exerciseName: "Push Up" },
    { exerciseID: 3, exerciseName: "Bench Press" },
  ];

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
            {dummyData.map((dummy) => (
              <Box
                key={dummy.exerciseID}
                h={"12"}
                bg={"yellow.300"}
                w={"full"}
                onClick={handleClick}
              >
                <Text textAlign={"center"}>
                  {dummy.exerciseID}. {dummy.exerciseName}
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
