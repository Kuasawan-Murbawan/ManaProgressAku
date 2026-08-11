import React from "react";
import ExerciseListComponent from "../components/Exercise/ExerciseListComponent";
import { Box, Button, useDisclosure, HStack } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/auth";
import InsertExerciseModal from "../components/Exercise/InsertExerciseModal";

const ExerciseListPage = () => {
	const navigate = useNavigate();
	const { isOpen, onOpen, onClose } = useDisclosure();

	const role = useAuthStore((state) => state.user?.role);
	const isAdmin = role === "ADMIN";

	return (
		<div>
			<ExerciseListComponent />
			<Box
				bg="mist.50"
				borderTop="1px solid"
				borderColor="gray.200"
				px={{ base: 4, md: 8 }}
				py={8}
			>
				<HStack
					maxW="1200px"
					mx="auto"
					justify="center"
					spacing={4}
					flexWrap="wrap"
				>
					<Button
						variant="outline"
						borderColor="tiber.500"
						color="tiber.800"
						_hover={{
							bg: "tiber.50",
						}}
						onClick={() => navigate("/")}
					>
						← Home
					</Button>

					{isAdmin && (
						<Button
							bg="tiber.800"
							color="white"
							_hover={{
								bg: "tiber.700",
								transform: "translateY(-1px)",
							}}
							onClick={onOpen}
						>
							+ Add Exercise
						</Button>
					)}
				</HStack>

				<InsertExerciseModal isOpen={isOpen} onClose={onClose} />
			</Box>
		</div>
	);
};

export default ExerciseListPage;
