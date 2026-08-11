import {
	Box,
	Button,
	Center,
	Text,
	VStack,
	Spinner,
	Container,
} from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SessionDetailsComponent from "../components/Session/SessionDetailsComponent";
import { useSessionStore } from "../store/session";
import { useExerciseStore } from "../store/exercise";

const PastSessionsPage = () => {
	const navigate = useNavigate();

	const { sessions, fetchUserSessions, isLoading } = useSessionStore();
	const { fetchAllExercises } = useExerciseStore();

	useEffect(() => {
		fetchUserSessions();
		fetchAllExercises();
	}, [fetchUserSessions, fetchAllExercises]);

	// Sort sessions by most recent date/time first
	const sortedSessions = [...sessions].sort((a, b) => {
		const dateA = new Date(`${a.date} ${a.time}`);
		const dateB = new Date(`${b.date} ${b.time}`);
		return dateB - dateA; // descending (latest first)
	});

	return (
		<Box
			minH="100vh"
			bg="mist.400"
			py={{ base: 8, md: 12 }}
			px={{ base: 4, md: 6 }}
		>
			{/* Page Header */}
			<Container maxW="900px">
				<Center mb={{ base: 8, md: 10 }}>
					<VStack spacing={2} textAlign="center">
						<Text
							fontSize={{ base: "3xl", md: "4xl" }}
							fontWeight="extrabold"
							color="tiber.800"
							letterSpacing="-0.02em"
						>
							Past Sessions
						</Text>

						<Text
							fontSize={{ base: "sm", md: "md" }}
							color="tiber.900"
							opacity={0.65}
						>
							Review your previous workout sessions
						</Text>
					</VStack>
				</Center>

				{/* Session List */}
				<VStack spacing={4} align={"stretch"} w="100%" maxW="500px" mx="auto">
					{isLoading ? (
						<Center py={10}>
							<VStack spacing={3}>
								<Spinner size="xl" thickness="4px" color="tiber.600" />
								<Text color="tiber.900" opacity={0.6}>
									Loading Sessions...
								</Text>
							</VStack>
						</Center>
					) : sortedSessions.length > 0 ? (
						sortedSessions.map((session) => (
							<SessionDetailsComponent
								key={session.sessionID}
								sessionID={session.sessionID}
								date={session.date}
								time={session.time}
							/>
						))
					) : (
						<Box
							bg="white"
							borderRadius="xl"
							p={{ base: 8, md: 10 }}
							textAlign="center"
							boxShadow="sm"
							borderLeft="5px solid"
							borderLeftColor="lime.400"
						>
							<Text
								fontSize={{ base: "md", md: "lg" }}
								fontWeight="600"
								color="tiber.800"
							>
								No past sessions yet
							</Text>

							<Text mt={2} fontSize="sm" color="tiber.900" opacity={0.6}>
								Start a workout to begin tracking your progress.
							</Text>
						</Box>
					)}
				</VStack>

				{/* Back Button */}
				<Center mt={{ base: 8, md: 10 }}>
					<Button
						size={{ base: "md", md: "lg" }}
						bg="tiber.800"
						color="white"
						_hover={{
							bg: "tiber.900",
							transform: "translateY(-2px)",
						}}
						_active={{
							transform: "translateY(0)",
						}}
						borderRadius="lg"
						px={8}
						shadow="sm"
						transition="all 0.2s ease"
						onClick={() => navigate("/")}
					>
						← Home
					</Button>
				</Center>
			</Container>
		</Box>
	);
};

export default PastSessionsPage;
