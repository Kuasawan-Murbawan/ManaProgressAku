// components/Profile/ChangePasswordCard.jsx
import { useEffect, useMemo, useRef, useState } from "react";
import {
	Alert,
	AlertIcon,
	Box,
	Button,
	Collapse,
	Flex,
	FormControl,
	FormErrorIcon,
	FormErrorMessage,
	FormLabel,
	HStack,
	IconButton,
	Input,
	InputGroup,
	InputRightElement,
	Text,
	VStack,
	useDisclosure,
	useToast,
} from "@chakra-ui/react";
import {
	CheckCircleIcon,
	ChevronDownIcon,
	LockIcon,
	ViewIcon,
	ViewOffIcon,
} from "@chakra-ui/icons";
import { useProfileStore } from "../../store/profile";
import { useAuthStore } from "../../store/auth";

// Frontend rules. Change these numbers here if the backend rules change.
// BCrypt only uses the first 72 bytes of a password, so longer is pointless.
const MIN_LENGTH = 8;
const MAX_LENGTH = 72;

const FIELDS = ["currentPassword", "newPassword", "confirmNewPassword"];
const EMPTY = { currentPassword: "", newPassword: "", confirmNewPassword: "" };

const validate = ({ currentPassword, newPassword, confirmNewPassword }) => {
	const errors = {};

	if (!currentPassword) errors.currentPassword = "Enter your current password";

	if (!newPassword) errors.newPassword = "Enter a new password";
	else if (newPassword.length < MIN_LENGTH)
		errors.newPassword = `Use at least ${MIN_LENGTH} characters`;
	else if (newPassword.length > MAX_LENGTH)
		errors.newPassword = `Use ${MAX_LENGTH} characters or fewer`;
	else if (currentPassword && newPassword === currentPassword)
		errors.newPassword = "New password must be different from your current one";

	if (!confirmNewPassword)
		errors.confirmNewPassword = "Confirm your new password";
	else if (newPassword !== confirmNewPassword)
		errors.confirmNewPassword = "Passwords do not match";

	return errors;
};

const STRENGTH = [
	{ label: "Too short", color: "red.400" },
	{ label: "Weak", color: "red.400" },
	{ label: "Fair", color: "orange.400" },
	{ label: "Good", color: "yellow.500" },
	{ label: "Strong", color: "green.500" },
];

const getStrength = (pw) => {
	if (pw.length < MIN_LENGTH) return 0;
	let score = 1;
	if (pw.length >= 12) score++;
	if (/[a-z]/.test(pw) && /[A-Z]/.test(pw)) score++;
	if (/\d/.test(pw) && /[^A-Za-z0-9]/.test(pw)) score++;
	return score;
};

const StrengthMeter = ({ password }) => {
	const score = getStrength(password);
	const { label, color } = STRENGTH[score];
	return (
		<Box mt={2} aria-live="polite">
			<HStack spacing={1}>
				{[1, 2, 3, 4].map((n) => (
					<Box
						key={n}
						flex="1"
						h="4px"
						borderRadius="full"
						bg={n <= score ? color : "mist.300"}
						transition="background 0.2s"
					/>
				))}
			</HStack>
			<Text mt={1} fontSize="xs" color={color} fontWeight="600">
				{label}
			</Text>
		</Box>
	);
};

const Requirement = ({ met, children }) => (
	<HStack
		spacing={2}
		color={met ? "green.600" : "tiber.700"}
		opacity={met ? 1 : 0.7}
	>
		{met ? (
			<CheckCircleIcon boxSize={3.5} />
		) : (
			<Box
				boxSize={3.5}
				display="flex"
				alignItems="center"
				justifyContent="center"
			>
				<Box boxSize="6px" borderRadius="full" bg="mist.300" />
			</Box>
		)}
		<Text fontSize="xs">{children}</Text>
	</HStack>
);

