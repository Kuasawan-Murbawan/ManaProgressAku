import { useState } from "react";
import {
  Box,
  Input,
  Button,
  Heading,
  Text,
  VStack,
  Image,
} from "@chakra-ui/react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/auth";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const setToken = useAuthStore((state) => state.setToken);

  const handleLogin = async (e) => {
    e.preventDefault(); // prevent page reload

    try {
      const response = await API.post("/auth/login", { email, password });

      const token = response.data.token;

      // store token
      // localStorage.setItem("token", token);   - old implementation

      setToken(token);

      // redirect to home
      navigate("/", { replace: true }); // make sure when user hit back, it doesnt go to login page
    } catch (err) {
      if (err.status == 401) {
        console.log(err);
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
      <VStack spacing={0}>
        {/* Logo */}
        <Image
          src="/logo2.png"
          alt="Mana Progress Aku Logo"
          maxW="300px"
          objectFit="contain"
          _hover={{
            transform: "scale(1.02)",
          }}
          transition="0.2s"
          position="relative"
          top="-70px"
        />

        {/* Login Card */}
        <VStack
          spacing={4}
          p={8}
          borderWidth={1}
          borderRadius="lg"
          bg="gray.200"
          boxShadow="lg"
          w="340px"
          position="relative"
          top="-90px"
        >
          <form onSubmit={handleLogin} style={{ width: "100%" }}>
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
            color="black"
            _hover={{
              shadow: "xl",
              transform: "scale(1.04)",
              transition: "0.1s ease-in-out",
              bg: "blue.900",
              color: "white",
            }}
            onClick={() => navigate("/register")}
          >
            Sign Up
          </Button>
        </VStack>
      </VStack>
    </Box>
  );
}
