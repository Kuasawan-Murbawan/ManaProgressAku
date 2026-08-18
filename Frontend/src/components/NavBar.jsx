import React from "react";
import {
	Box,
	Flex,
	Text,
	Icon,
	useDisclosure,
	useBreakpointValue,
} from "@chakra-ui/react";
import { useNavigate, useLocation } from "react-router-dom";
import { useSessionStore } from "../store/session";
import StartSessionDialog from "./Session/StartSessionDialog";

// Minimal inline icon set — no extra icon package dependency, same pattern
// used elsewhere in the app (see the placeholder icon in CurrentActivityPage).
const HomeIcon = (props) => (
	<Icon viewBox="0 0 24 24" {...props}>
		<path fill="currentColor" d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z" />
	</Icon>
);

const ExerciseIcon = (props) => (
	<Icon viewBox="0 0 24 24" {...props}>
		<path
			fill="currentColor"
			d="M20.57 14.86L22 13.43 20.57 12 17 15.57 8.43 7 12 3.43 10.57 2 9.14 3.43 7.71 2 5.57 4.14 4.14 2.71 2.71 4.14l1.43 1.43L2 7.71l1.43 1.43L2 10.57 3.43 12 7 8.43 15.57 17 12 20.57 13.43 22l1.43-1.43L16.29 22l2.14-2.14 1.43 1.43 1.43-1.43-1.43-1.43L22 16.29l-1.43-1.43z"
		/>
	</Icon>
);

const HistoryIcon = (props) => (
	<Icon viewBox="0 0 24 24" {...props}>
		<path
			fill="currentColor"
			d="M13 3a9 9 0 0 0-9 9H1l3.89 3.89.07.14L9 12H6a7 7 0 1 1 2.05 4.95l-1.42 1.42A9 9 0 1 0 13 3zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8z"
		/>
	</Icon>
);

const ProfileIcon = (props) => (
	<Icon viewBox="0 0 24 24" {...props}>
		<path
			fill="currentColor"
			d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"
		/>
	</Icon>
);

const PlayIcon = (props) => (
	<Icon viewBox="0 0 24 24" {...props}>
		<path fill="currentColor" d="M8 5v14l11-7z" />
	</Icon>
);

const NAV_ITEMS = [
	{ label: "Home", path: "/", icon: HomeIcon },
	{ label: "Exercises", path: "/exerciseList", icon: ExerciseIcon },
	{ label: "Sessions", path: "/pastSessions", icon: HistoryIcon },
	{ label: "Profile", path: "/profile", icon: ProfileIcon }, // TODO: 404s until the profile page ships
];

