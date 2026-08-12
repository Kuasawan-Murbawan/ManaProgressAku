import {
	Box,
	Text,
	HStack,
	Spacer,
	Table,
	Thead,
	Tbody,
	Tr,
	Th,
	Td,
	TableContainer,
	IconButton,
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
			w="100%"
			maxW={"600px"}
			bg="white"
			p={{ base: 4, md: 6 }}
			borderRadius="xl"
			boxShadow="sm"
			borderLeft="5px solid"
			borderLeftColor="tiber.600"
			transition="all 0.2s ease"
			_hover={{
				boxShadow: "md",
				borderLeftColor: "lime.400",
			}}
		>
			{/* Activity Header */}
			<HStack mb={4} align="start">
				<Box>
					<Text
						fontFamily="heading"
						fontSize={{ base: "lg", md: "xl" }}
						fontWeight="700"
						color="tiber.800"
					>
						{exerciseName}
					</Text>
					<Text fontSize="xs" color="tiber.600" opacity={0.6} mt={0.5}>
						{sets.length} {sets.length === 1 ? "set" : "sets"} logged
					</Text>
				</Box>

				<Spacer />

				<IconButton
					aria-label="Edit exercise"
					icon={<EditIcon />}
					color="tiber.800"
					variant="ghost"
					size="md"
					_hover={{ bg: "lime.400" }}
					onClick={editActivityOnOpen}
				/>

				<IconButton
					aria-label="Delete exercise"
					icon={<DeleteIcon />}
					color="red.500"
					variant="ghost"
					size="md"
					_hover={{ bg: "red.50" }}
					onClick={deleteActivityOnOpen}
				/>
			</HStack>

			{/* Sets */}
			<TableContainer
				border="1px solid"
				borderColor="mist.200"
				borderRadius="lg"
				overflow="hidden"
			>
				<Table variant="simple" size="sm">
					<Thead bg="mist.400">
						<Tr>
							<Th
								color="tiber.700"
								fontSize="xs"
								fontWeight="700"
								letterSpacing="0.05em"
								py={3}
							>
								Set
							</Th>
							<Th
								color="tiber.700"
								fontSize="xs"
								fontWeight="700"
								letterSpacing="0.05em"
								isNumeric
								py={3}
							>
								Weight (kg)
							</Th>
							<Th
								color="tiber.700"
								fontSize="xs"
								fontWeight="700"
								letterSpacing="0.05em"
								isNumeric
								py={3}
							>
								Reps
							</Th>
						</Tr>
					</Thead>
					<Tbody>
						{sets.map((set) => (
							<Tr key={set.setID} _hover={{ bg: "mist.50" }}>
								<Td py={3}>
									<HStack spacing={2}>
										<Box
											boxSize="22px"
											borderRadius="full"
											bg="lime.400"
											color="tiber.900"
											fontSize="xs"
											fontWeight="700"
											display="flex"
											alignItems="center"
											justifyContent="center"
											flexShrink={0}
										>
											{set.setNumber}
										</Box>
									</HStack>
								</Td>
								<Td
									isNumeric
									py={3}
									fontWeight="700"
									color="tiber.800"
									fontSize="md"
								>
									{set.weight}
								</Td>
								<Td
									isNumeric
									py={3}
									fontWeight="700"
									color="tiber.800"
									fontSize="md"
								>
									{set.reps}
								</Td>
							</Tr>
						))}
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
