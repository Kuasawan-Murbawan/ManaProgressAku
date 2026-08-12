import {
	Box,
	Button,
	Text,
	useDisclosure,
	VStack,
	useToast,
	Flex,
	Divider,
} from "@chakra-ui/react";
import { React, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DeleteSessionDialog from "../components/Session/DeleteSessionDialog";
import { useActivityStore } from "../store/activity";
import { useExerciseStore } from "../store/exercise";
import ExerciseSummaryCard from "../components/Activity/ExerciseSummaryCard";
import { useSessionStore } from "../store/session";
import useNavigationBlocker from "../hook/useNavigationBlocker.js";
import { useUserStore } from "../store/user.js";
import { AddIcon, CheckIcon, DeleteIcon } from "@chakra-ui/icons";

const NewSessionHomePage = () => {
	const navigate = useNavigate();

	const { activities, clearActivities } = useActivityStore();
	const { exercise, fetchAllExercises } = useExerciseStore();
	const {
		sessionID,
		clearSession,
		finishSession,
		currentSessionDetails,
		fetchSessionDetails,
		isLoading,
	} = useSessionStore();
	const { currentUser, fetchCurrentUser } = useUserStore();

	const [isBlocking, setIsBlocking] = useState(true);
	useNavigationBlocker(isBlocking);

	useEffect(() => {
		fetchCurrentUser();

		if (sessionID) {
			fetchSessionDetails(sessionID);
		}
	}, [fetchCurrentUser, sessionID]);

	const toast = useToast();

	const {
		isOpen: deleteSessionIsOpen,
		onOpen: deleteSessionOnOpen,
		onClose: deleteSessionOnClose,
	} = useDisclosure();

	const getExerciseName = (id) => {
		const found = exercise.find((e) => e.exerciseID === id);
		return found ? found.exerciseName : "Unknown Exercise : " + id;
	};

	const handleAddActivity = () => {
		setIsBlocking(false);
		fetchAllExercises();
		setTimeout(() => navigate("/newExercise"), 100);
	};

	const handleFinishSession = () => {
		finishSession();
		setIsBlocking(false);
		clearActivities();
		clearSession();

		toast({
			title: "Session finished 🎉",
			description: "Great job completing your workout!",
			status: "success",
			duration: 3000,
			isClosable: true,
		});

		navigate("/");
	};

	const handleDeleteSession = () => {
		setIsBlocking(false);
		deleteSessionOnOpen();
	};

	return (
		<Box
			minH="100vh"
			bg="mist.400"
			py={{ base: 6, md: 10 }}
			px={{ base: 4, md: 6 }}
		>
			<Box w="100%" maxW="700px" mx="auto">
				{/* Header */}
				<Box mb={8}>
					<Text
						fontFamily="heading"
						fontSize={{ base: "2xl", md: "3xl" }}
						fontWeight="700"
						color="tiber.800"
					>
						Hello{currentUser ? `, ${currentUser}` : " there"}
					</Text>
					<Text fontSize="md" color="tiber.700" opacity={0.7} mt={1}>
						What do you want to do today?
					</Text>
				</Box>

				{/* Exercises List */}
				{activities?.length > 0 ? (
					<VStack spacing={4} align="stretch" mb={2}>
						{activities.map((activity, index) => (
							<ExerciseSummaryCard
								key={index}
								activity={activity}
								exerciseName={getExerciseName(activity.exerciseID)}
							/>
						))}
					</VStack>
				) : (
					<Box
						bg="white"
						borderRadius="xl"
						boxShadow="sm"
						borderLeft="5px solid"
						borderLeftColor="tiber.600"
						px={6}
						py={8}
						textAlign="center"
						mb={2}
					>
						<Text color="tiber.800" fontWeight="600" mb={1}>
							No activities added yet.
						</Text>
						<Text color="tiber.700" opacity={0.7} fontSize="sm">
							Add an exercise to start logging this session.
						</Text>
					</Box>
				)}

				<Divider my={8} borderColor="mist.300" />

				{/* Buttons */}
				<Flex justify="center" gap={3} wrap="wrap">
					<Button
						onClick={handleAddActivity}
						bg="tiber.800"
						color="white"
						borderRadius="xl"
						px={6}
						leftIcon={<AddIcon boxSize={3} />}
						_hover={{
							bg: "tiber.900",
							transform: "translateY(-2px)",
							boxShadow: "md",
						}}
					>
						Add Exercise
					</Button>
					<Button
						onClick={handleFinishSession}
						bg="lime.400"
						color="tiber.900"
						borderRadius="xl"
						px={6}
						leftIcon={<CheckIcon boxSize={3} />}
						_hover={{
							bg: "lime.300",
							transform: "translateY(-2px)",
							boxShadow: "md",
						}}
					>
						Finish Session
					</Button>
					<Button
						onClick={handleDeleteSession}
						variant="outline"
						borderColor="red.300"
						color="red.500"
						borderRadius="xl"
						px={6}
						leftIcon={<DeleteIcon boxSize={3} />}
						_hover={{ bg: "red.50", transform: "translateY(-2px)" }}
					>
						Delete Session
					</Button>
				</Flex>
			</Box>

			{/* Delete dialog */}
			<DeleteSessionDialog
				isOpen={deleteSessionIsOpen}
				onClose={deleteSessionOnClose}
				sessionID={sessionID}
			/>
		</Box>
	);
};

export default NewSessionHomePage;