const NavBar = () => {
	const navigate = useNavigate();
	const location = useLocation();
	const { activeSession } = useSessionStore();
	const isDesktop = useBreakpointValue({ base: false, md: true });

	const {
		isOpen: startIsOpen,
		onOpen: startOnOpen,
		onClose: startOnClose,
	} = useDisclosure();

	const handleStartClick = async () => {
		const hasActive = await activeSession();
		if (hasActive) {
			navigate("/createSession");
		} else {
			startOnOpen();
		}
	};

	const isActive = (path) => location.pathname === path;

	// ---- Desktop: fixed top bar ----
	if (isDesktop) {
		return (
			<>
				<Flex
					as="nav"
					position="fixed"
					top={0}
					left={0}
					right={0}
					zIndex={20}
					h="64px"
					bg="white"
					borderBottom="1px solid"
					borderColor="mist.200"
					align="center"
					px={8}
				>
					{/* <Text
						fontFamily="heading"
						fontWeight="700"
						fontSize="lg"
						color="tiber.800"
						cursor="pointer"
						onClick={() => navigate("/")}
						mr={10}
					>
						Mana Progress Aku
					</Text> */}

					<Flex flex="1" gap={2}>
						{NAV_ITEMS.slice(0, 2).map((item) => (
							<NavLinkDesktop
								key={item.path}
								item={item}
								active={isActive(item.path)}
								onClick={() => navigate(item.path)}
							/>
						))}
					</Flex>

					<Flex
						align="center"
						gap={2}
						bg="lime.400"
						color="tiber.900"
						fontWeight="700"
						fontSize="sm"
						borderRadius="lg"
						px={4}
						py={2}
						cursor="pointer"
						_hover={{ bg: "lime.300" }}
						onClick={handleStartClick}
					>
						<Icon as={PlayIcon} boxSize={4} />
						Start Session
					</Flex>

					<Flex flex="1" justify="flex-end" gap={2}>
						{NAV_ITEMS.slice(2).map((item) => (
							<NavLinkDesktop
								key={item.path}
								item={item}
								active={isActive(item.path)}
								onClick={() => navigate(item.path)}
							/>
						))}
					</Flex>
				</Flex>

				<StartSessionDialog isOpen={startIsOpen} onClose={startOnClose} />
			</>
		);
	}

	// ---- Mobile: fixed bottom tab bar with elevated Start action ----
	const [leftItems, rightItems] = [NAV_ITEMS.slice(0, 2), NAV_ITEMS.slice(2)];

	return (
		<>
			<Flex
				as="nav"
				position="fixed"
				bottom={0}
				left={0}
				right={0}
				zIndex={20}
				h="64px"
				bg="white"
				borderTop="1px solid"
				borderColor="mist.200"
				align="center"
				justify="space-around"
				px={2}
				pb="env(safe-area-inset-bottom)"
			>
				{leftItems.map((item) => (
					<NavLinkMobile
						key={item.path}
						item={item}
						active={isActive(item.path)}
						onClick={() => navigate(item.path)}
					/>
				))}

				{/* Elevated center action */}
				<Box position="relative" w="64px">
					<Flex
						position="absolute"
						top="-28px"
						left="50%"
						transform="translateX(-50%)"
						boxSize="56px"
						borderRadius="full"
						bg="lime.400"
						align="center"
						justify="center"
						boxShadow="0 4px 12px rgba(6,53,55,0.25)"
						border="4px solid white"
						cursor="pointer"
						_hover={{ bg: "lime.300" }}
						onClick={handleStartClick}
					>
						<Icon as={PlayIcon} boxSize={5} color="tiber.900" />
					</Flex>
				</Box>

				{rightItems.map((item) => (
					<NavLinkMobile
						key={item.path}
						item={item}
						active={isActive(item.path)}
						onClick={() => navigate(item.path)}
					/>
				))}
			</Flex>

			<StartSessionDialog isOpen={startIsOpen} onClose={startOnClose} />
		</>
	);
};

const NavLinkMobile = ({ item, active, onClick }) => (
	<Flex
		direction="column"
		align="center"
		justify="center"
		flex="1"
		py={1}
		cursor="pointer"
		onClick={onClick}
	>
		<Icon
			as={item.icon}
			boxSize={5}
			color={active ? "tiber.800" : "tiber.400"}
			mb={0.5}
		/>
		<Text
			fontSize="2xs"
			fontWeight={active ? "700" : "500"}
			color={active ? "tiber.800" : "tiber.400"}
		>
			{item.label}
		</Text>
		<Box
			mt={0.5}
			boxSize="4px"
			borderRadius="full"
			bg={active ? "lime.400" : "transparent"}
		/>
	</Flex>
);

const NavLinkDesktop = ({ item, active, onClick }) => (
	<Flex
		align="center"
		gap={2}
		px={3}
		py={2}
		borderRadius="lg"
		cursor="pointer"
		bg={active ? "mist.400" : "transparent"}
		_hover={{ bg: "mist.400" }}
		onClick={onClick}
	>
		<Icon
			as={item.icon}
			boxSize={4}
			color={active ? "tiber.800" : "tiber.500"}
		/>
		<Text
			fontSize="sm"
			fontWeight={active ? "700" : "500"}
			color={active ? "tiber.800" : "tiber.600"}
		>
			{item.label}
		</Text>
	</Flex>
);

export default NavBar;
