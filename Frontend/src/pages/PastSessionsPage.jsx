import { Button, Stack, Text, VStack } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import SessionDetailsComponent from "../components/SessionDetailsComponent";
import { useSessionStore } from "../store/session";

const PastSessionsPage = () => {
  const navigate = useNavigate();

  const { sessions, fetchAllSessions } = useSessionStore();

  useEffect(() => {
    fetchAllSessions();
  }, [fetchAllSessions]);

  return (
    <div>
      <Text fontSize="2xl" fontWeight="bold" mb={4}>
        Past Sessions
      </Text>
      <VStack spacing={4}>
        {sessions.map((session) => (
          <SessionDetailsComponent
            key={session.sessionID}
            sessionID={session.sessionID}
            date={session.date}
            time={session.time}
          />
        ))}
      </VStack>
      <Button bg={"red.200"} mt={6} onClick={() => navigate("/")}>
        Home
      </Button>
    </div>
  );
};

export default PastSessionsPage;
