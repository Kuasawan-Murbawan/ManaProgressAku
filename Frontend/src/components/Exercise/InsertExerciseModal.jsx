import {
	Box,
	Button,
	Input,
	Stack,
	Text,
	VStack,
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalHeader,
	ModalOverlay,
	Select,
	HStack,
	Switch,
	FormControl,
	FormLabel,
} from "@chakra-ui/react";
import { React, useState } from "react";
import { useExerciseStore } from "../../store/exercise";

const InsertExerciseModal = ({ isOpen, onClose }) => {
	const [exerciseName, setExerciseName] = useState("");
	const [generalInfo, setgeneralInfo] = useState("");
	const [exerciseType, setExerciseType] = useState("");
	const [isBodyweight, setIsBodyweight] = useState(false);

	const { insertExercise } = useExerciseStore();

	const resetForm = () => {
		setExerciseName("");
		setgeneralInfo("");
		setExerciseType("");
		setIsBodyweight(false);
	};

	const handleSave = async () => {
		if (!exerciseName.trim() || !exerciseType.trim()) {
			alert("All fields are required!");
			return;
		}

		const newExercise = {
			exerciseName,
			generalInfo,
			exerciseType,
			isBodyweight,
		};

		await insertExercise(newExercise);
		resetForm();
		onClose();
	};

	const handleClose = () => {
		resetForm();
		onClose();
	};

	return (
		<div>
			<Modal isOpen={isOpen} onClose={handleClose}>
				<ModalOverlay />
				<ModalContent>
					<ModalHeader>Insert New Exercise</ModalHeader>
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

									<FormControl display="flex" alignItems="center" mt={2}>
										<FormLabel
											htmlFor="isBodyweight"
											mb="0"
											fontSize="sm"
											color="tiber.700"
										>
											Bodyweight exercise
										</FormLabel>
										<Switch
											id="isBodyweight"
											isChecked={isBodyweight}
											onChange={(e) => setIsBodyweight(e.target.checked)}
											colorScheme="green"
										/>
									</FormControl>
								</VStack>
							</Box>
						</VStack>
					</ModalBody>
					<ModalFooter>
						<HStack gap={5}>
							<Button onClick={handleClose} bg={"red.400"}>
								Cancel
							</Button>
							<Button onClick={handleSave}>Save</Button>
						</HStack>
					</ModalFooter>
				</ModalContent>
			</Modal>
		</div>
	);
};

export default InsertExerciseModal;
