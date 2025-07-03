import { Container, VStack, Box, Text, useDisclosure } from "@chakra-ui/react";
import React from "react";
import ArmExercises from "../components/ArmExercises";

const NewExercisePage = () => {
  const {
    isOpen: isOpenArm,
    onOpen: onOpenArm,
    onClose: onCloseArm,
  } = useDisclosure();

  const handleArm = () => {
    onOpenArm();
  };

  return (
    <div>
      <Container>
        <VStack>
          <Box bg={"red.100"} onClick={handleArm} cursor="pointer">
            <Text fontSize={"2xl"}>Arm</Text>
          </Box>
          <Box bg={"blue.100"}>
            <Text fontSize={"2xl"}>Body</Text>
          </Box>
          <Box bg={"yellow.100"}>
            <Text fontSize={"2xl"}>Leg</Text>
          </Box>
        </VStack>
      </Container>

      <ArmExercises isOpenArm={isOpenArm} onCloseArm={onCloseArm} />
    </div>
  );
};

export default NewExercisePage;
