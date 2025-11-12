import { Box, Button, Center, Text, VStack } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SessionDetailsComponent from "../components/SessionDetailsComponent";
import { useSessionStore } from "../store/session";

const PastSessionsPage = () => {
  const navigate = useNavigate();

  const { sessions, fetchUserSessions } = useSessionStore();

  useEffect(() => {
    fetchUserSessions();
  }, [fetchUserSessions]);

  // Sort sessions by most recent date/time first
  const sortedSessions = [...sessions].sort((a, b) => {
    const dateA = new Date(`${a.date} ${a.time}`);
    const dateB = new Date(`${b.date} ${b.time}`);
    return dateB - dateA; // descending (latest first)
  });

  return (
    <Box
      minH="100vh"
      bgGradient="linear(to-b, purple.50, pink.50)"
      py={10}
      px={4}
    >
      {/* Page Title */}
      <Center mb={8}>
        <Text
          fontSize="4xl"
          fontWeight="extrabold"
          color="purple.700"
          textShadow="1px 1px 2px rgba(0,0,0,0.1)"
        >
          📖 Past Sessions
        </Text>
      </Center>

      {/* Session List */}
      <VStack spacing={6} align="stretch">
        {sortedSessions.length > 0 ? (
          sortedSessions.map((session) => (
            <SessionDetailsComponent
              key={session.sessionID}
              sessionID={session.sessionID}
              date={session.date}
              time={session.time}
            />
          ))
        ) : (
          <Center>
            <Text fontSize="lg" color="gray.500">
              No past sessions yet ✨
            </Text>
          </Center>
        )}
      </VStack>

      {/* Back Button */}
      <Center mt={10}>
        <Button
          size="lg"
          bg="pink.200"
          _hover={{ bg: "pink.300", transform: "scale(1.05)" }}
          borderRadius="xl"
          shadow="md"
          onClick={() => navigate("/")}
        >
          ⬅ Home
        </Button>
      </Center>
    </Box>
  );
};

export default PastSessionsPage;
