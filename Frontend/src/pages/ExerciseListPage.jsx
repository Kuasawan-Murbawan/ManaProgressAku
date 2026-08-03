import React from "react";
import ExerciseListComponent from "../components/Exercise/ExerciseListComponent";
import { Box, Button, useDisclosure } from "@chakra-ui/react";
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
				display="flex"
				justifyContent="center"
				alignItems="center"
				pb={6} // add vertical padding so it doesn’t stick to the top
				bg={"gray.300"}
			>
				<Button
					size="lg"
					bg="pink.200"
					_hover={{ bg: "pink.300", transform: "scale(1.05)" }}
					borderRadius="xl"
					shadow="md"
					onClick={() => navigate("/")}
				>
					⬅ Home
				</Button>
				{isAdmin && (
					<Button size="lg" colorScheme="green" margin="20px" onClick={onOpen}>
						➕ Add Exercise
					</Button>
				)}

				<InsertExerciseModal isOpen={isOpen} onClose={onClose} />
			</Box>
		</div>
	);
};

export default ExerciseListPage;
