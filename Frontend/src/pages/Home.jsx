import {
  Box,
  Center,
  Container,
  Text,
  useDisclosure,
  VStack,
} from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import StartSessionDialog from "../components/StartSessionDialog";
import { useExerciseStore } from "../store/exercise";
import { useActivityStore } from "../store/activity";
import ConfirmLogoutDialog from "../components/ConfirmLogoutDialog";

const Home = () => {
  const navigate = useNavigate();

  const { exercise, fetchAllExercises } = useExerciseStore();
  const { clearActivities } = useActivityStore();

  useEffect(() => {
    clearActivities(); // clear activities after finishing session
    if (exercise.length === 0) {
      fetchAllExercises();
    }
  }, []);

  // Create Session
  const {
    isOpen: confirmSessionIsOpen,
    onOpen: confirmSessionOnOpen,
    onClose: confirmSessionOnClose,
  } = useDisclosure();

  // Log Out
  const {
    isOpen: confirmLogoutIsOpen,
    onOpen: confirmLogoutOnOpen,
    onClose: confirmLogoutOnClose,
  } = useDisclosure();

  // Reusable Menu Card
  const MenuCard = ({ text, bg, hoverBg, onClick }) => (
    <Box
      w="full"
      maxW="md"
      bg={bg}
      borderRadius="2xl"
      py={6}
      px={4}
      shadow="md"
      cursor="pointer"
      transition="all 0.2s"
      _hover={{
        transform: "scale(1.05)",
        bg: hoverBg,
        shadow: "xl",
      }}
      onClick={onClick}
    >
      <Center>
        <Text fontSize="2xl" fontWeight="bold" color="gray.700">
          {text}
        </Text>
      </Center>
    </Box>
  );

  return (
    <Box minH="100vh" bgGradient="linear(to-b, green.100, blue.50)">
      {/* Header */}
      <Container maxW="full" py={10}>
        <Center>
          <Text
            fontSize="5xl"
            fontWeight="extrabold"
            color="teal.700"
            textShadow="1px 1px 2px rgba(0,0,0,0.1)"
          >
            🏋️ Mana Progress Aku
          </Text>
        </Center>
      </Container>

      {/* Menu Options */}
      <VStack spacing={8} py={10}>
        <MenuCard
          text="✨ Start New Session"
          bg="pink.100"
          hoverBg="pink.200"
          onClick={confirmSessionOnOpen}
        />
        <MenuCard
          text="📖 Past Sessions"
          bg="purple.100"
          hoverBg="purple.200"
          onClick={() => navigate("/pastSessions")}
        />
        <MenuCard
          text="📋 Exercise List"
          bg="teal.100"
          hoverBg="teal.200"
          onClick={() => navigate("/exerciseList")}
        />

        <MenuCard
          text={"Log Out"}
          bg={"red.400"}
          hoverBg={"red.200"}
          onClick={confirmLogoutOnOpen}
        />
      </VStack>

      {/* Start Session Dialog */}
      <StartSessionDialog
        isOpen={confirmSessionIsOpen}
        onClose={confirmSessionOnClose}
      />

      <ConfirmLogoutDialog
        isOpen={confirmLogoutIsOpen}
        onClose={confirmLogoutOnClose}
      />
    </Box>
  );
};

export default Home;
