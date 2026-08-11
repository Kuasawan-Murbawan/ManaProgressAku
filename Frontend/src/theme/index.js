import { extendTheme } from "@chakra-ui/react";

const theme = extendTheme({
	colors: {
		tiber: {
			50: "#EAF4F3",
			100: "#D4E9E7",
			200: "#A9D3D0",
			300: "#7EBDB9",
			400: "#53A7A2",
			500: "#2A7D75",
			600: "#146059",
			700: "#0D4B48",
			800: "#063537",
			900: "#03201F",
		},

		lime: {
			50: "#F8FCE5",
			100: "#F1F9CB",
			200: "#E4F397",
			300: "#D8EE6B",
			400: "#CCEE44",
			500: "#B5D52F",
			600: "#8FAA25",
			700: "#6A801C",
			800: "#465613",
			900: "#232B0A",
		},

		mist: {
			50: "#F7FAF9",
			100: "#EEF4F2",
			200: "#E1EBE8",
			300: "#D2DFDC",
			400: "#B8CBC7",
			500: "#9DB5B0",
			600: "#7C9993",
			700: "#5C7B75",
			800: "#3D5D57",
			900: "#203F3B",
		},
	},

	fonts: {
		heading: "Space Grotesk, sans-serif",
		body: "Inter, sans-serif",
	},
});

export default theme;
