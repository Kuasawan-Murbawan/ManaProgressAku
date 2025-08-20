import { Container, VStack, Box, Text, useDisclosure } from "@chakra-ui/react";
import React from "react";
import UpperBodyExercises from "../components/UpperBodyExercises";
import LowerBodyExercises from "../components/LowerBodyExercises";

const NewExercisePage = () => {
  const {
    isOpen: isOpenUpperBody,
    onOpen: onOpenUpperBody,
    onClose: onCloseUpperBody,
  } = useDisclosure();

  const handleUpperBody = () => {
    onOpenUpperBody();
  };

  const {
    isOpen: isOpenLowerBody,
    onOpen: onOpenLowerBody,
    onClose: onCloseLowerBody,
  } = useDisclosure();

  const handleLowerBody = () => {
    onOpenLowerBody();
  };

  return (
    <div>
      <Container>
        <VStack>
          <Box bg={"red.100"} onClick={handleUpperBody} cursor="pointer">
            <Text fontSize={"2xl"}>UpperBody</Text>
          </Box>
          <Box bg={"blue.100"} onClick={handleLowerBody}>
            <Text fontSize={"2xl"}>Lower Body</Text>
          </Box>
          <Box bg={"yellow.100"}>
            <Text fontSize={"2xl"}>Coming Soon</Text>
          </Box>
        </VStack>
      </Container>

      <UpperBodyExercises
        isOpenUpperBody={isOpenUpperBody}
        onCloseUpperBody={onCloseUpperBody}
      />

      <LowerBodyExercises
        isOpenLowerBody={isOpenLowerBody}
        onCloseLowerBody={onCloseLowerBody}
      />
    </div>
  );
};

export default NewExercisePage;
