import { Button, VStack, Text, Box, Icon, useToast } from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import SetComponent from "../components/Activity/SetComponent";
import { useActivityStore } from "../store/activity";
import { useSessionStore } from "../store/session";
import { useSetStore } from "../store/set";
import { useProfileStore } from "../store/profile";

// Simple placeholder glyph — no external icon package needed.
// Swap this block for a real <Image src={exercise.imageUrl} /> in v1.2.1.
const ImagePlaceholderIcon = (props) => (
	<Icon viewBox="0 0 24 24" {...props}>
		<path
			fill="currentColor"
			d="M20 5H4c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 12H4V7h16v10zM8.5 11a1.5 1.5 0 100-3 1.5 1.5 0 000 3zM5 16l3.5-4.5 2.5 3.01L14.5 10 19 16H5z"
		/>
	</Icon>
);

const CurrentActivityPage = () => {
	const location = useLocation();
	const toast = useToast();
	const navigate = useNavigate();
	const { addActivity } = useActivityStore();
	const [activityID, setActivityID] = useState(null);
	const { sessionID } = useSessionStore();
	const { addSet } = useSetStore();
	const { profile, fetchProfile } = useProfileStore();

	const { exercise } = location.state || {};
	const [started, setStarted] = useState(false);
	const [sets, setSets] = useState([]);
	const [isSaving, setIsSaving] = useState(false);

	// Source of truth for "how many sets have actually been POSTed."
	// Not derived from array length/index — that breaks once a set can be deleted.
	const [savedCount, setSavedCount] = useState(0);

	useEffect(() => {
		fetchProfile();
	}, []);

	// Whether this exercise's weight should autofill from the profile.
	// Two-part check: the exercise must be flagged bodyweight AND the user
	// must actually have a weight on file — falls back to a normal editable
	// field, with a prompt, when the profile hasn't been filled in.
	const hasBodyweightAutofill = Boolean(
		exercise?.isBodyweight && profile?.weightKg,
	);
	const bodyweightNoProfile = Boolean(
		exercise?.isBodyweight && !profile?.weightKg,
	);

	const getInitialSetWeight = () => {
		if (hasBodyweightAutofill) {
			return String(profile.weightKg);
		}
		return "";
	};

	const isAllFieldsFilled = () =>
		sets.every(
			(set) => set.weight !== "" && set.weight <= 500 && set.reps !== "",
		);

	const failedToast = (result) => {
		toast({
			title: "Failed",
			description: result?.message || "Failed to save set.",
			status: "error",
			duration: 3000,
			isClosable: true,
		});
	};

	const handleBegin = async () => {
		const result = await addActivity({
			sessionID: sessionID,
			exerciseID: exercise.exerciseID,
		});
		if (result?.success) {
			setActivityID(result.activityID);
			setSets([{ weight: getInitialSetWeight(), reps: "" }]);
			setSavedCount(0);
			setStarted(true);
		} else {
			failedToast(result);
		}
	};

	const handleAddSet = async () => {
		if (!isAllFieldsFilled()) {
			toast({
				title: "Incomplete Set",
				description:
					"Please fill in weight and reps before adding another set.",
				status: "warning",
				duration: 3000,
				isClosable: true,
			});
			return;
		}

		const currentSet = sets[sets.length - 1];
		const currentSetNumber = savedCount + 1;

		const result = await addSet({
			setNumber: currentSetNumber,
			weight: parseFloat(currentSet.weight),
			reps: parseInt(currentSet.reps),
			activityID: activityID,
		});

		if (!result?.success) {
			failedToast(result);
		}

		setSavedCount(currentSetNumber);
		setSets([...sets, { weight: getInitialSetWeight(), reps: "" }]);
	};

	const handleDeleteSet = (index) => {
		if (index < savedCount) return; // saved sets can't be removed here
		if (sets.length <= 1) return; // always keep one set in progress
		setSets(sets.filter((_, i) => i !== index));
	};

	const handleSave = async () => {
		if (!isAllFieldsFilled()) {
			toast({
				title: "Incomplete Set",
				description: "Please fill in weight and reps before finishing.",
				status: "error",
				duration: 3000,
				isClosable: true,
			});
			return;
		}

		setIsSaving(true);

		// Only the trailing set can be unsaved. If it was deleted, sets.length
		// equals savedCount and there's nothing new to POST.
		if (sets.length > savedCount) {
			const currentSet = sets[sets.length - 1];
			const currentSetNumber = savedCount + 1;

			const result = await addSet({
				setNumber: currentSetNumber,
				weight: parseFloat(currentSet.weight),
				reps: parseInt(currentSet.reps),
				activityID: activityID,
			});

			setIsSaving(false);

			if (result.success) {
				toast({
					title: "Success!",
					description: result.message,
					status: "success",
					duration: 3000,
					isClosable: true,
				});
				navigate("/createSession");
			} else {
				failedToast(result);
			}
		} else {
			setIsSaving(false);
			toast({
				title: "Success!",
				description: "Exercise saved successfully",
				status: "success",
				duration: 3000,
				isClosable: true,
			});
			navigate("/createSession");
		}
	};

	const handleChange = (index, field, value) => {
		const newSets = [...sets];
		newSets[index][field] = value;
		setSets(newSets);
	};

	if (!exercise) {
		navigate("/createSession");
		return null;
	}

	return (
		<Box
			minH="100vh"
			bg="mist.400"
			py={{ base: 6, md: 10 }}
			px={{ base: 4, md: 6 }}
		>
			<Box w="100%" maxW="600px" mx="auto">
				<VStack spacing={5} align="stretch">
					{/* Before starting: full hero + placeholder info */}
					{!started && (
						<Box
							bg="white"
							borderRadius="xl"
							boxShadow="sm"
							p={{ base: 4, md: 6 }}
						>
							<Text
								fontFamily="heading"
								fontSize={{ base: "xl", md: "2xl" }}
								fontWeight="700"
								color="tiber.800"
								textAlign="center"
								mb={4}
							>
								{exercise.exerciseName}
							</Text>

							{/* Image placeholder — v1.2.1 */}
							<Box
								w="100%"
								aspectRatio={16 / 9}
								bg="mist.400"
								border="1px dashed"
								borderColor="mist.300"
								borderRadius="lg"
								display="flex"
								flexDirection="column"
								alignItems="center"
								justifyContent="center"
								mb={4}
							>
								<Icon
									as={ImagePlaceholderIcon}
									boxSize={7}
									color="tiber.400"
									mb={2}
								/>
								<Text fontSize="xs" color="tiber.600" opacity={0.6}>
									Exercise image — coming in v1.2.1
								</Text>
							</Box>

							{/* Muscle info placeholder — v1.2.1 */}
							<Box
								bg="mist.400"
								border="1px solid"
								borderColor="mist.300"
								borderRadius="lg"
								p={4}
								mb={5}
							>
								<Text
									fontSize="xs"
									fontWeight="700"
									color="tiber.700"
									textTransform="uppercase"
									letterSpacing="0.05em"
									mb={1}
								>
									Muscles Targeted
								</Text>
								<Text fontSize="sm" color="tiber.600" opacity={0.6}>
									Coming in v1.2.1
								</Text>
							</Box>

							<Button
								bg="tiber.800"
								color="white"
								size="lg"
								w="full"
								onClick={handleBegin}
								_hover={{ bg: "tiber.900" }}
							>
								Begin Exercise
							</Button>
						</Box>
					)}

					{/* After starting: compact header */}
					{started && (
						<Box
							display="flex"
							alignItems="center"
							gap={3}
							bg="white"
							borderRadius="xl"
							boxShadow="sm"
							p={4}
						>
							<Box
								boxSize="48px"
								bg="mist.400"
								borderRadius="lg"
								display="flex"
								alignItems="center"
								justifyContent="center"
								flexShrink={0}
							>
								<Icon as={ImagePlaceholderIcon} boxSize={5} color="tiber.400" />
							</Box>
							<Text
								fontFamily="heading"
								fontWeight="700"
								fontSize="lg"
								color="tiber.800"
							>
								{exercise.exerciseName}
							</Text>
						</Box>
					)}

					{/* Sets */}
					{started &&
						sets.map((set, index) => (
							<SetComponent
								key={index}
								currentNumber={index + 1}
								weight={set.weight}
								reps={set.reps}
								onChange={(field, value) => handleChange(index, field, value)}
								isDisabled={index < savedCount}
								onDelete={
									index >= savedCount ? () => handleDeleteSet(index) : undefined
								}
								weightLocked={hasBodyweightAutofill}
								weightLockedValue={profile?.weightKg}
								bodyweightNoProfile={bodyweightNoProfile}
							/>
						))}

					{started && (
						<VStack spacing={3}>
							<Button
								variant="outline"
								borderColor="tiber.600"
								color="tiber.800"
								onClick={handleAddSet}
								isDisabled={!isAllFieldsFilled()}
								w="full"
								_hover={{ bg: "mist.100" }}
							>
								+ Add Set
							</Button>

							<Button
								bg="lime.400"
								color="tiber.900"
								onClick={handleSave}
								isDisabled={sets.length === 0 || !isAllFieldsFilled()}
								isLoading={isSaving}
								w="full"
								_hover={{ bg: "lime.300" }}
							>
								Finish Exercise
							</Button>
						</VStack>
					)}
				</VStack>
			</Box>
		</Box>
	);
};

export default CurrentActivityPage;
