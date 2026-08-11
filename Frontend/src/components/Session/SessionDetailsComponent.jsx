import React from "react";
import { Box, Text, VStack } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import { formatDistanceToNowStrict } from "date-fns";

const SessionDetailsComponent = ({ sessionID, date, time }) => {
	const navigate = useNavigate();
	const title = formatDistanceToNowStrict(new Date(`${date} ${time}`), {
		addSuffix: true,
	});

	const handleClick = () => {
		navigate(`/session/${sessionID}`);
	};

	return (
		<Box
			p={{ base: 4, md: 5 }}
			width="100%"
			bg="white"
			borderRadius="xl"
			borderLeft="5px solid"
			borderLeftColor="tiber.600"
			boxShadow="sm"
			cursor="pointer"
			transition="all 0.2s ease"
			_hover={{
				transform: "translateY(-2px)",
				boxShadow: "md",
				borderLeftColor: "lime.400",
			}}
			onClick={handleClick}
		>
			<VStack align="start" spacing={1}>
				<Text
					fontWeight="700"
					fontSize={{ base: "md", md: "lg" }}
					color="tiber.800"
				>
					{title}
				</Text>

				<Text fontSize="sm" color="tiber.900" opacity={0.65}>
					📅 {date}
				</Text>

				<Text fontSize="sm" color="tiber.900" opacity={0.65}>
					⏰ {time}
				</Text>
			</VStack>
		</Box>
	);
};

export default SessionDetailsComponent;
