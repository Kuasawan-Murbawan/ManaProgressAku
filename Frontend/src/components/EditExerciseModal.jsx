import {
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalHeader,
	ModalOverlay,
	HStack,
	Button,
	Input,
	Select,
	VStack,
	Box,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { useExerciseStore } from "../store/exercise";

const EditExerciseModal = ({
	isOpen,
	onClose,
	currentExercise,
	onCloseExerciseDetail,
}) => {
	const [exerciseName, setExerciseName] = useState("");
	const [generalInfo, setgeneralInfo] = useState("");
	const [exerciseType, setExerciseType] = useState("");

	useEffect(() => {
		if (currentExercise) {
			setExerciseName(currentExercise.exerciseName || "");
			setgeneralInfo(currentExercise.generalInfo || "");
			setExerciseType(currentExercise.exerciseType || "");
		}
	}, [currentExercise]);

	const { editExercise } = useExerciseStore();

	const resetForm = () => {
		setExerciseName(currentExercise?.exerciseName || "");
		setgeneralInfo(currentExercise?.generalInfo || "");
		setExerciseType(currentExercise?.exerciseType || "");
	};

	const handleCancel = () => {
		resetForm();
		onClose();
	};

	const handleSaveEdit = async () => {
		const updatedExercise = {
			exerciseID: currentExercise.exerciseID,
			exerciseName: exerciseName,
			generalInfo: generalInfo,
			exerciseType: Number(exerciseType),
		};
		console.log(updatedExercise);

		const result = await editExercise(updatedExercise);
		if (result.success) {
			onClose();
			onCloseExerciseDetail();
		}
	};

	return (
		<div>
			<Modal isOpen={isOpen} onClose={onClose} closeOnOverlayClick={false}>
				<ModalOverlay />
				<ModalContent>
					<ModalHeader>Edit Exercise</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<VStack>
							<Box>
								<VStack>
									<Input
										placeholder="Exercise Name"
										variant="outline"
										value={exerciseName}
										onChange={(e) => setExerciseName(e.target.value)}
									/>
									<Input
										placeholder="Exercise Details"
										variant="outline"
										value={generalInfo}
										onChange={(e) => setgeneralInfo(e.target.value)}
									/>
									<Select
										placeholder="Type"
										value={exerciseType}
										onChange={(e) => setExerciseType(e.target.value)}
									>
										<option value="1">Upper Body</option>
										<option value="2">Lower Body</option>
									</Select>
								</VStack>
							</Box>
						</VStack>
					</ModalBody>
					<ModalFooter>
						<HStack gap={5}>
							<Button onClick={handleCancel} bg={"red.400"}>
								Cancel
							</Button>
							<Button onClick={handleSaveEdit}>Save</Button>
						</HStack>
					</ModalFooter>
				</ModalContent>
			</Modal>
		</div>
	);
};

export default EditExerciseModal;
