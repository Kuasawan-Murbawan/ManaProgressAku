import React from "react";
import ExerciseListComponent from "../components/ExerciseListComponent";
import { Button } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";

const ExerciseListPage = () => {
  const navigate = useNavigate();

  return (
    <div>
      Exercise List Page
      <ExerciseListComponent />
      <Button onClick={() => navigate("/")}>Home</Button>
    </div>
  );
};

export default ExerciseListPage;
