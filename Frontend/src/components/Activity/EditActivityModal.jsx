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
	Center,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { useActivityStore } from "../../store/activity";
import SetComponent from "./SetComponent";

const EditActivityModal = ({ isOpen, onClose, currentActivity }) => {
	const [editedSets, setEditedSets] = useState([]);

	useEffect(() => {
		if (isOpen) {
			resetForm();
		}
	}, [isOpen, currentActivity]);

	const { editActivity } = useActivityStore();

	const handleChange = (index, field, value) => {
		const updated = [...editedSets];
		updated[index][field] = value;
		setEditedSets(updated);
	};

	const handleCancel = () => {
		// resetForm();
		onClose();
	};

	const handleSaveEdit = async () => {
		if (!isAllFieldsFilled()) return;

		const updatedActivity = {
			activityID: currentActivity.activityID,
			activitySetList: editedSets.map((set) => ({
				setID: set.setID,
				setNumber: set.setNumber,
				weight: parseFloat(set.weight),
				reps: parseInt(set.reps),
			})),
		};

		const result = await editActivity(updatedActivity);
		if (result.success) {
			onClose();
		}
	};

	const isAllFieldsFilled = () =>
		editedSets.every(
			(set) => set.weight !== "" && set.weight <= 500 && set.reps !== "",
		);

	const resetForm = () => {
		if (!currentActivity) return;

		setEditedSets(currentActivity.sets.map((set) => ({ ...set })));
	};

	return (
		<div>
			<Modal isOpen={isOpen} onClose={onClose} closeOnOverlayClick={false}>
				<ModalOverlay />
				<ModalContent>
					<ModalHeader ml={"15px"} fontWeight={"700"}>
						Edit Activity
					</ModalHeader>
					<ModalCloseButton />
					<ModalBody>
						<VStack>
							<Box>
								<VStack>
									{editedSets.map((set, index) => (
										<SetComponent
											key={set.setID}
											currentNumber={set.setNumber}
											weight={set.weight}
											reps={set.reps}
											onChange={(field, value) =>
												handleChange(index, field, value)
											}
										/>
									))}
								</VStack>
							</Box>
						</VStack>
					</ModalBody>
					<ModalFooter>
						<HStack>
							<Button colorScheme="red" onClick={handleCancel}>
								Cancel
							</Button>

							<Button
								colorScheme="green"
								onClick={handleSaveEdit}
								isDisabled={!isAllFieldsFilled()}
							>
								Save
							</Button>
						</HStack>
					</ModalFooter>
				</ModalContent>
			</Modal>
		</div>
	);
};

export default EditActivityModal;
