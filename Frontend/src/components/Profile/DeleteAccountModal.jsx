// components/Profile/DeleteAccountModal.jsx
import {
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalHeader,
	ModalOverlay,
	Button,
	VStack,
	Text,
	Input,
	FormControl,
	FormLabel,
	Alert,
	AlertIcon,
	useBreakpointValue,
	useToast,
} from "@chakra-ui/react";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useProfileStore } from "../../store/profile";
import { useAuthStore } from "../../store/auth";
import { useSessionStore } from "../../store/session";

const DeleteAccountModal = ({ isOpen, onClose }) => {
	const [password, setPassword] = useState("");
	const [error, setError] = useState("");
	const [isDeleting, setIsDeleting] = useState(false);

	const { deleteAccount } = useProfileStore();
	const { clearSession, clearCurrentSessionDetails } = useSessionStore();
	const { logout } = useAuthStore();
	const navigate = useNavigate();
	const toast = useToast();
	const modalSize = useBreakpointValue({ base: "full", md: "md" });

	const handleClose = () => {
		setPassword("");
		setError("");
		onClose();
	};

	const handleDelete = async () => {
		if (!password) {
			setError("Please enter your password to confirm.");
			return;
		}

		setIsDeleting(true);
		const result = await deleteAccount(password);
		setIsDeleting(false);

		if (result.success) {
			clearSession();
			clearCurrentSessionDetails();
			logout();
			navigate("/login", { replace: true });
			toast({
				title: "Account deleted",
				status: "success",
				duration: 3000,
				isClosable: true,
			});
		} else {
			setError(result.message);
		}
	};

	return (
		<Modal
			isOpen={isOpen}
			onClose={handleClose}
			size={modalSize}
			isCentered={modalSize !== "full"}
		>
			<ModalOverlay bg="blackAlpha.600" />
			<ModalContent borderRadius={{ base: 0, md: "xl" }}>
				<ModalHeader fontFamily="heading" fontWeight="700" color="red.600">
					Delete account
				</ModalHeader>
				<ModalCloseButton />
				<ModalBody>
					<VStack spacing={4} align="stretch">
						<Alert status="warning" borderRadius="md" fontSize="sm">
							<AlertIcon />
							This will permanently delete your account, all workout sessions,
							and all logged sets. This cannot be undone.
						</Alert>

						{error && (
							<Alert status="error" borderRadius="md" fontSize="sm">
								<AlertIcon />
								{error}
							</Alert>
						)}

						<FormControl>
							<FormLabel fontSize="sm" color="tiber.700">
								Confirm your password
							</FormLabel>
							<Input
								type="password"
								value={password}
								onChange={(e) => setPassword(e.target.value)}
								placeholder="Enter your password"
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_focus={{
									borderColor: "red.400",
									boxShadow: "0 0 0 1px #E53E3E",
								}}
							/>
						</FormControl>
					</VStack>
				</ModalBody>
				<ModalFooter borderTop="1px solid" borderColor="mist.200" gap={3}>
					<Button variant="outline" onClick={handleClose}>
						Cancel
					</Button>
					<Button
						bg="red.600"
						color="white"
						_hover={{ bg: "red.700" }}
						onClick={handleDelete}
						isLoading={isDeleting}
					>
						Permanently delete
					</Button>
				</ModalFooter>
			</ModalContent>
		</Modal>
	);
};

export default DeleteAccountModal;
