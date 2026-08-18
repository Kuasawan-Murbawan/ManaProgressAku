import {
	AlertDialog,
	AlertDialogBody,
	AlertDialogContent,
	AlertDialogFooter,
	AlertDialogHeader,
	AlertDialogOverlay,
	Button,
	useToast,
} from "@chakra-ui/react";
import React from "react";
import { useNavigate } from "react-router-dom";
import { useSessionStore } from "../../store/session";

const ActiveSessionDialog = ({ isOpen, onClose }) => {
	const toast = useToast();
	const navigate = useNavigate();
	const { finishSession } = useSessionStore();

	const handleContinue = async () => {
		onClose();
		navigate("/createSession");
	};

	const handleFinish = async () => {
		const result = await finishSession();
		if (result?.success) {
			onClose();
		} else {
			toast({
				title: "Failed",
				description: result?.message || "Failed to finish session.",
				status: "error",
				duration: 3000,
				isClosable: true,
			});
		}
	};

	return (
		<div>
			<AlertDialog isOpen={isOpen}>
				<AlertDialogOverlay>
					<AlertDialogContent>
						<AlertDialogHeader>Active Session Found</AlertDialogHeader>
						<AlertDialogBody>
							You can choose either to <b>Continue</b> or <b>Finish</b> the
							active session.
						</AlertDialogBody>
						<AlertDialogFooter>
							<Button onClick={handleContinue} colorScheme="green">
								Continue
							</Button>
							<Button onClick={handleFinish} ml={3}>
								Finish
							</Button>
						</AlertDialogFooter>
					</AlertDialogContent>
				</AlertDialogOverlay>
			</AlertDialog>
		</div>
	);
};

export default ActiveSessionDialog;
