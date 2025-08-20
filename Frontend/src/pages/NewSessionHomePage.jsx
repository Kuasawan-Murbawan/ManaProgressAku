import {
  Box,
  Button,
  Text,
  useDisclosure,
  VStack,
  useToast,
  Flex,
  Divider,
} from "@chakra-ui/react";
import { React, useState } from "react";
import { useNavigate } from "react-router-dom";
import DeleteSessionDialog from "../components/DeleteSessionDialog";
import { useActivityStore } from "../store/activity";
import { useExerciseStore } from "../store/exercise";
import ExerciseSummaryCard from "../components/ExerciseSummaryCard";
import { useSessionStore } from "../store/session";
import useNavigationBlocker from "../hook/useNavigationBlocker.js";

const NewSessionHomePage = () => {
  const navigate = useNavigate();

  const { activities, clearActivities } = useActivityStore();
  const { exercise } = useExerciseStore();
  const { sessionID, clearSession } = useSessionStore();

  const [isBlocking, setIsBlocking] = useState(true);
  useNavigationBlocker(isBlocking);

  const toast = useToast();

  const {
    isOpen: deleteSessionIsOpen,
    onOpen: deleteSessionOnOpen,
    onClose: deleteSessionOnClose,
  } = useDisclosure();

  const getExerciseName = (id) => {
    const found = exercise.find((e) => e.exerciseID === id);
    return found ? found.exerciseName : "Unknown Exercise : " + id;
  };

  const handleAddActivity = () => {
    setIsBlocking(false);
    setTimeout(() => navigate("/newExercise"), 100);
  };

  const handleFinishSession = () => {
    setIsBlocking(false);
    clearActivities();
    clearSession();

    console.log("Session " + sessionID + " has finished.");

    toast({
      title: "Session finished 🎉",
      description: "Great job completing your workout!",
      status: "success",
      duration: 3000,
      isClosable: true,
    });

    navigate("/");
  };

  const handleDeleteSession = () => {
    setIsBlocking(false);
    deleteSessionOnOpen();
  };

  return (
    <Box
      minH="100vh"
      bgGradient="linear(to-br, teal.50, purple.50)"
      py={8}
      px={6}
    >
      {/* Header */}
      <Box textAlign="center" mb={8}>
        <Text fontSize="4xl" fontWeight="bold" color="purple.700">
          Hello Din 👋
        </Text>
        <Text fontSize="lg" color="gray.600">
          What do you want to do today?
        </Text>
      </Box>

      {/* Exercises List */}
      {activities.length > 0 ? (
        activities.map((activity, index) => (
          <ExerciseSummaryCard
            key={index}
            exerciseName={getExerciseName(activity.exerciseID)}
            weights={activity.weight.split(",")}
            reps={activity.rep.split(",")}
          />
        ))
      ) : (
        <Text fontStyle="italic" color="gray.500" mb={6} textAlign="center">
          No activities added yet.
        </Text>
      )}

      <Divider my={6} />

      {/* Buttons */}
      <Flex justify="center" gap={4} wrap="wrap">
        <Button
          onClick={handleAddActivity}
          colorScheme="teal"
          variant="solid"
          borderRadius="xl"
          px={6}
          py={4}
        >
          ➕ Add Exercise
        </Button>
        <Button
          onClick={handleFinishSession}
          colorScheme="purple"
          variant="solid"
          borderRadius="xl"
          px={6}
          py={4}
        >
          ✅ Finish Session
        </Button>
        <Button
          onClick={handleDeleteSession}
          colorScheme="red"
          variant="outline"
          borderRadius="xl"
          px={6}
          py={4}
        >
          🗑️ Delete Session
        </Button>
      </Flex>

      {/* Delete dialog */}
      <DeleteSessionDialog
        isOpen={deleteSessionIsOpen}
        onClose={deleteSessionOnClose}
        sessionID={sessionID}
      />
    </Box>
  );
};

export default NewSessionHomePage;
