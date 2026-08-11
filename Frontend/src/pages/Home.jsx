import {
	Box,
	Center,
	Container,
	Text,
	useDisclosure,
	Card,
	VStack,
} from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import StartSessionDialog from "../components/Session/StartSessionDialog";
import { useExerciseStore } from "../store/exercise";
import { useActivityStore } from "../store/activity";
import { useSessionStore } from "../store/session";
import ConfirmLogoutDialog from "../components/ConfirmLogoutDialog";
import ActiveSessionDialog from "../components/Session/ActiveSessionDialog";

const Home = () => {
	const navigate = useNavigate();

	const { exercise, fetchAllExercises } = useExerciseStore();
	const { clearActivities } = useActivityStore();
	const { activeSession } = useSessionStore();

	useEffect(() => {
		const init = async () => {
			clearActivities(); // clear activities after finishing session

			if (exercise.length === 0) {
				await fetchAllExercises();
			}

			// Check for any active session
			const hasActive = await activeSession();

			if (hasActive) {
				activeSessionOnOpen();
			}
		};

		init();
	}, []);

	// Create Session
	const {
		isOpen: confirmSessionIsOpen,
		onOpen: confirmSessionOnOpen,
		onClose: confirmSessionOnClose,
	} = useDisclosure();

	const {
		isOpen: activeSessionIsOpen,
		onOpen: activeSessionOnOpen,
		onClose: activeSessionOnClose,
	} = useDisclosure();

	// Log Out
	const {
		isOpen: confirmLogoutIsOpen,
		onOpen: confirmLogoutOnOpen,
		onClose: confirmLogoutOnClose,
	} = useDisclosure();

	// Reusable Menu Card
	const MenuCard = ({ text, bg, hoverBg, onClick }) => (
		<Box
			w="full"
			maxW="md"
			bg={bg}
			borderRadius="2xl"
			py={6}
			px={4}
			shadow="md"
			cursor="pointer"
			transition="all 0.2s"
			_hover={{
				transform: "scale(1.05)",
				bg: hoverBg,
				shadow: "xl",
			}}
			onClick={onClick}
		>
			<Center>
				<Text fontSize="2xl" fontWeight="bold" color="gray.700">
					{text}
				</Text>
			</Center>
		</Box>
	);

	return (
		<Box minH="100vh" bg="mist.400">
			{/* Header */}
			<Container maxW="900px" py={{ base: 10, md: 16 }}>
				<Center>
					<VStack spacing={3} textAlign="center">
						<Text
							fontSize={{ base: "3xl", sm: "4xl", md: "5xl" }}
							fontWeight="extrabold"
							color="tiber.800"
							letterSpacing="-0.02em"
						>
							🏋️ Mana Progress Aku
						</Text>

						<Text
							fontSize={{ base: "sm", md: "md" }}
							color="tiber.900"
							opacity={0.7}
						>
							Track your workouts. Measure your progress.
						</Text>
					</VStack>
				</Center>
			</Container>

			{/* Menu Options */}
			<Container maxW="600px" pb={12}>
				<VStack spacing={4}>
					<MenuCard
						text="Start New Session"
						bg="lime.300"
						hoverBg="lime.500"
						borderStyle="solid"
						borderWidth="1px"
						borderLeftWidth="4px"
						borderColor="gray.200"
						borderLeftColor="lime.400"
						color="tiber.800"
						onClick={confirmSessionOnOpen}
					/>

					<MenuCard
						text="Past Sessions"
						bg="white"
						hoverBg="white"
						borderLeft="5px solid"
						borderLeftColor="tiber.600"
						color="tiber.800"
						onClick={() => navigate("/pastSessions")}
					/>

					<MenuCard
						text="Exercise Library"
						bg="white"
						hoverBg="white"
						borderLeft="5px solid"
						borderLeftColor="tiber.600"
						color="tiber.800"
						onClick={() => navigate("/exerciseList")}
					/>

					<MenuCard
						text="Log Out"
						bg="#ad213f"
						hoverBg="red.600"
						color="white"
						borderLeft="5px solid"
						borderLeftColor="red.400"
						onClick={confirmLogoutOnOpen}
					/>
				</VStack>
			</Container>

			{/* Start Session Dialog */}
			<StartSessionDialog
				isOpen={confirmSessionIsOpen}
				onClose={confirmSessionOnClose}
			/>

			<ConfirmLogoutDialog
				isOpen={confirmLogoutIsOpen}
				onClose={confirmLogoutOnClose}
			/>

			<ActiveSessionDialog
				isOpen={activeSessionIsOpen}
				onClose={activeSessionOnClose}
			/>
		</Box>
	);
};

export default Home;
