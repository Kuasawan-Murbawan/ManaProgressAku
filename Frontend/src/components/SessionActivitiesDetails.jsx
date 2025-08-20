import { Button, Text, useDisclosure, HStack, Box } from "@chakra-ui/react";
import React, { useEffect } from "react";
import ExerciseSummaryCard from "./ExerciseSummaryCard";
import { useActivityStore } from "../store/activity";
import { useNavigate, useParams } from "react-router-dom";
import { useExerciseStore } from "../store/exercise";
import DeleteSessionDialog from "./DeleteSessionDialog";

const SessionActivitiesDetails = () => {
  const { sessionID } = useParams();
  const { getExerciseName } = useExerciseStore();
  const { activities, fetchActivityBySession, clearActivities } =
    useActivityStore();
  const navigate = useNavigate();

  useEffect(() => {
    if (sessionID) {
      fetchActivityBySession(sessionID);
    }
    return () => clearActivities(); // cleanup on unmount
  }, [sessionID, fetchActivityBySession, clearActivities]);

  const handleBackClick = () => {
    clearActivities();
    navigate("/pastSessions");
  };

  const {
    isOpen: deleteSessionIsOpen,
    onOpen: deleteSessionOnOpen,
    onClose: deleteSessionOnClose,
  } = useDisclosure();

  return (
    <div>
      <Box bg="pink.50" borderRadius="2xl" p={6} mb={6} boxShadow="md">
        <Text fontSize="2xl" fontWeight="bold" mb={4} color="pink.600">
          Activities for Session {sessionID}
        </Text>

        <HStack spacing={4}>
          <Button
            bg="blue.100"
            _hover={{ bg: "blue.200" }}
            color="blue.800"
            borderRadius="xl"
            px={6}
            onClick={handleBackClick}
          >
            Back
          </Button>
          <Button
            bg="red.100"
            _hover={{ bg: "red.200" }}
            color="red.800"
            borderRadius="xl"
            px={6}
            onClick={deleteSessionOnOpen}
          >
            Delete Session
          </Button>
        </HStack>
      </Box>

      {activities.length > 0 ? (
        activities.map((activity, index) => {
          const reps = activity.rep ? activity.rep.split(",") : [];
          const weights = activity.weight ? activity.weight.split(",") : [];
          const minLength = Math.min(reps.length, weights.length);

          return (
            <ExerciseSummaryCard
              key={index}
              exerciseName={getExerciseName(activity.exerciseID)}
              weights={weights.slice(0, minLength)}
              reps={reps.slice(0, minLength)}
            />
          );
        })
      ) : (
        <Text fontStyle="italic" mb={6}>
          No activities added yet.
        </Text>
      )}

      <DeleteSessionDialog
        isOpen={deleteSessionIsOpen}
        onClose={deleteSessionOnClose}
        sessionID={sessionID}
      />
    </div>
  );
};

export default SessionActivitiesDetails;
