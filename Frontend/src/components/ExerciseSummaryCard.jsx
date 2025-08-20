import {
  Box,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  Text,
  TableContainer,
} from "@chakra-ui/react";

const ExerciseSummaryCard = ({ exerciseName, weights, reps }) => {
  return (
    <Box
      bgGradient="linear(to-r, teal.100, green.100)"
      p={6}
      borderRadius="2xl"
      boxShadow="lg"
      w="90%"
      mx="auto"
      my={8}
      transition="all 0.2s ease-in-out"
      _hover={{ transform: "scale(1.02)", boxShadow: "xl" }}
    >
      <Text
        fontSize="2xl"
        fontWeight="extrabold"
        mb={4}
        textAlign="center"
        color="green.700"
      >
        {exerciseName}
      </Text>

      <TableContainer borderRadius="lg" overflow="hidden" boxShadow="sm">
        <Table variant="striped" colorScheme="green" size="md">
          <Thead bg="green.200">
            <Tr>
              <Th color="green.900" fontWeight="bold" textAlign="center">
                Sets
              </Th>
              {weights.map((_, index) => (
                <Th
                  key={index}
                  color="green.900"
                  fontWeight="bold"
                  textAlign="center"
                >
                  Set {index + 1}
                </Th>
              ))}
            </Tr>
          </Thead>
          <Tbody>
            <Tr>
              <Td fontWeight="semibold" textAlign="center" color="teal.800">
                Weight (kg)
              </Td>
              {weights.map((w, index) => (
                <Td key={index} textAlign="center">
                  {w}
                </Td>
              ))}
            </Tr>
            <Tr>
              <Td fontWeight="semibold" textAlign="center" color="teal.800">
                Reps
              </Td>
              {reps.map((r, index) => (
                <Td key={index} textAlign="center">
                  {r}
                </Td>
              ))}
            </Tr>
          </Tbody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default ExerciseSummaryCard;