const PasswordField = ({
	label,
	value,
	onChange,
	onBlur,
	error,
	inputRef,
	autoComplete,
	isDisabled,
	children,
}) => {
	const [show, setShow] = useState(false);
	return (
		<FormControl isInvalid={!!error} isDisabled={isDisabled}>
			<FormLabel fontSize="sm" color="tiber.700">
				{label}
			</FormLabel>
			<InputGroup>
				<Input
					ref={inputRef}
					type={show ? "text" : "password"}
					value={value}
					onChange={onChange}
					onBlur={onBlur}
					autoComplete={autoComplete}
					bg="white"
					borderColor="mist.300"
					borderRadius="lg"
					pr="2.75rem"
					_hover={{ borderColor: "tiber.400" }}
					_focus={{
						borderColor: "tiber.600",
						boxShadow: "0 0 0 1px #146059",
					}}
					_invalid={{
						borderColor: "red.400",
						boxShadow: "0 0 0 1px var(--chakra-colors-red-400)",
					}}
				/>
				<InputRightElement>
					<IconButton
						type="button"
						size="sm"
						variant="ghost"
						color="tiber.700"
						aria-label={show ? `Hide ${label}` : `Show ${label}`}
						icon={show ? <ViewOffIcon /> : <ViewIcon />}
						onClick={() => setShow((s) => !s)}
						isDisabled={isDisabled}
					/>
				</InputRightElement>
			</InputGroup>
			<FormErrorMessage>
				<FormErrorIcon />
				{error}
			</FormErrorMessage>
			{children}
		</FormControl>
	);
};

