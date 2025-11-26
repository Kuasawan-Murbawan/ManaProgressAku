import React, { useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  Input,
  FormControl,
  FormLabel,
  Heading,
  VStack,
  Text,
  Alert,
  AlertIcon,
} from "@chakra-ui/react";

const RegisterPage = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confPass, setConfPass] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      if (password !== confPass) {
        alert("Password must be the same!");
        return;
      }

      const response = await API.post("/auth/signup", {
        email,
        password,
        name,
      });

      const token = response.data.token;
      localStorage.setItem("token", token);

      navigate("/", { replace: true });
    } catch (err) {
      if (err.status === 400) {
        setError(err.response?.data?.errorMessage || "Invalid request");
      } else {
        setError("Server error, please try again later!");
      }
    }
  };

  return (
    <Box
      minH="100vh"
      display="flex"
      alignItems="center"
      justifyContent="center"
      bg="gray.100"
      px={4}
    >
      <Box
        bg="white"
        p={8}
        rounded="md"
        boxShadow="md"
        width="100%"
        maxW="400px"
      >
        <Heading size="lg" mb={6} textAlign="center">
          Create Account
        </Heading>

        {error && (
          <Alert status="error" mb={4}>
            <AlertIcon />
            {error}
          </Alert>
        )}

        <form onSubmit={handleRegister}>
          <VStack spacing={4}>
            <FormControl>
              <FormLabel>Name</FormLabel>
              <Input
                type="text"
                value={name}
                placeholder="Enter your name"
                onChange={(e) => setName(e.target.value)}
              />
            </FormControl>

            <FormControl>
              <FormLabel>Email</FormLabel>
              <Input
                type="email"
                value={email}
                placeholder="Enter your email"
                onChange={(e) => setEmail(e.target.value)}
              />
            </FormControl>

            <FormControl>
              <FormLabel>Password</FormLabel>
              <Input
                type="password"
                value={password}
                placeholder="Enter password"
                onChange={(e) => setPassword(e.target.value)}
              />
            </FormControl>

            <FormControl>
              <FormLabel>Confirm Password</FormLabel>
              <Input
                type="password"
                value={confPass}
                placeholder="Confirm password"
                onChange={(e) => setConfPass(e.target.value)}
              />
            </FormControl>

            <Button type="submit" colorScheme="teal" width="100%" mt={2}>
              Register
            </Button>
          </VStack>
        </form>
      </Box>
    </Box>
  );
};

export default RegisterPage;
