import {
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalHeader,
	ModalOverlay,
	Box,
	Flex,
	Button,
	Input,
	Image,
	Text,
	VStack,
	HStack,
	Alert,
	AlertIcon,
	useBreakpointValue,
	useToast,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { useExerciseStore } from "../../store/exercise";
import { useExerciseDetailsStore } from "../../store/exerciseDetails";

export const LinkExerciseDbModal = ({
	isOpen,
	onClose,
	exercise,
	currentLinkedId,
	onLinked,
}) => {
	const [query, setQuery] = useState("");
	const [hasSearched, setHasSearched] = useState(false);
	const [searchError, setSearchError] = useState("");
	const [linkingId, setLinkingId] = useState(null);

	const toast = useToast();
	const modalSize = useBreakpointValue({ base: "full", md: "lg" });

	const { linkExerciseToDb } = useExerciseStore();
	const {
		searchExerciseDbMatches,
		searchResults,
		isSearching,
		clearSearchResults,
	} = useExerciseDetailsStore();

	useEffect(() => {
		if (isOpen) {
			setQuery(exercise?.exerciseName || "");
			setHasSearched(false);
			setSearchError("");
			setLinkingId(null);
			clearSearchResults();
		}
	}, [isOpen]);

	const handleSearch = async () => {
		const trimmed = query.trim();
		if (!trimmed || isSearching) return;

		setSearchError("");
		clearSearchResults();

		const result = await searchExerciseDbMatches(trimmed);
		setHasSearched(true);

		if (!result.success) {
			setSearchError(result.message);
		}
	};

	const handleSelect = async (match) => {
		console.log(exercise);
		console.log(match);
		setLinkingId(match.exerciseId);
		const result = await linkExerciseToDb(
			exercise.exerciseID,
			match.exerciseId,
		);
		setLinkingId(null);

		if (result.success) {
			toast({
				title: "Linked",
				description: `${exercise.exerciseName} is now linked to ${match.name}.`,
				status: "success",
				duration: 3000,
				isClosable: true,
			});
			onLinked(match.exerciseId);
			onClose();
		} else {
			toast({
				title: "Failed to link",
				description: result.message,
				status: "error",
				duration: 3000,
				isClosable: true,
			});
		}
	};

	return (
		<Modal
			isOpen={isOpen}
			onClose={onClose}
			size={modalSize}
			isCentered={modalSize !== "full"}
			scrollBehavior="inside"
		>
			<ModalOverlay bg="blackAlpha.600" />
			<ModalContent bg="mist.400" borderRadius={{ base: 0, md: "xl" }}>
				<ModalHeader fontFamily="heading" fontWeight="700" color="tiber.800">
					Link to ExerciseDB
					<Text fontSize="sm" fontWeight="400" color="tiber.700" opacity={0.7}>
						{exercise?.exerciseName}
					</Text>
				</ModalHeader>
				<ModalCloseButton />

				<ModalBody>
					<VStack spacing={4} align="stretch">
						<HStack>
							<Input
								value={query}
								onChange={(e) => setQuery(e.target.value)}
								onKeyDown={(e) => e.key === "Enter" && handleSearch()}
								placeholder="Search ExerciseDB, e.g. bench press"
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_focus={{
									borderColor: "tiber.600",
									boxShadow: "0 0 0 1px #146059",
								}}
							/>
							<Button
								bg="tiber.800"
								color="white"
								_hover={{ bg: "tiber.900" }}
								onClick={handleSearch}
								isLoading={isSearching}
								isDisabled={!query.trim()}
							>
								Search
							</Button>
						</HStack>

						{searchError && (
							<Alert status="error" borderRadius="md" fontSize="sm">
								<AlertIcon />
								{searchError}
							</Alert>
						)}

						{searchResults.map((match) => {
							const isCurrent = match.exerciseId === currentLinkedId;

							return (
								<Flex
									key={match.exerciseId}
									align="center"
									gap={3}
									bg="white"
									borderRadius="lg"
									boxShadow="sm"
									borderLeft="4px solid"
									borderLeftColor={isCurrent ? "lime.400" : "tiber.600"}
									p={3}
								>
									<Image
										src={match.imageUrl}
										alt={match.name}
										boxSize="56px"
										objectFit="cover"
										borderRadius="md"
										flexShrink={0}
										fallback={
											<Box
												boxSize="56px"
												bg="mist.400"
												borderRadius="md"
												flexShrink={0}
											/>
										}
									/>

									<Box flex="1" minW={0}>
										<Text fontWeight="600" color="tiber.800" noOfLines={1}>
											{match.name}
										</Text>
										{isCurrent && (
											<Text fontSize="xs" color="tiber.600">
												Currently linked
											</Text>
										)}
									</Box>

									<Button
										size="sm"
										bg="tiber.800"
										color="white"
										_hover={{ bg: "tiber.900" }}
										onClick={() => handleSelect(match)}
										isLoading={linkingId === match.exerciseId}
										isDisabled={linkingId !== null || isCurrent}
									>
										{isCurrent ? "Linked" : "Select"}
									</Button>
								</Flex>
							);
						})}

						{hasSearched && !searchError && searchResults.length === 0 && (
							<Box bg="white" borderRadius="lg" p={4} textAlign="center">
								<Text color="tiber.800" fontWeight="600">
									No matches found
								</Text>
								<Text fontSize="sm" color="tiber.700" opacity={0.7}>
									Try a shorter or more common name.
								</Text>
							</Box>
						)}
					</VStack>
				</ModalBody>

				<ModalFooter>
					<Button variant="ghost" onClick={onClose}>
						Close
					</Button>
				</ModalFooter>
			</ModalContent>
		</Modal>
	);
};
