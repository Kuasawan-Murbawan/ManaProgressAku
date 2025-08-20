import React from "react";
import { Box, Text, VStack } from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";

const SessionDetailsComponent = ({ sessionID, date, time }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/session/${sessionID}`);
  };

  return (
    <Box
      p={5}
      borderRadius="2xl"
      shadow="lg"
      width="100%"
      maxW="500px"
      bgGradient="linear(to-r, teal.100, green.100)"
      cursor="pointer"
      transition="all 0.2s ease-in-out"
      _hover={{
        transform: "scale(1.03)",
        shadow: "xl",
        bgGradient: "linear(to-r, teal.200, green.200)",
      }}
      onClick={handleClick}
    >
      <VStack align="start" spacing={1}>
        <Text fontWeight="bold" fontSize="lg" color="teal.800">
          Session #{sessionID}
        </Text>
        <Text color="gray.700">📅 {date}</Text>
        <Text color="gray.700">⏰ {time}</Text>
      </VStack>
    </Box>
  );
};

export default SessionDetailsComponent;
