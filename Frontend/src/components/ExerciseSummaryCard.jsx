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
  IconButton,
  HStack,
  Spacer,
  useDisclosure,
  AlertDialog,
  AlertDialogOverlay,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogBody,
  AlertDialogFooter,
  Button,
} from "@chakra-ui/react";
import { DeleteIcon } from "@chakra-ui/icons";
import { useActivityStore } from "../store/activity";
import { useRef } from "react";

const ExerciseSummaryCard = ({ activityID, exerciseName, weights, reps }) => {
  const { deleteActivity } = useActivityStore();

  const { isOpen, onOpen, onClose } = useDisclosure();
  const cancelRef = useRef();

  const handleDeleteActivity = async () => {
    await deleteActivity(activityID);
    onClose();
  };

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
      <HStack mb={4}>
        <Text fontSize="2xl" fontWeight="extrabold" color="green.700">
          {exerciseName}
        </Text>
        <Spacer />

        <IconButton
          aria-label="Delete exercise"
          icon={<DeleteIcon />}
          colorScheme="red"
          variant="ghost"
          size="lg"
          onClick={onOpen}
        />
      </HStack>

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

      <AlertDialog
        isOpen={isOpen}
        leastDestructiveRef={cancelRef}
        onClose={onClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Delete Activity
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure you want to delete <b>{exerciseName}</b>? This action
              cannot be undone.
            </AlertDialogBody>

            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onClose}>
                Cancel
              </Button>
              <Button colorScheme="red" onClick={handleDeleteActivity} ml={3}>
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </Box>
  );
};

export default ExerciseSummaryCard;
