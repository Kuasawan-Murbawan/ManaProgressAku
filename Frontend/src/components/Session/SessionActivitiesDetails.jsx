import {
	Button,
	Text,
	useDisclosure,
	HStack,
	Box,
	Spinner,
	Center,
	VStack,
} from "@chakra-ui/react";
import React, { useEffect } from "react";
import ExerciseSummaryCard from "../Activity/ExerciseSummaryCard";
import { useActivityStore } from "../../store/activity";
import { useNavigate, useParams } from "react-router-dom";
import { useExerciseStore } from "../../store/exercise";
import DeleteSessionDialog from "./DeleteSessionDialog";
import { useSessionStore } from "../../store/session";

const SessionActivitiesDetails = () => {
	const { sessionID } = useParams();
	const {
		fetchSessionDetails,
		currentSessionDetails,
		isLoading,
		clearCurrentSessionDetails,
	} = useSessionStore();
	const { fetchAllExercises, getExerciseName } = useExerciseStore();
	const { activities, clearActivities } = useActivityStore();

	const navigate = useNavigate();

	useEffect(() => {
		if (sessionID) {
			fetchSessionDetails(sessionID);
		}
		fetchAllExercises();
	}, [sessionID, fetchSessionDetails, fetchAllExercises]);

	const handleBackClick = () => {
		clearCurrentSessionDetails();
		navigate("/pastSessions");
	};

	const {
		isOpen: deleteSessionIsOpen,
		onOpen: deleteSessionOnOpen,
		onClose: deleteSessionOnClose,
	} = useDisclosure();

	return (
		<Box
			minH="100vh"
			bg="mist.400"
			py={{ base: 6, md: 10 }}
			px={{ base: 4, md: 6 }}
		>
			<Box w="100%" maxW="900px" mx="auto">
				{/* Header */}
				<Box
					bg="tiber.800"
					color="white"
					borderRadius="2xl"
					p={{ base: 5, md: 6 }}
					mb={6}
					boxShadow="md"
				>
					<Text fontSize={{ base: "xl", md: "2xl" }} fontWeight="700" mb={5}>
						Activities for Session {sessionID}
					</Text>

					<HStack spacing={3} flexWrap="wrap">
						<Button
							bg="white"
							color="tiber.800"
							_hover={{
								bg: "mist.50",
								transform: "translateY(-1px)",
							}}
							borderRadius="lg"
							px={6}
							onClick={handleBackClick}
						>
							← Back
						</Button>

						<Button
							bg="red.500"
							color="white"
							_hover={{
								bg: "red.600",
								transform: "translateY(-1px)",
							}}
							borderRadius="lg"
							px={6}
							onClick={deleteSessionOnOpen}
						>
							Delete Session
						</Button>
					</HStack>
				</Box>

				{/* Activities */}
				{isLoading ? (
					<Center py={10}>
						<VStack spacing={3}>
							<Spinner size="xl" thickness="4px" color="tiber.600" />
							<Text color="tiber.800" opacity={0.6}>
								Loading Activities...
							</Text>
						</VStack>
					</Center>
				) : activities?.length > 0 ? (
					<VStack spacing={5} align="stretch" mx={"auto"} maxW={"600px"}>
						{activities.map((activity) => (
							<ExerciseSummaryCard
								key={activity.activityID}
								activity={activity}
								exerciseName={getExerciseName(activity.exerciseID)}
							/>
						))}
					</VStack>
				) : (
					<Box
						bg="white"
						borderRadius="xl"
						p={8}
						textAlign="center"
						borderLeft="5px solid"
						borderLeftColor="lime.400"
						boxShadow="sm"
					>
						<Text fontWeight="600" color="tiber.800">
							No activities added yet.
						</Text>
					</Box>
				)}
			</Box>

			<DeleteSessionDialog
				isOpen={deleteSessionIsOpen}
				onClose={deleteSessionOnClose}
				sessionID={sessionID}
			/>
		</Box>
	);
};

export default SessionActivitiesDetails;
