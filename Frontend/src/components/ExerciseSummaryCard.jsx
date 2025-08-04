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
      bg="gray.300"
      p={6}
      borderRadius="xl"
      boxShadow="md"
      w="80%"
      mx="auto"
      my={"7"}
    >
      <Text fontSize="3xl" fontWeight="bold" mb={4}>
        {exerciseName}
      </Text>

      <TableContainer>
        <Table variant="simple" size="sm" border="1px solid black">
          <Thead>
            <Tr>
              <Th border="1px solid black">Sets</Th>
              {weights.map((_, index) => (
                <Th key={index} border="1px solid black">
                  Set {index + 1}
                </Th>
              ))}
            </Tr>
          </Thead>
          <Tbody>
            <Tr>
              <Td border="1px solid black" fontWeight="semibold">
                Weight (kg)
              </Td>
              {weights.map((w, index) => (
                <Td key={index} border="1px solid black">
                  {w}
                </Td>
              ))}
            </Tr>
            <Tr>
              <Td border="1px solid black" fontWeight="semibold">
                Reps
              </Td>
              {reps.map((r, index) => (
                <Td key={index} border="1px solid black">
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
