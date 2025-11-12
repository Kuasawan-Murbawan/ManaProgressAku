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
    e.preventDefault();

    localStorage.removeItem("token"); // clear previous token

    console.log("Login payload", { email, password });

    try {
      const response = await API.post("/auth/login", { email, password });

      const token = response.data.token;

      // store token
      localStorage.setItem("token", token);

      // redirect to home
      navigate("/", { replace: true }); // make sure when user hit back, it doesnt go to login page
    } catch (err) {
      setError("Invalid email or password");
    }
  };

  return (
    <Box
      w="100vw"
      h="100vh"
      display="flex"
      alignItems="center"
      justifyContent="center"
      bg="gray.50"
    >
      <VStack
        spacing={4}
        p={8}
        borderWidth={1}
        borderRadius="lg"
        bg="white"
        boxShadow="lg"
      >
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
        <Button colorScheme="teal" onClick={handleLogin} width="full">
          Login
        </Button>
      </VStack>
    </Box>
  );
}
