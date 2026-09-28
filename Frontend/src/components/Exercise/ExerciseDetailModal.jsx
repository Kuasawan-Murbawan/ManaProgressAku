import React, { useEffect, useRef, useState } from "react";
import {
	AlertDialog,
	AlertDialogBody,
	AlertDialogContent,
	AlertDialogFooter,
	AlertDialogHeader,
	AlertDialogOverlay,
	Box,
	Button,
	Center,
	Divider,
	Flex,
	Image,
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalHeader,
	ModalOverlay,
	Skeleton,
	Tag,
	Text,
	VStack,
	useBreakpointValue,
	useDisclosure,
	useToast,
} from "@chakra-ui/react";
import { useAuthStore } from "../../store/auth";
import { useExerciseStore } from "../../store/exercise";
import { useExerciseDetailsStore } from "../../store/exerciseDetails";
import { EQUIPMENT_OPTIONS } from "../../constants/equipment";
import EditExerciseModal from "./EditExerciseModal";
import ExerciseInstruction from "./ExerciseInstruction";

const SectionCard = ({ title, accent, children }) => (
	<Box
		bg="white"
		borderRadius="xl"
		boxShadow="sm"
		p={4}
		borderLeft={accent ? "5px solid" : undefined}
		borderLeftColor={accent}
	>
		{title && (
			<Text
				fontSize="xs"
				fontWeight="700"
				color="tiber.700"
				textTransform="uppercase"
				letterSpacing="0.05em"
				mb={2}
			>
				{title}
			</Text>
		)}
		{children}
	</Box>
);

const DetailRow = ({ label, children }) => (
	<Flex justify="space-between" align="center" py={2.5} gap={4}>
		<Text fontSize="sm" color="tiber.600">
			{label}
		</Text>
		<Box fontSize="sm" fontWeight="600" color="tiber.800" textAlign="right">
			{children}
		</Box>
	</Flex>
);

const MuscleTags = ({ muscles, primary = false }) => (
	<Flex wrap="wrap" gap={2}>
		{muscles.map((muscle) => (
			<Tag
				key={muscle}
				size="sm"
				borderRadius="full"
				textTransform="capitalize"
				bg={primary ? "tiber.100" : "mist.400"}
				color="tiber.800"
				fontWeight="600"
			>
				{muscle.toLowerCase()}
			</Tag>
		))}
	</Flex>
);

