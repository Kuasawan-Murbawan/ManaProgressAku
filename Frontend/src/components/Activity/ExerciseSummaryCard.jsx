import {
	Box,
	Table,
	Thead,
	Tbody,
	Tr,
	Th,
	Td,
	Text,
	TableContainer,
	IconButton,
	HStack,
	Spacer,
	useDisclosure,
	AlertDialog,
	AlertDialogOverlay,
	AlertDialogContent,
	AlertDialogHeader,
	AlertDialogBody,
	AlertDialogFooter,
	Button,
} from "@chakra-ui/react";
import { DeleteIcon, EditIcon } from "@chakra-ui/icons";
import { useActivityStore } from "../../store/activity";
import { useRef } from "react";
import { useSessionStore } from "../../store/session";
import EditActivityModal from "./EditActivityModal";

const ExerciseSummaryCard = ({ exerciseName, activity }) => {
	const { deleteActivity, setActivities, activities } = useActivityStore();
	const { fetchSessionDetails, sessionID } = useSessionStore();

	const { activityID, sets = [] } = activity;
	const {
		isOpen: deleteActivityIsOpen,
		onOpen: deleteActivityOnOpen,
		onClose: deleteActivityOnClose,
	} = useDisclosure();

	const {
		isOpen: editActivityIsOpen,
		onOpen: editActivityOnOpen,
		onClose: editActivityOnClose,
	} = useDisclosure();

	const cancelRef = useRef();

	const handleDeleteActivity = async () => {
		const result = await deleteActivity(activityID);
		if (result?.success) {
			setActivities(activities.filter((a) => a.activityID !== activityID)); // no need to ask from server again
		}
		deleteActivityOnClose();
	};

	return (
		<Box
			bgGradient="linear(to-r, teal.100, green.100)"
			p={6}
			borderRadius="2xl"
			boxShadow="lg"
			w="90%"
			mx="auto"
			my={8}
			transition="all 0.2s ease-in-out"
			_hover={{ transform: "scale(1.02)", boxShadow: "xl" }}
		>
			<HStack mb={4}>
				<Text fontSize="2xl" fontWeight="extrabold" color="green.700">
					{exerciseName}
				</Text>
				<Spacer />
				<IconButton
					aria-label="Edit exercise"
					icon={<EditIcon />}
					colorScheme="yellow"
					variant="ghost"
					size="lg"
					onClick={editActivityOnOpen}
				/>
				<IconButton
					aria-label="Delete exercise"
					icon={<DeleteIcon />}
					colorScheme="red"
					variant="ghost"
					size="lg"
					onClick={deleteActivityOnOpen}
				/>
			</HStack>

			<TableContainer
				borderRadius="lg"
				overflow="scroll"
				boxShadow="sm"
				border={"2px"}
				borderColor={"gray.500"}
			>
				<Table variant="striped" colorScheme="green" size="md">
					<Thead bg="green.200">
						<Tr>
							<Th color="green.900" fontWeight="bold" textAlign="center">
								Sets
							</Th>
							{sets.map((set) => (
								<Th
									key={set.setID}
									color="green.900"
									fontWeight="bold"
									textAlign="center"
								>
									Set {set.setNumber}
								</Th>
							))}
						</Tr>
					</Thead>
					<Tbody>
						<Tr>
							<Td fontWeight="semibold" textAlign="center" color="teal.800">
								Weight (kg)
							</Td>
							{sets.map((set) => (
								<Td key={set.setID} textAlign="center">
									{set.weight}
								</Td>
							))}
						</Tr>
						<Tr>
							<Td fontWeight="semibold" textAlign="center" color="teal.800">
								Reps
							</Td>
							{sets.map((set) => (
								<Td key={set.setID} textAlign="center">
									{set.reps}
								</Td>
							))}
						</Tr>
					</Tbody>
				</Table>
			</TableContainer>

			<AlertDialog
				isOpen={deleteActivityIsOpen}
				leastDestructiveRef={cancelRef}
				onClose={deleteActivityOnClose}
			>
				<AlertDialogOverlay>
					<AlertDialogContent>
						<AlertDialogHeader fontSize="lg" fontWeight="bold">
							Delete Activity
						</AlertDialogHeader>

						<AlertDialogBody>
							Are you sure you want to delete <b>{exerciseName}</b>? This action
							cannot be undone.
						</AlertDialogBody>

						<AlertDialogFooter>
							<Button ref={cancelRef} onClick={deleteActivityOnClose}>
								Cancel
							</Button>
							<Button colorScheme="red" onClick={handleDeleteActivity} ml={3}>
								Delete
							</Button>
						</AlertDialogFooter>
					</AlertDialogContent>
				</AlertDialogOverlay>
			</AlertDialog>

			<EditActivityModal
				isOpen={editActivityIsOpen}
				onClose={editActivityOnClose}
				currentActivity={activity}
			/>
		</Box>
	);
};

export default ExerciseSummaryCard;
