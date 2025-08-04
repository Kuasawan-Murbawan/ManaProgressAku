import { Box, Button } from "@chakra-ui/react";
import "./App.css";
import ExerciseListComponent from "./components/ExerciseListComponent";
import Home from "./pages/Home";
import { Route, Routes } from "react-router-dom";
import ExerciseListPage from "./pages/ExerciseListPage";
import NewSessionHomePage from "./pages/NewSessionHomePage";
import NewExercisePage from "./pages/NewExercisePage";
import CurrentExercisePage from "./pages/CurrentExercisePage";
import CurrentActivityPage from "./pages/CurrentActivityPage";
import PastSessionsPage from "./pages/PastSessionsPage";

function App() {
  return (
    <Box>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/exerciseList" element={<ExerciseListPage />} />
        <Route path="/createSession" element={<NewSessionHomePage />} />
        <Route path="/newExercise" element={<NewExercisePage />} />
        <Route path="/currentExercisePage" element={<CurrentExercisePage />} />
        <Route path="/currentActivity" element={<CurrentActivityPage />} />
        <Route path="/pastSessions" element={<PastSessionsPage />} />
      </Routes>
    </Box>
  );
}

export default App;
