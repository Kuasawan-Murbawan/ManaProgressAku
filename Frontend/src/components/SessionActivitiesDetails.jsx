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
import ExerciseSummaryCard from "./ExerciseSummaryCard";
import { useActivityStore } from "../store/activity";
import { useNavigate, useParams } from "react-router-dom";
import { useExerciseStore } from "../store/exercise";
import DeleteSessionDialog from "./DeleteSessionDialog";
import { useSessionStore } from "../store/session";

const SessionActivitiesDetails = () => {
	const { sessionID } = useParams();
	const {
		fetchSessionDetails,
		currentSessionDetails,
		isLoading,
		clearCurrentSessionDetails,
	} = useSessionStore();
	const { getExerciseName } = useExerciseStore();
	const navigate = useNavigate();

	useEffect(() => {
		if (sessionID) {
			fetchSessionDetails(sessionID);
		}
	}, [sessionID, fetchSessionDetails]);

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
		<div>
			<Box bg="pink.50" borderRadius="2xl" p={6} mb={6} boxShadow="md">
				<Text fontSize="2xl" fontWeight="bold" mb={4} color="pink.600">
					Activities for Session {sessionID}
				</Text>

				<HStack spacing={4}>
					<Button
						bg="blue.100"
						_hover={{ bg: "blue.200" }}
						color="blue.800"
						borderRadius="xl"
						px={6}
						onClick={handleBackClick}
					>
						Back
					</Button>
					<Button
						bg="red.100"
						_hover={{ bg: "red.200" }}
						color="red.800"
						borderRadius="xl"
						px={6}
						onClick={deleteSessionOnOpen}
					>
						Delete Session
					</Button>
				</HStack>
			</Box>

			{isLoading ? (
				<Center py={10}>
					<VStack>
						<Spinner size="xl" thickness="4px" color="green.500" />
						<Text color={"gray.500"}>Loading Activities...</Text>
					</VStack>
				</Center>
			) : currentSessionDetails?.activities?.length > 0 ? (
				currentSessionDetails.activities.map((activity, index) => {
					return (
						<ExerciseSummaryCard
							key={activity.activityID}
							activityID={activity.activityID}
							exerciseName={getExerciseName(activity.exerciseID)}
							sets={activity.sets}
						/>
					);
				})
			) : (
				<Text fontStyle="italic" mb={6}>
					No activities added yet.
				</Text>
			)}

			<DeleteSessionDialog
				isOpen={deleteSessionIsOpen}
				onClose={deleteSessionOnClose}
				sessionID={sessionID}
			/>
		</div>
	);
};

export default SessionActivitiesDetails;
