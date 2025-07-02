import { Box, Button } from "@chakra-ui/react";
import "./App.css";
import ExerciseListComponent from "./components/ExerciseListComponent";
import Home from "./pages/Home";
import { Route, Routes } from "react-router-dom";
import ExerciseListPage from "./pages/ExerciseListPage";

function App() {
  return (
    <Box>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/exerciseList" element={<ExerciseListPage />} />
      </Routes>
    </Box>
  );
}

export default App;
