import React from "react";
import { Box, Text, VStack } from "@chakra-ui/react";

const SessionDetailsComponent = ({ sessionID, date, time }) => {
  return (
    <Box
      p={4}
      borderWidth={1}
      borderRadius="lg"
      shadow="md"
      width="100%"
      maxW="500px"
      bg={"green.100"}
    >
      <VStack align="start" spacing={1}>
        <Text fontWeight="bold">Session ID: {sessionID}</Text>
        <Text>Date: {date}</Text>
        <Text>Time: {time}</Text>
      </VStack>
    </Box>
  );
};

export default SessionDetailsComponent;
