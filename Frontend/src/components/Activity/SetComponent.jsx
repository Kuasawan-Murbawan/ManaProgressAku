import {
	Box,
	HStack,
	NumberInput,
	NumberInputField,
	Text,
	VStack,
} from "@chakra-ui/react";
import { React, useState } from "react";

const SetComponent = ({ currentNumber, weight, reps, onChange }) => {
	const [weightError, setWeightError] = useState("");

	return (
		<Box bg={"mist.400"} p={6} borderRadius="2xl" boxShadow="md">
			<VStack spacing={6}>
				<Text fontWeight="bold" fontSize="xl">
					Set {currentNumber}
				</Text>

				<HStack w="100%" justify="space-between">
					<Text w="70px">Weight:</Text>
					<NumberInput
						bg={"mist.50"}
						borderRadius={"6px"}
						w={"80%"}
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
						w="full"
					>
						<NumberInputField />
					</NumberInput>
					<Text>KG</Text>
				</HStack>

				{weightError && (
					<Text color="red.500" fontSize="sm">
						{weightError}
					</Text>
				)}

				<HStack w="100%" justify="space-between">
					<Text w="70px">Reps:</Text>
					<NumberInput
						bg={"mist.50"}
						borderRadius={"6px"}
						value={reps}
						min={1}
						max={500}
						onChange={(valStr, valNum) => onChange("reps", valNum)}
						w="full"
					>
						<NumberInputField />
					</NumberInput>
				</HStack>
			</VStack>
		</Box>
	);
};

export default SetComponent;
