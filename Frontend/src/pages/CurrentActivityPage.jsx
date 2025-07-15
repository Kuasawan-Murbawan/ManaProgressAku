import {
  Button,
  VStack,
  Text,
  Box,
  NumberInput,
  NumberInputField,
  HStack,
  useToast,
} from "@chakra-ui/react";
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import SetComponent from "../components/SetComponent";
import { useActivityStore } from "../store/activity";
import { useSessionStore } from "../store/session";

const CurrentActivityPage = () => {
  const location = useLocation();
  const toast = useToast();
  const navigate = useNavigate();
  const { addActivity } = useActivityStore();
  const { sessionID } = useSessionStore();

  const { exercise } = location.state || {};

  const [started, setStarted] = useState(false);

  const [sets, setSets] = useState([]);

  const isAllFieldsFilled = () => {
    return sets.every((set) => set.weight !== "" && set.reps !== "");
  };

  const handleBegin = () => {
    setStarted(true);
    setSets([{ weight: "", reps: "" }]);
  };

  const handleAddSet = () => {
    if (!isAllFieldsFilled()) {
      toast({
        title: "Incomplete Set",
        description:
          "Please fill in weight and reps before adding another set.",
        status: "warning",
        duration: 3000,
        isClosable: true,
      });
      return;
    }

    setSets([...sets, { weight: "", reps: "" }]);
  };

  const handleSave = async () => {
    if (!isAllFieldsFilled()) {
      toast({
        title: "Incomplete Set",
        description:
          "Please fill in weight and reps before finishing the exercise.",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
      return;
    }

    // Proceed to save all sets (API call here)
    const activityPayLoad = {
      sessionID: sessionID,
      exerciseID: exercise.exerciseID,
      sets: sets.length,
      rep: sets.map((s) => s.reps).join(","),
      weight: sets.map((s) => s.weight).join(","),
    };

    const result = await addActivity(activityPayLoad);

    if (result.success) {
      toast({
        title: "Success!",
        description: result.message,
        status: "success",
        duration: 3000,
        isClosable: true,
      });
      console.log("navigating to create session..");
      navigate("/createSession");
    } else {
      toast({
        title: "Failed",
        description: result.message,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  const handleChange = (index, field, value) => {
    const newSets = [...sets];
    newSets[index][field] = value;
    setSets(newSets);
  };

  return (
    <div>
      <VStack>
        <Box>
          <Text fontSize="2xl" fontWeight="bold">
            {exercise?.exerciseName || "Unknown Exercise"}
          </Text>
        </Box>

        {!started && (
          <Button colorScheme="blue" onClick={handleBegin}>
            Begin
          </Button>
        )}

        {started &&
          sets.map((set, index) => (
            <SetComponent
              key={index}
              currentNumber={index + 1}
              weight={set.weight}
              reps={set.reps}
              onChange={(field, value) => handleChange(index, field, value)}
            />
          ))}

        {started && (
          <>
            <Button
              colorScheme="teal"
              onClick={handleAddSet}
              isDisabled={!isAllFieldsFilled()}
            >
              + Add Set
            </Button>

            <Button
              colorScheme="green"
              onClick={handleSave}
              isDisabled={sets.length === 0 || !isAllFieldsFilled()}
            >
              Finish Exercise
            </Button>
          </>
        )}
      </VStack>
    </div>
  );
};

export default CurrentActivityPage;
