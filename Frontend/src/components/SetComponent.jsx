import {
  Box,
  Button,
  HStack,
  NumberInput,
  NumberInputField,
  Text,
  VStack,
} from "@chakra-ui/react";
import React from "react";

const SetComponent = ({ currentNumber, weight, reps, onChange }) => {
  return (
    <Box bg={"blue.200"} p={6} borderRadius={10} w="80%">
      <VStack spacing={4}>
        <Text fontWeight="bold">Set {currentNumber}</Text>

        <HStack w="100%">
          <Text w="70px">Weight:</Text>
          <NumberInput
            value={weight}
            onChange={(val) => onChange("weight", val)}
          >
            <NumberInputField />
          </NumberInput>
          <Text>KG</Text>
        </HStack>

        <HStack w="100%">
          <Text w="70px">Reps:</Text>
          <NumberInput value={reps} onChange={(val) => onChange("reps", val)}>
            <NumberInputField />
          </NumberInput>
        </HStack>
      </VStack>
    </Box>
  );
};

export default SetComponent;
