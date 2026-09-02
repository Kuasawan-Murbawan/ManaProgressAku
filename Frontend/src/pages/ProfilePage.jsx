// pages/ProfilePage.jsx
import {
	Box,
	VStack,
	Text,
	FormControl,
	FormLabel,
	Input,
	NumberInput,
	NumberInputField,
	Select,
	Button,
	Alert,
	AlertIcon,
	useDisclosure,
	useToast,
	Center,
	Spinner,
	FormErrorMessage,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { useProfileStore } from "../store/profile";
import DeleteAccountModal from "../components/Profile/DeleteAccountModal";

const GENDER_OPTIONS = [
	{ value: "MALE", label: "Male" },
	{ value: "FEMALE", label: "Female" },
	{ value: "OTHER", label: "Other" },
	{ value: "PREFER_NOT_TO_SAY", label: "Prefer not to say" },
];

const ProfilePage = () => {
	const { profile, isLoading, fetchProfile, updateProfile } = useProfileStore();
	const toast = useToast();
	const {
		isOpen: deleteIsOpen,
		onOpen: deleteOnOpen,
		onClose: deleteOnClose,
	} = useDisclosure();

	const [weightKg, setWeightKg] = useState("");
	const [heightCm, setHeightCm] = useState("");
	const [dateOfBirth, setDateOfBirth] = useState("");
	const [gender, setGender] = useState("");
	const [isSaving, setIsSaving] = useState(false);

	useEffect(() => {
		fetchProfile();
	}, []);

	// Sync local form state once the fetched profile arrives.
	useEffect(() => {
		if (profile) {
			setWeightKg(profile.weightKg ?? "");
			setHeightCm(profile.heightCm ?? "");
			setDateOfBirth(profile.dateOfBirth ?? "");
			setGender(profile.gender ?? "");
		}
	}, [profile]);

	const isEmpty =
		profile &&
		!profile.weightKg &&
		!profile.heightCm &&
		!profile.dateOfBirth &&
		!profile.gender;

	const handleSave = async () => {
		setIsSaving(true);
		const result = await updateProfile({
			weightKg: weightKg === "" ? null : parseFloat(weightKg),
			heightCm: heightCm === "" ? null : parseFloat(heightCm),
			dateOfBirth: dateOfBirth === "" ? null : dateOfBirth,
			gender: gender === "" ? null : gender,
		});
		setIsSaving(false);

		toast({
			title: result.success ? "Profile updated" : "Failed to update",
			description: result.success ? undefined : result.message,
			status: result.success ? "success" : "error",
			duration: 3000,
			isClosable: true,
		});
	};

	if (isLoading && !profile) {
		return (
			<Center py={20}>
				<Spinner color="tiber.600" thickness="3px" />
			</Center>
		);
	}

	return (
		<Box
			minH="100vh"
			bg="mist.400"
			py={{ base: 6, md: 10 }}
			px={{ base: 4, md: 6 }}
		>
			<Box w="100%" maxW="480px" mx="auto">
				<Text
					fontFamily="heading"
					fontSize={{ base: "2xl", md: "3xl" }}
					fontWeight="700"
					color="tiber.800"
					mb={6}
				>
					Profile
				</Text>

				{isEmpty && (
					<Alert status="info" borderRadius="md" mb={4} fontSize="sm">
						<AlertIcon />
						Add your details to unlock automatic bodyweight tracking.
					</Alert>
				)}

				<Box
					bg="white"
					borderRadius="xl"
					boxShadow="sm"
					p={{ base: 5, md: 6 }}
					mb={6}
				>
					<VStack spacing={4} align="stretch">
						<FormControl isInvalid={weightKg === ""}>
							<FormLabel fontSize="sm" color="tiber.700">
								Weight (kg)
							</FormLabel>
							<NumberInput
								value={weightKg}
								min={1}
								max={500}
								step={0.1}
								precision={2}
								onChange={(valStr) => setWeightKg(valStr)}
							>
								<NumberInputField
									bg="white"
									borderColor="mist.300"
									borderRadius="lg"
									_focus={{
										borderColor: "tiber.600",
										boxShadow: "0 0 0 1px #146059",
									}}
								/>
							</NumberInput>
							<FormErrorMessage>Not set</FormErrorMessage>
						</FormControl>

						<FormControl isInvalid={heightCm === ""}>
							<FormLabel fontSize="sm" color="tiber.700">
								Height (cm)
							</FormLabel>
							<NumberInput
								value={heightCm}
								min={1}
								max={500}
								step={0.1}
								precision={2}
								onChange={(valStr) => setHeightCm(valStr)}
							>
								<NumberInputField
									bg="white"
									borderColor="mist.300"
									borderRadius="lg"
									_focus={{
										borderColor: "tiber.600",
										boxShadow: "0 0 0 1px #146059",
									}}
								/>
							</NumberInput>
							<FormErrorMessage>Not set</FormErrorMessage>
						</FormControl>

						<FormControl>
							<FormLabel fontSize="sm" color="tiber.700">
								Date of birth
							</FormLabel>
							<Input
								type="date"
								value={dateOfBirth}
								onChange={(e) => setDateOfBirth(e.target.value)}
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_focus={{
									borderColor: "tiber.600",
									boxShadow: "0 0 0 1px #146059",
								}}
							/>
						</FormControl>

						<FormControl>
							<FormLabel fontSize="sm" color="tiber.700">
								Gender
							</FormLabel>
							<Select
								placeholder="Select gender"
								value={gender}
								onChange={(e) => setGender(e.target.value)}
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_focus={{
									borderColor: "tiber.600",
									boxShadow: "0 0 0 1px #146059",
								}}
							>
								{GENDER_OPTIONS.map((opt) => (
									<option key={opt.value} value={opt.value}>
										{opt.label}
									</option>
								))}
							</Select>
						</FormControl>

						<Button
							bg="lime.400"
							color="tiber.900"
							_hover={{ bg: "lime.300" }}
							onClick={handleSave}
							isLoading={isSaving}
						>
							Save changes
						</Button>
					</VStack>
				</Box>

				{/* Danger zone */}
				<Box
					bg="white"
					borderRadius="xl"
					boxShadow="sm"
					border="1px solid"
					borderColor="red.200"
					p={{ base: 5, md: 6 }}
				>
					<Text fontFamily="heading" fontWeight="700" color="red.600" mb={2}>
						Danger zone
					</Text>
					<Text fontSize="sm" color="tiber.700" opacity={0.7} mb={4}>
						Permanently delete your account and all associated data.
					</Text>
					<Button
						variant="outline"
						borderColor="red.300"
						color="red.500"
						_hover={{ bg: "red.50" }}
						onClick={deleteOnOpen}
					>
						Delete account
					</Button>
				</Box>
			</Box>

			<DeleteAccountModal isOpen={deleteIsOpen} onClose={deleteOnClose} />
		</Box>
	);
};

export default ProfilePage;
