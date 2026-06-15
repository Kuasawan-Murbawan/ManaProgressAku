import React from "react";
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
} from "@chakra-ui/react";
import { useAuthStore } from "../store/auth";

const ExerciseDetailModal = ({ isOpen, onClose, currentExercise }) => {
	if (!currentExercise) return null;

	const role = useAuthStore((state) => state.user?.role);
	const isAdmin = role === "ADMIN";

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
								<Button w={100} colorScheme="yellow">
									Edit
								</Button>
								<Button w={100} colorScheme={"red"} onClick={handleDelete}>
									Delete
								</Button>
							</HStack>
						)}

						{/* <Button onClick={onClose}>Okay</Button> */}
					</ModalFooter>
				</ModalContent>
			</Modal>
		</div>
	);
};

export default ExerciseDetailModal;
