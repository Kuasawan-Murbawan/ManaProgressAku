import {
	Box,
	Flex,
	FormControl,
	FormLabel,
	NumberInput,
	NumberInputField,
	Stack,
	Text,
	Badge,
	HStack,
	IconButton,
} from "@chakra-ui/react";
import { CheckIcon, DeleteIcon } from "@chakra-ui/icons";
import { React, useState } from "react";

const SetComponent = ({
	currentNumber,
	weight,
	reps,
	onChange,
	isDisabled = false,
	onDelete,
}) => {
	const [weightError, setWeightError] = useState("");

	return (
		<Box
			bg="white"
			borderRadius="xl"
			boxShadow="sm"
			border="1px solid"
			borderColor="mist.200"
			borderLeft="5px solid"
			borderLeftColor={isDisabled ? "mist.300" : "tiber.600"}
			p={{ base: 4, md: 5 }}
			opacity={isDisabled ? 0.75 : 1}
			transition="all 0.15s ease"
		>
			{/* Set label */}
			<HStack justify="space-between" mb={4}>
				<Flex align="center" gap={3}>
					<Flex
						align="center"
						justify="center"
						boxSize="32px"
						borderRadius="full"
						bg={isDisabled ? "mist.300" : "lime.400"}
						color="tiber.900"
						fontFamily="heading"
						fontWeight="700"
						fontSize="sm"
						flexShrink={0}
					>
						{currentNumber}
					</Flex>
					<Text
						fontFamily="heading"
						fontWeight="700"
						fontSize="md"
						color="tiber.800"
					>
						Set {currentNumber}
					</Text>
				</Flex>

				<HStack spacing={2}>
					{isDisabled && (
						<Badge
							display="flex"
							alignItems="center"
							gap={1}
							bg="mist.100"
							color="tiber.600"
							fontSize="2xs"
							fontWeight="700"
							textTransform="uppercase"
							letterSpacing="0.05em"
							px={2}
							py={1}
							borderRadius="md"
						>
							<CheckIcon boxSize={2} />
							Saved
						</Badge>
					)}

					{onDelete && !isDisabled && (
						<IconButton
							aria-label="Remove set"
							icon={<DeleteIcon />}
							size="sm"
							variant="ghost"
							color="red.400"
							_hover={{ bg: "red.50" }}
							onClick={onDelete}
						/>
					)}
				</HStack>
			</HStack>

			{/* Fields */}
			<Stack direction={{ base: "column", sm: "row" }} spacing={4}>
				<FormControl isDisabled={isDisabled}>
					<FormLabel
						fontSize="xs"
						fontWeight="700"
						color="tiber.700"
						textTransform="uppercase"
						letterSpacing="0.05em"
						mb={1.5}
					>
						Weight (kg)
					</FormLabel>
					<NumberInput
						isDisabled={isDisabled}
						value={weight}
						min={0}
						max={500}
						step={0.1}
						precision={2}
						clampValueOnBlur={true}
						keepWithinRange={true}
						onChange={(valStr) => {
							const weight = parseFloat(valStr);

							if (weight > 500) {
								setWeightError("Weight cannot exceed 500kg");
							} else {
								setWeightError("");
								onChange("weight", valStr);
							}
						}} // take as String to allow decimal
					>
						<NumberInputField
							bg="white"
							borderColor="mist.300"
							borderRadius="lg"
							_hover={{ borderColor: "tiber.400" }}
							_focus={{
								borderColor: "tiber.600",
								boxShadow: "0 0 0 1px #146059",
							}}
							_disabled={{
								bg: "mist.50",
								color: "tiber.600",
								cursor: "not-allowed",
							}}
						/>
					</NumberInput>
					{weightError && (
						<Text color="red.500" fontSize="xs" mt={1}>
							{weightError}
						</Text>
					)}
				</FormControl>

				<FormControl isDisabled={isDisabled}>
					<FormLabel
						fontSize="xs"
						fontWeight="700"
						color="tiber.700"
						textTransform="uppercase"
						letterSpacing="0.05em"
						mb={1.5}
					>
						Reps
					</FormLabel>
					<NumberInput
						isDisabled={isDisabled}
						value={reps}
						min={1}
						max={500}
						onChange={(valStr, valNum) => onChange("reps", valNum)}
					>
						<NumberInputField
							bg="white"
							borderColor="mist.300"
							borderRadius="lg"
							_hover={{ borderColor: "tiber.400" }}
							_focus={{
								borderColor: "tiber.600",
								boxShadow: "0 0 0 1px #146059",
							}}
							_disabled={{
								bg: "mist.50",
								color: "tiber.600",
								cursor: "not-allowed",
							}}
						/>
					</NumberInput>
				</FormControl>
			</Stack>
		</Box>
	);
};

export default SetComponent;
