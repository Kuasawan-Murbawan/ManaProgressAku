import { Button, Text } from "@chakra-ui/react";
import React from "react";
import { useNavigate } from "react-router-dom";

const NewSessionHomePage = () => {
  const navigate = useNavigate();
  return (
    <div>
      <Text fontSize={"4xl"} fontWeight={"bold"}>
        Hello Din
      </Text>
      <Text>What do you want to do</Text>
      <Button onClick={() => navigate("/newExercise")}>Add Exercise</Button>
    </div>
  );
};

export default NewSessionHomePage;
