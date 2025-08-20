import React from "react";
import ExerciseListComponent from "../components/ExerciseListComponent";
import { Box, Button } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";

const ExerciseListPage = () => {
  const navigate = useNavigate();

  return (
    <div>
      <ExerciseListComponent />

      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        pb={6} // add vertical padding so it doesn’t stick to the top
        bg={"gray.300"}
      >
        <Button
          size="lg"
          bg="pink.200"
          _hover={{ bg: "pink.300", transform: "scale(1.05)" }}
          borderRadius="xl"
          shadow="md"
          onClick={() => navigate("/")}
        >
          ⬅ Home
        </Button>
      </Box>
    </div>
  );
};

export default ExerciseListPage;
