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

  const {
    isOpen: isOpenLowerBody,
    onOpen: onOpenLowerBody,
    onClose: onCloseLowerBody,
  } = useDisclosure();

  return (
    <Container maxW="md" py={8}>
      <VStack spacing={6} w="100%">
        {/* Upper Body */}
        <Box
          bg="pink.100"
          p={6}
          rounded="xl"
          shadow="md"
          w="100%"
          textAlign="center"
          cursor="pointer"
          onClick={onOpenUpperBody}
          _hover={{ bg: "pink.200", transform: "scale(1.03)" }}
          transition="all 0.2s"
        >
          <Text fontSize="2xl" fontWeight="bold">
            Upper Body
          </Text>
        </Box>

        {/* Lower Body */}
        <Box
          bg="blue.100"
          p={6}
          rounded="xl"
          shadow="md"
          w="100%"
          textAlign="center"
          cursor="pointer"
          onClick={onOpenLowerBody}
          _hover={{ bg: "blue.200", transform: "scale(1.03)" }}
          transition="all 0.2s"
        >
          <Text fontSize="2xl" fontWeight="bold">
            Lower Body
          </Text>
        </Box>

        {/* Coming Soon */}
        <Box
          bg="yellow.100"
          p={6}
          rounded="xl"
          shadow="md"
          w="100%"
          textAlign="center"
          _hover={{ bg: "yellow.200", transform: "scale(1.03)" }}
          transition="all 0.2s"
        >
          <Text fontSize="2xl" fontWeight="bold">
            Coming Soon
          </Text>
        </Box>
      </VStack>

      {/* Modals */}
      <UpperBodyExercises
        isOpenUpperBody={isOpenUpperBody}
        onCloseUpperBody={onCloseUpperBody}
      />
      <LowerBodyExercises
        isOpenLowerBody={isOpenLowerBody}
        onCloseLowerBody={onCloseLowerBody}
      />
    </Container>
  );
};

export default NewExercisePage;
