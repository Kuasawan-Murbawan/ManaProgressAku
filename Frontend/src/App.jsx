// export default App;
import { Box } from "@chakra-ui/react";
import { Outlet } from "react-router-dom";
import NavBar from "./components/NavBar";

function App() {
	return (
		<Box minH="100vh" pt={{ base: 0, md: "64px" }} pb={{ base: "64px", md: 0 }}>
			<NavBar />
			<Outlet /> {/* this is where child routes will render */}
		</Box>
	);
}

export default App;
