import React, { useState, useEffect } from "react";
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
} from "@chakra-ui/react";
import { useAuthStore } from "../store/auth";
import EditExerciseModal from "./EditExerciseModal";

const ExerciseDetailModal = ({ isOpen, onClose, currentExercise }) => {
	const [exerciseName, setExerciseName] = useState("");
	const [generalInfo, setGeneralInfo] = useState("");
	const [exerciseType, setExerciseType] = useState("");

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

	const role = useAuthStore((state) => state.user?.role);
	const isAdmin = role === "ADMIN";

	const handleDelete = () => {};
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
								<Button w={100} colorScheme={"red"}>
									Delete
								</Button>
							</HStack>
						)}

						{/* <Button onClick={onClose}>Okay</Button> */}
					</ModalFooter>
				</ModalContent>
			</Modal>

			<EditExerciseModal
				isOpen={editExerciseIsOpen}
				onClose={editExerciseOnClose}
				onCloseExerciseDetail={onClose}
				currentExercise={currentExercise}
			/>
		</div>
	);
};

export default ExerciseDetailModal;
