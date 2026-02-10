// export default App;
import { Box } from "@chakra-ui/react";
import { Outlet } from "react-router-dom";

function App() {
  return (
    <Box>
      <Outlet /> {/* This is where child routes will render */}
    </Box>
  );
}

export default App;