const ExerciseDetailModal = ({ isOpen, onClose, currentExercise }) => {
	const [isDeleting, setIsDeleting] = useState(false);
	const [fetchedKey, setFetchedKey] = useState(null);

	const toast = useToast();
	const cancelRef = useRef();
	const modalSize = useBreakpointValue({ base: "full", md: "md" });

	const deleteExercise = useExerciseStore((state) => state.deleteExercise);

	// currentExercise is a snapshot taken when the card was clicked, so it goes
	// stale after an edit or a link. Read the fresh row from the store instead.
	const liveExercise = useExerciseStore((state) =>
		state.exercise.find((e) => e.exerciseID === currentExercise?.exerciseID),
	);
	const exercise = liveExercise ?? currentExercise;

	const exerciseID = exercise?.exerciseID;
	const ascendExerciseId = exercise?.ascendExerciseId;
	const fetchKey = `${exerciseID}:${ascendExerciseId}`;

	const fetchDetails = useExerciseDetailsStore((state) => state.fetchDetails);
	const details = useExerciseDetailsStore(
		(state) => state.detailsByExerciseId[exerciseID],
	);

	const {
		isOpen: editExerciseIsOpen,
		onOpen: editExerciseOnOpen,
		onClose: editExerciseOnClose,
	} = useDisclosure();

	const {
		isOpen: deleteConfirmIsOpen,
		onOpen: deleteConfirmOnOpen,
		onClose: deleteConfirmOnClose,
	} = useDisclosure();

	const role = useAuthStore((state) => state.user?.role);
	const isAdmin = role === "ADMIN";

	// Fetch enrichment only while the modal is open, and only for linked
	// exercises. An unlinked exercise makes no request at all.
	useEffect(() => {
		if (!isOpen || !ascendExerciseId) return;

		let cancelled = false;
		fetchDetails(exerciseID).finally(() => {
			if (!cancelled) setFetchedKey(fetchKey);
		});

		return () => {
			cancelled = true;
		};
	}, [isOpen, exerciseID, ascendExerciseId]);

	const handleDelete = async () => {
		setIsDeleting(true);
		const result = await deleteExercise(exercise.exerciseID);
		setIsDeleting(false);

		if (result.success) {
			toast({
				title: "Deleted",
				description: result.message,
				status: "success",
				duration: 3000,
				isClosable: true,
			});
			deleteConfirmOnClose();
			onClose();
		} else {
			toast({
				title: "Failed",
				description: result.message,
				status: "error",
				duration: 3000,
				isClosable: true,
			});
			deleteConfirmOnClose();
		}
	};

	if (!exercise) return null;

	const isLowerBody = exercise.exerciseType === "2";
	const accent = isLowerBody ? "lime.400" : "tiber.600";
	const isLinked = Boolean(ascendExerciseId);

	// The key includes ascendExerciseId, so re-linking an exercise shows the
	// skeleton again instead of the previous match's image.
	const detailsReady = fetchedKey === fetchKey;
	const showSkeleton = isLinked && !detailsReady;
	const enriched = detailsReady && details?.available ? details : null;

	const equipmentLabel = exercise.equipment
		? (EQUIPMENT_OPTIONS.find((o) => o.value === exercise.equipment)?.label ??
			exercise.equipment)
		: null;

	const hasMuscles =
		enriched &&
		(enriched.targetMuscles?.length > 0 ||
			enriched.secondaryMuscles?.length > 0);

	const hasInstructions = enriched?.instructions?.length > 0;

	return (
		<div>
			<Modal
				isOpen={isOpen}
				onClose={onClose}
				size={modalSize}
				isCentered={modalSize !== "full"}
				scrollBehavior="inside"
			>
				<ModalOverlay bg="blackAlpha.600" />
				<ModalContent borderRadius={{ base: 0, md: "xl" }} overflow="hidden">
					<ModalHeader
						bg="tiber.800"
						color="white"
						fontFamily="heading"
						fontWeight="700"
						py={5}
						pr={12}
					>
						{exercise.exerciseName}
					</ModalHeader>
					<ModalCloseButton color="white" top={4} />

					<ModalBody bg="mist.400" p={4}>
						<VStack spacing={4} align="stretch">
							{showSkeleton ? (
								<Skeleton borderRadius="xl" aspectRatio={16 / 9} />
							) : enriched?.imageUrl ? (
								<Box
									aspectRatio={16 / 9}
									bg="white"
									borderRadius="xl"
									boxShadow="sm"
									overflow="hidden"
								>
									<Image
										src={enriched.imageUrl}
										alt={exercise.exerciseName}
										w="100%"
										h="100%"
										objectFit="contain"
									/>
								</Box>
							) : (
								<Center
									h="120px"
									bg="mist.400"
									border="1px dashed"
									borderColor="mist.300"
									borderRadius="xl"
								>
									<Text fontSize="xs" color="tiber.600" opacity={0.6}>
										No image available
									</Text>
								</Center>
							)}

							<SectionCard title="About" accent={accent}>
								<Text fontSize="sm" color="tiber.700" lineHeight="1.6">
									{exercise.generalInfo || "No description yet."}
								</Text>
							</SectionCard>

							<SectionCard title="Details">
								<VStack
									spacing={0}
									align="stretch"
									divider={<Divider borderColor="mist.200" />}
								>
									<DetailRow label="Type">
										{isLowerBody ? "Lower body" : "Upper body"}
									</DetailRow>

									{(equipmentLabel || isAdmin) && (
										<DetailRow label="Equipment">
											{equipmentLabel ?? (
												<Text as="span" fontWeight="400" opacity={0.6}>
													Not set
												</Text>
											)}
										</DetailRow>
									)}

									<DetailRow label="Bodyweight">
										{exercise.isBodyweight ? "Yes" : "No"}
									</DetailRow>

									{isAdmin && (
										<DetailRow label="ExerciseDB">
											{isLinked ? (
												<>
													<Text as="span">Linked</Text>
													<Text
														fontSize="xs"
														fontWeight="400"
														color="tiber.600"
														opacity={0.7}
													>
														{ascendExerciseId}
													</Text>
												</>
											) : (
												<Text as="span" fontWeight="400" opacity={0.6}>
													Not linked
												</Text>
											)}
										</DetailRow>
									)}
								</VStack>
							</SectionCard>

							{hasMuscles && (
								<SectionCard title="Muscles">
									<VStack align="stretch" spacing={3}>
										{enriched.targetMuscles?.length > 0 && (
											<Box>
												<Text fontSize="xs" color="tiber.600" mb={1.5}>
													Target
												</Text>
												<MuscleTags muscles={enriched.targetMuscles} primary />
											</Box>
										)}
										{enriched.secondaryMuscles?.length > 0 && (
											<Box>
												<Text fontSize="xs" color="tiber.600" mb={1.5}>
													Secondary
												</Text>
												<MuscleTags muscles={enriched.secondaryMuscles} />
											</Box>
										)}
									</VStack>
								</SectionCard>
							)}

							{hasInstructions && (
								<SectionCard>
									<ExerciseInstruction steps={enriched.instructions} />
								</SectionCard>
							)}

							{enriched && (
								<Text
									fontSize="xs"
									color="tiber.600"
									opacity={0.6}
									textAlign="center"
								>
									Images and muscle data from ExerciseDB
								</Text>
							)}
						</VStack>
					</ModalBody>

					{isAdmin && (
						<ModalFooter
							justifyContent="center"
							gap={4}
							borderTop="1px solid"
							borderColor="mist.200"
						>
							<Button
								minW="110px"
								bg="tiber.800"
								color="white"
								_hover={{ bg: "tiber.900" }}
								onClick={editExerciseOnOpen}
							>
								Edit
							</Button>
							<Button
								minW="110px"
								variant="outline"
								borderColor="red.300"
								color="red.500"
								_hover={{ bg: "red.50" }}
								onClick={deleteConfirmOnOpen}
							>
								Delete
							</Button>
						</ModalFooter>
					)}
				</ModalContent>
			</Modal>

			{/* Deliberately the original snapshot, not the live row. See notes. */}
			<EditExerciseModal
				isOpen={editExerciseIsOpen}
				onClose={editExerciseOnClose}
				onCloseExerciseDetail={onClose}
				currentExercise={currentExercise}
			/>

			<AlertDialog
				isOpen={deleteConfirmIsOpen}
				leastDestructiveRef={cancelRef}
				onClose={deleteConfirmOnClose}
			>
				<AlertDialogOverlay>
					<AlertDialogContent>
						<AlertDialogHeader fontSize="lg" fontWeight="bold">
							Delete Exercise
						</AlertDialogHeader>

						<AlertDialogBody>
							Are you sure you want to delete <b>{exercise.exerciseName}</b>?
							This cannot be undone.
						</AlertDialogBody>

						<AlertDialogFooter>
							<Button ref={cancelRef} onClick={deleteConfirmOnClose}>
								Cancel
							</Button>
							<Button
								colorScheme="red"
								onClick={handleDelete}
								ml={3}
								isLoading={isDeleting}
							>
								Delete
							</Button>
						</AlertDialogFooter>
					</AlertDialogContent>
				</AlertDialogOverlay>
			</AlertDialog>
		</div>
	);
};

export default ExerciseDetailModal;
