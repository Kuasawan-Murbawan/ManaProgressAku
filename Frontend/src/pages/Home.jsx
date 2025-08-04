import {
  Center,
  Container,
  HStack,
  Text,
  useDisclosure,
  VStack,
} from "@chakra-ui/react";
import React from "react";
import { useNavigate } from "react-router-dom";
import StartSessionDialog from "../components/StartSessionDialog";

const Home = () => {
  const navigate = useNavigate();

  // Create Session
  const {
    isOpen: confirmSessionIsOpen,
    onOpen: confirmSessionOnOpen,
    onClose: confirmSessionOnClose,
  } = useDisclosure();

  return (
    <Container maxW={"full"} bg={"green.200"}>
      <Container bg={"blue.100"}>
        <Center>
          <Text fontSize={"6xl"} fontWeight={"bold"}>
            Home
          </Text>
        </Center>
      </Container>

      <VStack spacing={"24px"} py={"20px"} bg={"blue.300"}>
        <Container
          bg={"gray.200"}
          borderRadius={"2xl"}
          py={"20px"}
          _hover={{ bg: "gray.300" }}
          onClick={confirmSessionOnOpen}
        >
          <Center>
            <Text
              alignItems={"center"}
              alignContent={"center"}
              fontSize={"3xl"}
              fontWeight={"bold"}
            >
              Start New Session
            </Text>
          </Center>
        </Container>
        <Container
          bg={"gray.200"}
          borderRadius={"2xl"}
          py={"20px"}
          _hover={{ bg: "gray.300" }}
        >
          <Center>
            <Text
              alignItems={"center"}
              alignContent={"center"}
              fontSize={"3xl"}
              fontWeight={"bold"}
              onClick={() => navigate("/pastSessions")}
            >
              Past Sessions
            </Text>
          </Center>
        </Container>
        <Container
          bg={"gray.300"}
          borderRadius={"2xl"}
          py={"20px"}
          _hover={{ bg: "gray.100" }}
          onClick={() => navigate("/exerciseList")}
        >
          <Center>
            <Text
              alignItems={"center"}
              alignContent={"center"}
              fontSize={"3xl"}
              fontWeight={"bold"}
            >
              Exercise List
            </Text>
          </Center>
        </Container>
      </VStack>

      <StartSessionDialog
        isOpen={confirmSessionIsOpen}
        onClose={confirmSessionOnClose}
      />
    </Container>
  );
};

export default Home;
