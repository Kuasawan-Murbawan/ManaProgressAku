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
	Switch,
	FormControl,
	FormLabel,
	Text,
	useDisclosure,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { useExerciseStore } from "../../store/exercise";
import { EQUIPMENT_OPTIONS } from "../../constants/equipment";
import { LinkExerciseDbModal } from "./LinkExerciseDbModal";

const EditExerciseModal = ({
	isOpen,
	onClose,
	currentExercise,
	onCloseExerciseDetail,
}) => {
	const [exerciseName, setExerciseName] = useState("");
	const [generalInfo, setgeneralInfo] = useState("");
	const [exerciseType, setExerciseType] = useState("");
	const [isBodyweight, setIsBodyweight] = useState(false);
	const [equipment, setEquipment] = useState("");
	const [ascendExerciseId, setAscendExerciseId] = useState(null);

	const {
		isOpen: linkIsOpen,
		onOpen: linkOnOpen,
		onClose: linkOnClose,
	} = useDisclosure();

	useEffect(() => {
		if (currentExercise) {
			setExerciseName(currentExercise.exerciseName || "");
			setgeneralInfo(currentExercise.generalInfo || "");
			setExerciseType(currentExercise.exerciseType || "");
			setIsBodyweight(currentExercise.isBodyweight || false);
			setEquipment(currentExercise.equipment || "");
			setAscendExerciseId(currentExercise.ascendExerciseId || null);
		}
	}, [currentExercise]);

	const { editExercise } = useExerciseStore();

	// Deliberately does NOT reset ascendExerciseId: linking is saved immediately
	// by its own action, so it is not part of this form's unsaved edits.
	const resetForm = () => {
		setExerciseName(currentExercise?.exerciseName || "");
		setgeneralInfo(currentExercise?.generalInfo || "");
		setExerciseType(currentExercise?.exerciseType || "");
		setIsBodyweight(currentExercise?.isBodyweight || false);
		setEquipment(currentExercise?.equipment || "");
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
			isBodyweight: isBodyweight,
			equipment: equipment || null,
		};

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

									<Select
										placeholder="Equipment (optional)"
										value={equipment}
										onChange={(e) => setEquipment(e.target.value)}
									>
										{EQUIPMENT_OPTIONS.map((opt) => (
											<option key={opt.value} value={opt.value}>
												{opt.label}
											</option>
										))}
									</Select>

									<FormControl display="flex" alignItems="center" mt={2}>
										<FormLabel
											htmlFor="isBodyweightEdit"
											mb="0"
											fontSize="sm"
											color="tiber.700"
										>
											Bodyweight exercise
										</FormLabel>
										<Switch
											id="isBodyweightEdit"
											isChecked={isBodyweight}
											onChange={(e) => setIsBodyweight(e.target.checked)}
											colorScheme="green"
										/>
									</FormControl>

									<Box
										w="100%"
										borderWidth="1px"
										borderRadius="md"
										p={3}
										mt={2}
									>
										<Text fontSize="sm" fontWeight="600">
											ExerciseDB link
										</Text>
										<Text fontSize="xs" color="gray.600" mb={2}>
											{ascendExerciseId
												? `Linked (${ascendExerciseId})`
												: "Not linked to ExerciseDB yet"}
										</Text>
										<Button size="sm" onClick={linkOnOpen}>
											{ascendExerciseId ? "Change match" : "Find match"}
										</Button>
									</Box>
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

			<LinkExerciseDbModal
				isOpen={linkIsOpen}
				onClose={linkOnClose}
				exercise={currentExercise}
				currentLinkedId={ascendExerciseId}
				onLinked={(newId) => setAscendExerciseId(newId)}
			/>
		</div>
	);
};

export default EditExerciseModal;
