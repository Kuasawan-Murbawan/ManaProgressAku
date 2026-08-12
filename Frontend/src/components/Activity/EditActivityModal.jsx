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
	VStack,
	Text,
	useBreakpointValue,
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
	const modalSize = useBreakpointValue({ base: "full", md: "md" });

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
			<Modal
				isOpen={isOpen}
				onClose={onClose}
				closeOnOverlayClick={false}
				size={modalSize}
				isCentered={modalSize !== "full"}
			>
				<ModalOverlay bg="blackAlpha.600" />
				<ModalContent borderRadius={{ base: 0, md: "xl" }} boxShadow="md">
					<ModalHeader
						fontFamily="heading"
						fontWeight="700"
						fontSize="xl"
						color="tiber.800"
					>
						Edit Activity
					</ModalHeader>
					<ModalCloseButton color="tiber.700" />
					<ModalBody maxH="70vh" overflowY="auto">
						<VStack spacing={4}>
							{editedSets.map((set, index) => (
								<SetComponent
									key={set.setID}
									currentNumber={set.setNumber}
									weight={set.weight}
									reps={set.reps}
									onChange={(field, value) => handleChange(index, field, value)}
								/>
							))}
						</VStack>
					</ModalBody>
					<ModalFooter borderTop="1px solid" borderColor="mist.200" gap={3}>
						<Button
							onClick={handleCancel}
							variant="outline"
							borderColor="red.300"
							color="red.500"
							_hover={{ bg: "red.50" }}
						>
							Cancel
						</Button>

						<Button
							onClick={handleSaveEdit}
							isDisabled={!isAllFieldsFilled()}
							bg="lime.400"
							color="tiber.900"
							_hover={{ bg: "lime.300" }}
							_disabled={{
								bg: "mist.300",
								color: "tiber.500",
								cursor: "not-allowed",
							}}
						>
							Save Changes
						</Button>
					</ModalFooter>
				</ModalContent>
			</Modal>
		</div>
	);
};

export default EditActivityModal;
