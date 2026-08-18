// export default App;
import { Box, useDisclosure } from "@chakra-ui/react";
import { Outlet, useLocation } from "react-router-dom";
import NavBar from "./components/NavBar";
import { useSessionStore } from "./store/session";
import { useEffect } from "react";
import ActiveSessionDialog from "./components/Session/ActiveSessionDialog";

function App() {
	const { activeSession } = useSessionStore();
	const location = useLocation();

	const {
		isOpen: activeSessionIsOpen,
		onOpen: activeSessionOnOpen,
		onClose: activeSessionOnClose,
	} = useDisclosure();

	useEffect(() => {
		// don't re-prompt the user about the session they're already in.
		if (
			location.pathname === "/createSession" ||
			location.pathname === "/currentActivity" ||
			location.pathname === "/newExercise"
		)
			return;

		const checkActiveSession = async () => {
			const hasActive = await activeSession();
			if (hasActive) {
				activeSessionOnOpen();
			}
		};

		checkActiveSession();
	}, [location.pathname]);

	return (
		<Box minH="100vh" pt={{ base: 0, md: "64px" }} pb={{ base: "64px", md: 0 }}>
			<NavBar />
			<Outlet /> {/* this is where child routes will render */}
			<ActiveSessionDialog
				isOpen={activeSessionIsOpen}
				onClose={activeSessionOnClose}
			/>
		</Box>
	);
}

export default App;
