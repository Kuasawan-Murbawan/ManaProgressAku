// LoginPage.jsx
import { useState } from "react";
import {
	Box,
	Input,
	Button,
	Text,
	VStack,
	Image,
	FormControl,
	FormLabel,
	Alert,
	AlertIcon,
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
			minH="100vh"
			display="flex"
			alignItems="center"
			justifyContent="center"
			bg="mist.400"
			px={4}
		>
			<VStack spacing={6} w="100%" maxW="380px">
				{/* Logo */}
				<Image
					src="/logo2.png"
					alt="Mana Progress Aku Logo"
					maxW="200px"
					objectFit="contain"
				/>

				{/* Login Card */}
				<Box
					bg="white"
					borderRadius="xl"
					boxShadow="sm"
					p={{ base: 6, md: 8 }}
					w="100%"
				>
					<Text
						fontFamily="heading"
						fontSize="2xl"
						fontWeight="700"
						color="tiber.800"
						textAlign="center"
						mb={6}
					>
						Log in
					</Text>

					<form onSubmit={handleLogin} style={{ width: "100%" }}>
						<VStack spacing={4} align="stretch">
							{error && (
								<Alert status="error" borderRadius="md" fontSize="sm">
									<AlertIcon />
									{error}
								</Alert>
							)}

							<FormControl>
								<FormLabel fontSize="sm" color="tiber.700">
									Email
								</FormLabel>
								<Input
									type="email"
									placeholder="you@example.com"
									value={email}
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
									placeholder="Enter your password"
									value={password}
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

							<Button
								type="submit"
								bg="tiber.800"
								color="white"
								w="full"
								mt={2}
								_hover={{ bg: "tiber.900" }}
							>
								Log in
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
						Don't have an account?{" "}
						<Text
							as="span"
							color="tiber.800"
							fontWeight="700"
							cursor="pointer"
							onClick={() => navigate("/register")}
							_hover={{ textDecoration: "underline" }}
						>
							Sign up
						</Text>
					</Text>
				</Box>
			</VStack>
		</Box>
	);
}
