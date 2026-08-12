// NewExercisePage.jsx
import { Box, Flex, Text, VStack, useDisclosure, Icon } from "@chakra-ui/react";
import { ChevronRightIcon } from "@chakra-ui/icons";
import React from "react";
import UpperBodyExercises from "../components/Exercise/UpperBodyExercises";
import LowerBodyExercises from "../components/Exercise/LowerBodyExercises";

const NewExercisePage = () => {
	const {
		isOpen: isOpenUpperBody,
		onOpen: onOpenUpperBody,
		onClose: onCloseUpperBody,
	} = useDisclosure();

	const {
		isOpen: isOpenLowerBody,
		onOpen: onOpenLowerBody,
		onClose: onCloseLowerBody,
	} = useDisclosure();

	return (
		<Box
			minH="100vh"
			bg="mist.400"
			py={{ base: 6, md: 10 }}
			px={{ base: 4, md: 6 }}
		>
			<Box w="100%" maxW="480px" mx="auto">
				<Box mb={8} textAlign="center">
					<Text
						fontFamily="heading"
						fontSize={{ base: "2xl", md: "3xl" }}
						fontWeight="700"
						color="tiber.800"
					>
						Add an exercise
					</Text>
					<Text fontSize="sm" color="tiber.700" opacity={0.7} mt={1}>
						Choose a muscle group to continue
					</Text>
				</Box>

				<VStack spacing={4} w="100%">
					{/* Upper Body */}
					<Flex
						onClick={onOpenUpperBody}
						w="100%"
						justify="space-between"
						align="center"
						bg="white"
						borderRadius="xl"
						boxShadow="sm"
						borderLeft="5px solid"
						borderLeftColor="tiber.600"
						px={6}
						py={5}
						cursor="pointer"
						transition="all 0.15s ease"
						_hover={{ transform: "translateY(-2px)", boxShadow: "md" }}
					>
						<Text
							fontFamily="heading"
							fontSize="lg"
							fontWeight="600"
							color="tiber.800"
						>
							Upper Body
						</Text>
						<Icon as={ChevronRightIcon} color="tiber.600" boxSize={5} />
					</Flex>

					{/* Lower Body */}
					<Flex
						onClick={onOpenLowerBody}
						w="100%"
						justify="space-between"
						align="center"
						bg="white"
						borderRadius="xl"
						boxShadow="sm"
						borderLeft="5px solid"
						borderLeftColor="lime.400"
						px={6}
						py={5}
						cursor="pointer"
						transition="all 0.15s ease"
						_hover={{ transform: "translateY(-2px)", boxShadow: "md" }}
					>
						<Text
							fontFamily="heading"
							fontSize="lg"
							fontWeight="600"
							color="tiber.800"
						>
							Lower Body
						</Text>
						<Icon as={ChevronRightIcon} color="tiber.600" boxSize={5} />
					</Flex>

					{/* Coming Soon — visually disabled, no interaction */}
					<Flex
						w="100%"
						justify="space-between"
						align="center"
						bg="white"
						borderRadius="xl"
						boxShadow="sm"
						borderLeft="5px solid"
						borderLeftColor="mist.300"
						px={6}
						py={5}
						opacity={0.55}
						cursor="not-allowed"
					>
						<Text
							fontFamily="heading"
							fontSize="lg"
							fontWeight="600"
							color="tiber.800"
						>
							Coming Soon
						</Text>
					</Flex>
				</VStack>
			</Box>

			{/* Modals */}
			<UpperBodyExercises
				isOpenUpperBody={isOpenUpperBody}
				onCloseUpperBody={onCloseUpperBody}
			/>
			<LowerBodyExercises
				isOpenLowerBody={isOpenLowerBody}
				onCloseLowerBody={onCloseLowerBody}
			/>
		</Box>
	);
};

export default NewExercisePage;
