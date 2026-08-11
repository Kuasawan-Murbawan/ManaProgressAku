import {
	Box,
	Card,
	CardBody,
	Center,
	Heading,
	SimpleGrid,
	Spinner,
	Tag,
	Text,
	VStack,
	HStack,
} from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { useDisclosure } from "@chakra-ui/react";
import { useExerciseStore } from "../../store/exercise";
import ExerciseDetailModal from "./ExerciseDetailModal";

const ExerciseListComponent = () => {
	const { fetchAllExercises, exercise, isLoading } = useExerciseStore();
	const { isOpen, onOpen, onClose } = useDisclosure();

	const [selectedExercise, setSelectedExercise] = useState(null);

	useEffect(() => {
		fetchAllExercises();
	}, [fetchAllExercises]);

	const handleClick = (exercise) => {
		setSelectedExercise(exercise);
		onOpen();
	};

	const upperBodyExercises = exercise.filter((ex) => ex.exerciseType === "1");

	const lowerBodyExercises = exercise.filter((ex) => ex.exerciseType === "2");

	const ExerciseGrid = ({ title, items, isLowerBody = false }) => (
		<Box mb={12}>
			<HStack mb={5} spacing={3}>
				<Box
					w="8px"
					h="32px"
					borderRadius="full"
					bg={isLowerBody ? "lime.400" : "tiber.500"}
				/>

				<Heading
					fontSize={{ base: "xl", md: "2xl" }}
					fontWeight="700"
					color="tiber.800"
				>
					{title}
				</Heading>

				<Tag
					size="sm"
					variant="subtle"
					bg={isLowerBody ? "lime.100" : "tiber.50"}
					color={isLowerBody ? "tiber.900" : "tiber.700"}
					borderRadius="full"
				>
					{items.length}
				</Tag>
			</HStack>

			{items.length === 0 ? (
				<Text color="gray.500" fontSize="sm">
					No exercises available.
				</Text>
			) : (
				<SimpleGrid columns={{ base: 1, sm: 2, lg: 3 }} spacing={4}>
					{items.map((exercise) => (
						<Card
							key={exercise.exerciseID}
							bg="white"
							border="1px solid"
							borderColor="gray.200"
							borderLeft="4px solid"
							borderLeftColor={isLowerBody ? "lime.400" : "tiber.500"}
							borderRadius="lg"
							boxShadow="sm"
							cursor="pointer"
							transition="all 0.2s ease"
							_hover={{
								transform: "translateY(-3px)",
								boxShadow: "md",
								borderColor: isLowerBody ? "lime.400" : "tiber.500",
							}}
							onClick={() => handleClick(exercise)}
						>
							<CardBody p={5}>
								<VStack align="start" spacing={2}>
									<Text fontSize="lg" fontWeight="700" color="tiber.800">
										{exercise.exerciseName}
									</Text>

									<Text
										fontSize="sm"
										color="gray.600"
										noOfLines={2}
										lineHeight="1.6"
									>
										{exercise.generalInfo || "No description available."}
									</Text>

									<Tag
										size="sm"
										mt={2}
										borderRadius="full"
										bg={isLowerBody ? "lime.100" : "tiber.50"}
										color={isLowerBody ? "tiber.900" : "tiber.700"}
									>
										{isLowerBody ? "Lower Body" : "Upper Body"}
									</Tag>
								</VStack>
							</CardBody>
						</Card>
					))}
				</SimpleGrid>
			)}
		</Box>
	);

	return (
		<Box
			minH="100vh"
			bg="mist.400"
			px={{ base: 4, md: 8 }}
			py={{ base: 6, md: 10 }}
		>
			<Box maxW="1200px" mx="auto">
				{/* Page header */}
				<Box mb={{ base: 8, md: 10 }}>
					<Text
						fontSize={{ base: "sm", md: "md" }}
						fontWeight="600"
						color="tiber.500"
						letterSpacing="wide"
						textTransform="uppercase"
						mb={2}
					>
						Training
					</Text>

					<Heading
						fontSize={{ base: "3xl", md: "4xl" }}
						color="tiber.800"
						fontWeight="700"
						letterSpacing="-0.02em"
					>
						Exercise Library
					</Heading>

					<Text
						mt={3}
						color="gray.600"
						fontSize={{ base: "sm", md: "md" }}
						maxW="600px"
					>
						Browse your exercise library and select an exercise to view its
						details.
					</Text>
				</Box>

				{isLoading ? (
					<Center py={20}>
						<VStack spacing={4}>
							<Spinner size="lg" thickness="3px" color="tiber.500" />
							<Text color="gray.500">Loading exercises...</Text>
						</VStack>
					</Center>
				) : (
					<>
						<ExerciseGrid title="Upper Body" items={upperBodyExercises} />

						<ExerciseGrid
							title="Lower Body"
							items={lowerBodyExercises}
							isLowerBody
						/>
					</>
				)}
			</Box>

			<ExerciseDetailModal
				isOpen={isOpen}
				onClose={onClose}
				currentExercise={selectedExercise}
			/>
		</Box>
	);
};

export default ExerciseListComponent;
