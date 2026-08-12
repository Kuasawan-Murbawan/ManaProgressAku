// RegisterPage.jsx
import React, { useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";
import {
	Box,
	Button,
	Input,
	FormControl,
	FormLabel,
	Text,
	VStack,
	Alert,
	AlertIcon,
} from "@chakra-ui/react";
import { useAuthStore } from "../store/auth";

const RegisterPage = () => {
	const [name, setName] = useState("");
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [confPass, setConfPass] = useState("");
	const [error, setError] = useState("");
	const navigate = useNavigate();

	const setToken = useAuthStore((state) => state.setToken);

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
			setToken(token);

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
			bg="mist.400"
			px={4}
		>
			<Box
				bg="white"
				borderRadius="xl"
				boxShadow="sm"
				p={{ base: 6, md: 8 }}
				w="100%"
				maxW="400px"
			>
				<Text
					fontFamily="heading"
					fontSize="2xl"
					fontWeight="700"
					color="tiber.800"
					textAlign="center"
					mb={6}
				>
					Create account
				</Text>

				{error && (
					<Alert status="error" borderRadius="md" fontSize="sm" mb={4}>
						<AlertIcon />
						{error}
					</Alert>
				)}

				<form onSubmit={handleRegister}>
					<VStack spacing={4} align="stretch">
						<FormControl>
							<FormLabel fontSize="sm" color="tiber.700">
								Name
							</FormLabel>
							<Input
								type="text"
								value={name}
								placeholder="Enter your name"
								onChange={(e) => setName(e.target.value)}
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_hover={{ borderColor: "tiber.400" }}
								_focus={{
									borderColor: "tiber.600",
									boxShadow: "0 0 0 1px #146059",
								}}
							/>
						</FormControl>

						<FormControl>
							<FormLabel fontSize="sm" color="tiber.700">
								Email
							</FormLabel>
							<Input
								type="email"
								value={email}
								placeholder="Enter your email"
								onChange={(e) => setEmail(e.target.value)}
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_hover={{ borderColor: "tiber.400" }}
								_focus={{
									borderColor: "tiber.600",
									boxShadow: "0 0 0 1px #146059",
								}}
							/>
						</FormControl>

						<FormControl>
							<FormLabel fontSize="sm" color="tiber.700">
								Password
							</FormLabel>
							<Input
								type="password"
								value={password}
								placeholder="Enter password"
								onChange={(e) => setPassword(e.target.value)}
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_hover={{ borderColor: "tiber.400" }}
								_focus={{
									borderColor: "tiber.600",
									boxShadow: "0 0 0 1px #146059",
								}}
							/>
						</FormControl>

						<FormControl>
							<FormLabel fontSize="sm" color="tiber.700">
								Confirm password
							</FormLabel>
							<Input
								type="password"
								value={confPass}
								placeholder="Confirm password"
								onChange={(e) => setConfPass(e.target.value)}
								bg="white"
								borderColor="mist.300"
								borderRadius="lg"
								_hover={{ borderColor: "tiber.400" }}
								_focus={{
									borderColor: "tiber.600",
									boxShadow: "0 0 0 1px #146059",
								}}
							/>
						</FormControl>

						<Button
							type="submit"
							bg="lime.400"
							color="tiber.900"
							w="100%"
							mt={2}
							_hover={{ bg: "lime.300" }}
						>
							Create account
						</Button>
					</VStack>
				</form>

				<Text
					textAlign="center"
					fontSize="sm"
					color="tiber.700"
					opacity={0.7}
					mt={5}
				>
					Already have an account?{" "}
					<Text
						as="span"
						color="tiber.800"
						fontWeight="700"
						cursor="pointer"
						onClick={() => navigate("/login")}
						_hover={{ textDecoration: "underline" }}
					>
						Log in
					</Text>
				</Text>
			</Box>
		</Box>
	);
};

export default RegisterPage;
