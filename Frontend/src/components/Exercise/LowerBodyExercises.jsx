// LowerBodyExercises.jsx
import {
	Box,
	Flex,
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalHeader,
	ModalOverlay,
	VStack,
	Text,
	Center,
	Spinner,
	Icon,
	useBreakpointValue,
} from "@chakra-ui/react";
import { ChevronRightIcon } from "@chakra-ui/icons";
import React, { useEffect } from "react";
import { useExerciseStore } from "../../store/exercise";
import { useNavigate } from "react-router-dom";

const LowerBodyExercises = ({ isOpenLowerBody, onCloseLowerBody }) => {
	const { exercise, fetchAllExercises, isLoading } = useExerciseStore();

	useEffect(() => {
		if (exercise.length < 10) {
			fetchAllExercises();
		}
	}, [exercise, fetchAllExercises]);

	function lowerBodyFilter(exercises) {
		return exercises.filter((item) => item.exerciseType === "2");
	}

	const lowerBodyExercises = lowerBodyFilter(exercise);

	const navigate = useNavigate();
	const modalSize = useBreakpointValue({ base: "full", md: "md" });

	const handleClick = (exercise) => {
		onCloseLowerBody();
		navigate("/currentActivity", {
			state: {
				exercise: exercise,
			},
		});
	};

	return (
		<Modal
			isOpen={isOpenLowerBody}
			onClose={onCloseLowerBody}
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
					textAlign="center"
				>
					Lower Body Exercises
				</ModalHeader>
				<ModalCloseButton color="tiber.700" />
				<ModalBody pb={6}>
					{isLoading ? (
						<Center py={10}>
							<Spinner color="tiber.600" thickness="3px" />
						</Center>
					) : lowerBodyExercises.length === 0 ? (
						<Text textAlign="center" color="tiber.700" opacity={0.7} py={6}>
							No lower body exercises found.
						</Text>
					) : (
						<VStack spacing={3} w="full">
							{lowerBodyExercises.map((exer) => (
								<Flex
									key={exer.exerciseID}
									w="full"
									justify="space-between"
									align="center"
									bg="lime.100"
									borderRadius="lg"
									boxShadow="sm"
									borderLeft="4px solid"
									borderLeftColor="lime.400"
									px={5}
									py={4}
									cursor="pointer"
									transition="all 0.15s ease"
									_hover={{ transform: "translateY(-2px)", boxShadow: "md" }}
									onClick={() => handleClick(exer)}
								>
									<Text fontWeight="600" fontSize="md" color="tiber.800">
										{exer.exerciseName}
									</Text>
									<Icon as={ChevronRightIcon} color="tiber.600" boxSize={4} />
								</Flex>
							))}
						</VStack>
					)}
				</ModalBody>
			</ModalContent>
		</Modal>
	);
};

export default LowerBodyExercises;
