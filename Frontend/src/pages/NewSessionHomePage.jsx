import {
  Box,
  Button,
  Text,
  useDisclosure,
  VStack,
  Table,
  Thead,
  Tbody,
  Tr,
  Th,
  Td,
  TableContainer,
  useToast,
} from "@chakra-ui/react";
import React from "react";
import { useNavigate } from "react-router-dom";
import DeleteSessionDialog from "../components/DeleteSessionDialog";
import { useActivityStore } from "../store/activity";
import { useExerciseStore } from "../store/exercise";

import ExerciseSummaryCard from "../components/ExerciseSummaryCard";
import { useSessionStore } from "../store/session";

const NewSessionHomePage = () => {
  const navigate = useNavigate();

  const { activities, clearActivities } = useActivityStore();
  const { exercise } = useExerciseStore();
  const { sessionID, clearSession } = useSessionStore();

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

  const handleFinishSession = async () => {
    clearActivities();
    clearSession();

    console.log("Session " + sessionID + " has finished.");

    toast({
      title: "Session finished",
      status: "success",
      duration: 3000,
      isClosable: true,
    });

    navigate("/");
  };

  return (
    <div>
      <Text fontSize={"4xl"} fontWeight={"bold"}>
        Hello Din
      </Text>
      <Text>What do you want to do</Text>

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
        <Text fontStyle="italic" mb={6}>
          No activities added yet.
        </Text>
      )}

      <VStack>
        <Button onClick={() => navigate("/newExercise")}>Add Exercise</Button>
        <Button onClick={handleFinishSession}>Finish Session</Button>
        <Button onClick={deleteSessionOnOpen}>Delete Session</Button>
      </VStack>

      <DeleteSessionDialog
        isOpen={deleteSessionIsOpen}
        onClose={deleteSessionOnClose}
        sessionID={sessionID}
      />
    </div>
  );
};

export default NewSessionHomePage;
