import {
  Box,
  HStack,
  NumberInput,
  NumberInputField,
  Text,
  VStack,
} from "@chakra-ui/react";
import React from "react";

const SetComponent = ({ currentNumber, weight, reps, onChange }) => {
  return (
    <Box bg={"purple.100"} p={6} borderRadius="2xl" boxShadow="md">
      <VStack spacing={6}>
        <Text fontWeight="bold" fontSize="xl">
          Set {currentNumber}
        </Text>

        <HStack w="100%" justify="space-between">
          <Text w="70px">Weight:</Text>
          <NumberInput
            value={weight}
            min={0}
            max={500}
            onChange={(valStr, valNum) => onChange("weight", valNum)}
            w="full"
          >
            <NumberInputField />
          </NumberInput>
          <Text>KG</Text>
        </HStack>

        <HStack w="100%" justify="space-between">
          <Text w="70px">Reps:</Text>
          <NumberInput
            value={reps}
            min={1}
            max={100}
            onChange={(valStr, valNum) => onChange("reps", valNum)}
            w="full"
          >
            <NumberInputField />
          </NumberInput>
        </HStack>
      </VStack>
    </Box>
  );
};

export default SetComponent;
