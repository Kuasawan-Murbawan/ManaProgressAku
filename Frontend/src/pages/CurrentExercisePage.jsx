import { Button, Text, VStack, Box } from "@chakra-ui/react";
import React from "react";

const CurrentExercisePage = ({ exerciseID, exerciseName }) => {
  return (
    <Box
      p={6}
      maxW="500px"
      mx="auto"
      mt={10}
      rounded="xl"
      shadow="xl"
      bg="teal.50"
      textAlign="center"
    >
      <VStack spacing={6}>
        <Text fontSize="4xl" fontWeight="bold" color="teal.800" as="u">
          {exerciseName}
        </Text>

        <Text fontSize="lg" color="gray.600">
          Exercise ID: {exerciseID}
        </Text>

        <Button
          size="lg"
          colorScheme="teal"
          w="full"
          rounded="xl"
          _hover={{ transform: "scale(1.05)" }}
        >
          Begin Exercise
        </Button>
      </VStack>
    </Box>
  );
};

export default CurrentExercisePage;
