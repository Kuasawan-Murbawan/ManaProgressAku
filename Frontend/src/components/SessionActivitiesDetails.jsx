import { Button, Text, useDisclosure } from "@chakra-ui/react";
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
  }, [sessionID]);

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
      <Text fontSize="2xl" fontWeight="bold" mb={4}>
        Activities for Session {sessionID}
      </Text>
      <Button bg={"gray.500"} onClick={handleBackClick}>
        Back
      </Button>
      <Button
        ml={"10"}
        bg={"red"}
        textColor={"white"}
        onClick={deleteSessionOnOpen}
      >
        Delete Session
      </Button>

      {activities.length > 0 ? (
        activities.map((activity, index) => {
          const reps = activity.rep.split(",");
          const weights = activity.weight.split(",");
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