const ChangePasswordCard = () => {
	const changePassword = useProfileStore((s) => s.changePassword);
	const logout = useAuthStore((s) => s.logout);
	const toast = useToast();
	const { isOpen, onToggle } = useDisclosure();

	const [values, setValues] = useState(EMPTY);
	const [touched, setTouched] = useState({});
	const [serverErrors, setServerErrors] = useState({});
	const [formError, setFormError] = useState("");
	const [isSubmitting, setIsSubmitting] = useState(false);
	const [isDone, setIsDone] = useState(false);

	const refs = {
		currentPassword: useRef(null),
		newPassword: useRef(null),
		confirmNewPassword: useRef(null),
	};
	const timer = useRef(null);
	useEffect(() => () => clearTimeout(timer.current), []);

	const clientErrors = useMemo(() => validate(values), [values]);

	// A message from the server wins over the local one until the user edits the field.
	const errorFor = (field) =>
		serverErrors[field] || (touched[field] ? clientErrors[field] : undefined);

	// Which server errors become stale when a field changes.
	// "Same as old password" depends on both fields, "mismatch" on new + confirm.
	const STALE = {
		currentPassword: ["currentPassword", "newPassword"],
		newPassword: ["newPassword", "confirmNewPassword"],
		confirmNewPassword: ["confirmNewPassword"],
	};

	const handleChange = (field) => (e) => {
		const value = e.target.value;
		setValues((v) => ({ ...v, [field]: value }));
		setFormError("");
		setServerErrors((prev) => {
			const next = { ...prev };
			STALE[field].forEach((f) => delete next[f]);
			return next;
		});
	};

	const handleBlur = (field) => () =>
		setTouched((t) => ({ ...t, [field]: true }));

	const focusField = (field, select = false) => {
		const el = refs[field].current;
		if (!el) return;
		el.focus();
		if (select) el.select();
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		if (isSubmitting || isDone) return;

		setFormError("");
		setTouched({
			currentPassword: true,
			newPassword: true,
			confirmNewPassword: true,
		});

		const firstInvalid = FIELDS.find((f) => clientErrors[f]);
		if (firstInvalid) {
			focusField(firstInvalid);
			return;
		}

		setIsSubmitting(true);
		const result = await changePassword(values);
		setIsSubmitting(false);

		if (result.success) {
			setIsDone(true);
			setValues(EMPTY);
			setTouched({});
			setServerErrors({});
			toast({
				title: "Password changed",
				description: "Please log in again with your new password.",
				status: "success",
				duration: 4000,
				isClosable: true,
			});
			// The old token stops working after a password change, so log out cleanly.
			timer.current = setTimeout(() => logout(), 1800);
			return;
		}

		// Show each backend message under its own input.
		const fromServer = {};
		FIELDS.forEach((f) => {
			if (result.fieldErrors?.[f]) fromServer[f] = result.fieldErrors[f];
		});
		setServerErrors(fromServer);

		const firstServer = FIELDS.find((f) => fromServer[f]);
		if (firstServer) {
			focusField(firstServer, firstServer === "currentPassword");
		} else {
			// Network problem or an error that is not tied to one field.
			setFormError(result.message);
		}
	};

	const resetForm = () => {
		setValues(EMPTY);
		setTouched({});
		setServerErrors({});
		setFormError("");
	};

	// Closing the panel throws away anything half typed, so passwords never linger on screen.
	const handleToggle = () => {
		if (isSubmitting || isDone) return;
		if (isOpen) resetForm();
		onToggle();
	};

	// Put the cursor in the first field once the panel has opened.
	useEffect(() => {
		if (!isOpen) return undefined;
		const id = setTimeout(() => refs.currentPassword.current?.focus(), 250);
		return () => clearTimeout(id);
	}, [isOpen]);

	const hasNew = values.newPassword.length > 0;
	// Inputs stay enabled while the request runs, so focus can move to the field
	// with the error as soon as the response arrives. Double submits are blocked in handleSubmit.
	const locked = isDone;

	return (
		<Box
			as="form"
			onSubmit={handleSubmit}
			noValidate
			bg="white"
			borderRadius="xl"
			boxShadow="sm"
			p={{ base: 5, md: 6 }}
			mb={6}
		>
			<Flex
				as="button"
				type="button"
				onClick={handleToggle}
				aria-expanded={isOpen}
				aria-controls="change-password-panel"
				w="100%"
				align="center"
				gap={3}
				textAlign="left"
				borderRadius="lg"
				cursor={isSubmitting || isDone ? "default" : "pointer"}
				_focusVisible={{ boxShadow: "0 0 0 2px #146059" }}
			>
				<Flex
					boxSize="40px"
					borderRadius="lg"
					bg="lime.400"
					color="tiber.900"
					align="center"
					justify="center"
					flexShrink={0}
				>
					<LockIcon />
				</Flex>
				<Box flex="1" minW={0}>
					<Text
						fontFamily="heading"
						fontWeight="700"
						fontSize="lg"
						color="tiber.800"
					>
						Change password
					</Text>
					<Text fontSize="sm" color="tiber.700" opacity={0.7}>
						{isOpen
							? "You will be signed out and asked to log in again."
							: "Update the password you use to log in."}
					</Text>
				</Box>
				<ChevronDownIcon
					boxSize={6}
					color="tiber.700"
					flexShrink={0}
					transform={isOpen ? "rotate(180deg)" : "rotate(0deg)"}
					transition="transform 0.2s ease"
				/>
			</Flex>

			<Collapse in={isOpen} animateOpacity unmountOnExit>
				<Box id="change-password-panel" pt={5}>
					<VStack spacing={4} align="stretch">
						{formError && (
							<Alert status="error" borderRadius="md" fontSize="sm">
								<AlertIcon />
								{formError}
							</Alert>
						)}

						{isDone && (
							<Alert status="success" borderRadius="md" fontSize="sm">
								<AlertIcon />
								Password changed. Taking you to the login page...
							</Alert>
						)}

						<PasswordField
							label="Current password"
							value={values.currentPassword}
							onChange={handleChange("currentPassword")}
							onBlur={handleBlur("currentPassword")}
							error={errorFor("currentPassword")}
							inputRef={refs.currentPassword}
							autoComplete="current-password"
							isDisabled={locked}
						/>

						<PasswordField
							label="New password"
							value={values.newPassword}
							onChange={handleChange("newPassword")}
							onBlur={handleBlur("newPassword")}
							error={errorFor("newPassword")}
							inputRef={refs.newPassword}
							autoComplete="new-password"
							isDisabled={locked}
						>
							{hasNew && <StrengthMeter password={values.newPassword} />}
						</PasswordField>

						<PasswordField
							label="Confirm new password"
							value={values.confirmNewPassword}
							onChange={handleChange("confirmNewPassword")}
							onBlur={handleBlur("confirmNewPassword")}
							error={errorFor("confirmNewPassword")}
							inputRef={refs.confirmNewPassword}
							autoComplete="new-password"
							isDisabled={locked}
						/>

						{hasNew && (
							<VStack
								align="stretch"
								spacing={1.5}
								bg="mist.400"
								borderRadius="lg"
								px={4}
								py={3}
							>
								<Requirement met={values.newPassword.length >= MIN_LENGTH}>
									At least {MIN_LENGTH} characters
								</Requirement>
								<Requirement
									met={
										values.currentPassword.length > 0 &&
										values.newPassword !== values.currentPassword
									}
								>
									Different from your current password
								</Requirement>
								<Requirement
									met={
										values.confirmNewPassword.length > 0 &&
										values.newPassword === values.confirmNewPassword
									}
								>
									Matches the confirmation
								</Requirement>
							</VStack>
						)}

						<Button
							type="submit"
							bg="lime.400"
							color="tiber.900"
							_hover={{ bg: "lime.300" }}
							isLoading={isSubmitting}
							loadingText="Changing password"
							isDisabled={isDone}
							mt={1}
						>
							Change password
						</Button>
					</VStack>
				</Box>
			</Collapse>
		</Box>
	);
};

export default ChangePasswordCard;
