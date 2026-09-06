import React, { useState, useEffect, useRef } from "react";
import {
	Button,
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalHeader,
	ModalOverlay,
	Text,
	Box,
	HStack,
	useDisclosure,
	useToast,
	AlertDialog,
	AlertDialogOverlay,
	AlertDialogContent,
	AlertDialogHeader,
	AlertDialogBody,
	AlertDialogFooter,
} from "@chakra-ui/react";
import { useAuthStore } from "../../store/auth";
import { useExerciseStore } from "../../store/exercise";
import EditExerciseModal from "./EditExerciseModal";

const ExerciseDetailModal = ({ isOpen, onClose, currentExercise }) => {
	const [exerciseName, setExerciseName] = useState("");
	const [generalInfo, setGeneralInfo] = useState("");
	const [exerciseType, setExerciseType] = useState("");
	const [isDeleting, setIsDeleting] = useState(false);

	const toast = useToast();
	const cancelRef = useRef();
	const { deleteExercise } = useExerciseStore();

	useEffect(() => {
		if (currentExercise) {
			setExerciseName(currentExercise.exerciseName || "");
			setGeneralInfo(currentExercise.generalInfo || "");
			setExerciseType(currentExercise.exerciseType || "");
		}
	}, [currentExercise]);

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

	const handleDelete = async () => {
		setIsDeleting(true);
		const result = await deleteExercise(currentExercise.exerciseID);
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

	if (!currentExercise) return null;

	return (
		<div>
			<Modal isOpen={isOpen} onClose={onClose}>
				<ModalOverlay />
				<ModalContent>
					<ModalHeader>{currentExercise.exerciseName}</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<Text>Exercise Image</Text>
						<Text>{currentExercise.generalInfo}</Text>
					</ModalBody>
					<ModalFooter justifyContent="center" w={"100%"} py={19}>
						{isAdmin && (
							<HStack gap={7}>
								<Button
									w={100}
									colorScheme="yellow"
									onClick={editExerciseOnOpen}
								>
									Edit
								</Button>
								<Button
									w={100}
									colorScheme={"red"}
									onClick={deleteConfirmOnOpen}
								>
									Delete
								</Button>
							</HStack>
						)}
					</ModalFooter>
				</ModalContent>
			</Modal>

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
							Are you sure you want to delete <b>{exerciseName}</b>? This cannot
							be undone.
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
