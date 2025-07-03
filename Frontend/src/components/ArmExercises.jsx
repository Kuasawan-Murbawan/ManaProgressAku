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
    { exerciseID: 6, exerciseName: "Bench Press" },
    { exerciseID: 7, exerciseName: "Preacher Curl" },
    { exerciseID: 8, exerciseName: "Hammer Curl" },
    { exerciseID: 9, exerciseName: "Push Up" },
  ];

  return (
    <Modal isOpen={isOpenArm} onClose={onCloseArm}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Arm Exercises</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <VStack spacing={5} align="start">
            {dummyData.map((dummy) => (
              <Box key={dummy.exerciseID} h={"12"} bg={"yellow.300"} w={"full"}>
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
