import { Text } from "@chakra-ui/react";
import React, { useEffect } from "react";
import ExerciseSummaryCard from "./ExerciseSummaryCard";
import { useActivityStore } from "../store/activity";
import { useParams } from "react-router-dom";
import { useExerciseStore } from "../store/exercise";

const SessionActivitiesDetails = () => {
  const { sessionID } = useParams();
  const { getExerciseName } = useExerciseStore();
  const { sessionActivities, fetchActivityBySession } = useActivityStore();

  useEffect(() => {
    if (sessionID) {
      fetchActivityBySession(sessionID);
    }
  }, [sessionID]);

  return (
    <div>
      <Text fontSize="2xl" fontWeight="bold" mb={4}>
        Activities for Session {sessionID}
      </Text>

      {sessionActivities.length > 0 ? (
        sessionActivities.map((activity, index) => {
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
    </div>
  );
};

export default SessionActivitiesDetails;
