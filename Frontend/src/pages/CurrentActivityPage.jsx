import { Button, VStack, Text, Box, useToast, Spinner } from "@chakra-ui/react";
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
  const [isSaving, setIsSaving] = useState(false);

  // Validation: weight + reps must be filled
  const isAllFieldsFilled = () =>
    sets.every((set) => set.weight !== "" && set.reps !== "");

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
        description: "Please fill in weight and reps before finishing.",
        status: "error",
        duration: 3000,
        isClosable: true,
      });
      return;
    }

    setIsSaving(true);

    const activityPayLoad = {
      sessionID,
      exerciseID: exercise.exerciseID,
      sets: sets.length,
      rep: sets.map((s) => s.reps).join(","),
      weight: sets.map((s) => s.weight).join(","),
    };

    const result = await addActivity(activityPayLoad);
    setIsSaving(false);

    if (result.success) {
      toast({
        title: "Success!",
        description: result.message,
        status: "success",
        duration: 3000,
        isClosable: true,
      });
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

  if (!exercise) {
    navigate("/createSession"); // safeguard
    return null;
  }

  return (
    <Box
      maxW="600px"
      mx="auto"
      mt={10}
      p={6}
      rounded="xl"
      shadow="md"
      bg="blue.50"
    >
      <VStack spacing={6} align="stretch">
        {/* Exercise Header */}
        <Box textAlign="center">
          <Text fontSize="3xl" fontWeight="bold" color="blue.800">
            {exercise.exerciseName}
          </Text>
        </Box>

        {/* Before starting */}
        {!started && (
          <Button colorScheme="blue" size="lg" onClick={handleBegin}>
            Begin Exercise
          </Button>
        )}

        {/* After starting */}
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
          <VStack spacing={4}>
            <Button
              colorScheme="teal"
              onClick={handleAddSet}
              isDisabled={!isAllFieldsFilled()}
              w="full"
            >
              + Add Set
            </Button>

            <Button
              colorScheme="green"
              onClick={handleSave}
              isDisabled={sets.length === 0 || !isAllFieldsFilled()}
              isLoading={isSaving}
              w="full"
            >
              Finish Exercise
            </Button>
          </VStack>
        )}
      </VStack>
    </Box>
  );
};

export default CurrentActivityPage;
