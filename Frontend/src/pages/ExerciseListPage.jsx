import React from "react";
import ExerciseListComponent from "../components/ExerciseListComponent";
import { Button } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";

const ExerciseListPage = () => {
  const navigate = useNavigate();

  return (
    <div>
      <Button textColor={"white"} bg={"gray.500"} onClick={() => navigate("/")}>
        Home
      </Button>

      <ExerciseListComponent />
    </div>
  );
};

export default ExerciseListPage;
