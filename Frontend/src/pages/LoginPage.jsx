import { useState } from "react";
import { Box, Input, Button, Heading, Text, VStack } from "@chakra-ui/react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault(); // prevent page reload

    localStorage.removeItem("token"); // clear previous token

    try {
      const response = await API.post("/auth/login", { email, password });

      const token = response.data.token;

      // store token
      localStorage.setItem("token", token);

      // redirect to home
      navigate("/", { replace: true }); // make sure when user hit back, it doesnt go to login page
    } catch (err) {
      if (err.response.status == 403) {
        setError("Invalid email or password");
      } else {
        setError("Issue with the server, please try again later!");
      }
    }
  };

  return (
    <Box
      w="100vw"
      h="100vh"
      display="flex"
      alignItems="center"
      justifyContent="center"
      bg="gray.300"
    >
      <VStack
        spacing={4}
        p={8}
        borderWidth={1}
        borderRadius="lg"
        bg="gray.200"
        boxShadow="lg"
      >
        <form onSubmit={handleLogin}>
          <VStack spacing={4}>
            <Heading size="lg">Login</Heading>
            <Input
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <Input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {error && <Text color="red.500">{error}</Text>}
            <Button type="submit" colorScheme="teal" width="full">
              Login
            </Button>
          </VStack>
        </form>

        <Button
          width="full"
          bg="blue.100"
          color="black" // default text color
          _hover={{
            shadow: "xl",
            transform: "scale(1.04)",
            transition: "0.1s ease-in-out",
            bg: "blue.900",
            color: "white", // text color on hover
          }}
          onClick={() => navigate("/register")}
        >
          Sign Up
        </Button>
      </VStack>
    </Box>
  );
}
