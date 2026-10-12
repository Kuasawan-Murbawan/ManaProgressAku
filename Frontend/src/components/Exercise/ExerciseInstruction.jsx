import {
	Box,
	Button,
	Collapse,
	Flex,
	Text,
	VStack,
	useDisclosure,
	AspectRatio,
} from "@chakra-ui/react";
import { ChevronDownIcon, ChevronUpIcon } from "@chakra-ui/icons";
import React from "react";

export const ExerciseInstruction = ({
	steps,
	videoUrl,
	collapsible = false,
	defaultOpen = false,
}) => {
	const { isOpen, onToggle } = useDisclosure({ defaultIsOpen: defaultOpen });

	if (!steps || steps.length === 0) return null;

	const showSteps = !collapsible || isOpen;

	return (
		<Box>
			<Flex justify="space-between" align="center" mb={showSteps ? 3 : 0}>
				<Text
					fontSize="xs"
					fontWeight="700"
					color="tiber.700"
					textTransform="uppercase"
					letterSpacing="0.05em"
				>
					Instructions
				</Text>

				{collapsible && (
					<Button
						size="xs"
						variant="ghost"
						color="tiber.700"
						rightIcon={isOpen ? <ChevronUpIcon /> : <ChevronDownIcon />}
						onClick={onToggle}
						_hover={{ bg: "mist.100" }}
					>
						{isOpen ? "Hide steps" : `Show ${steps.length} steps`}
					</Button>
				)}
			</Flex>

			<Collapse in={showSteps} animateOpacity>
				<VStack
					as="ol"
					align="stretch"
					spacing={3}
					listStyleType="none"
					m={0}
					p={0}
				>
					<AspectRatio maxW="560px" ratio={16 / 9}>
						{/* <iframe title="naruto" src={videoUrl} allowFullScreen autoPlay /> */}
						<video
							autoPlay
							muted
							loop
							playsInline
							controls
							style={{ width: "100%", borderRadius: "8px" }}
						>
							<source src={videoUrl} type="video/mp4" />
							Your browser does not support the video tag.
						</video>
					</AspectRatio>
					{steps.map((step, index) => (
						<Flex as="li" key={index} gap={3} align="flex-start">
							<Flex
								boxSize="24px"
								flexShrink={0}
								borderRadius="full"
								bg="tiber.100"
								color="tiber.800"
								fontFamily="heading"
								fontWeight="700"
								fontSize="xs"
								align="center"
								justify="center"
							>
								{index + 1}
							</Flex>
							<Text fontSize="sm" color="tiber.700" lineHeight="1.6">
								{step}
							</Text>
						</Flex>
					))}
				</VStack>
			</Collapse>
		</Box>
	);
};

export default ExerciseInstruction;
