// import { StrictMode } from "react";
// import { createRoot } from "react-dom/client";
// import "./index.css";
// import "bootstrap/dist/css/bootstrap.min.css";
// import App from "./App.jsx";
// import { ChakraProvider } from "@chakra-ui/react";
// import { BrowserRouter } from "react-router-dom";

// createRoot(document.getElementById("root")).render(
//   <StrictMode>
//     <BrowserRouter>
//       <ChakraProvider>
//         <App />
//       </ChakraProvider>
//     </BrowserRouter>
//   </StrictMode>
// );

import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { ChakraProvider } from "@chakra-ui/react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import App from "./App";
import Home from "./pages/Home";
import ExerciseListPage from "./pages/ExerciseListPage";
import NewSessionHomePage from "./pages/NewSessionHomePage";
import NewExercisePage from "./pages/NewExercisePage";
import CurrentExercisePage from "./pages/CurrentExercisePage";
import CurrentActivityPage from "./pages/CurrentActivityPage";
import PastSessionsPage from "./pages/PastSessionsPage";
import SessionActivitiesDetails from "./components/SessionActivitiesDetails";

const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <ChakraProvider>
        <App />
      </ChakraProvider>
    ),
    children: [
      { index: true, element: <Home /> },
      { path: "exerciseList", element: <ExerciseListPage /> },
      { path: "createSession", element: <NewSessionHomePage /> },
      { path: "newExercise", element: <NewExercisePage /> },
      { path: "currentExercisePage", element: <CurrentExercisePage /> },
      { path: "currentActivity", element: <CurrentActivityPage /> },
      { path: "pastSessions", element: <PastSessionsPage /> },
      { path: "session/:sessionID", element: <SessionActivitiesDetails /> },
    ],
  },
]);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);
