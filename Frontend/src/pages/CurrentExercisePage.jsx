import { Button, Text } from "@chakra-ui/react";
import React from "react";

const CurrentExercisePage = ({ exerciseID, exerciseName }) => {
  return (
    <div>
      <Text fontSize={"3xl"} fontWeight={"bold"} as={"u"}>
        {exerciseName}
      </Text>

      <Button>Begin Exercise</Button>
    </div>
  );
};

export default CurrentExercisePage;
