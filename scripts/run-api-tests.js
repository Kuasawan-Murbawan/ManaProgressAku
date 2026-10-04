const path = require("path");
const dotenv = require("dotenv");
const newman = require("newman");

const target = process.argv[2] === "prod" ? "prod" : "local";
const projectRoot = path.join(__dirname, "..");

dotenv.config({
	path: path.join(projectRoot, target === "prod" ? ".env.prod" : ".env.local"),
	quiet: true,
});

const required = ["ADMIN_EMAIL", "ADMIN_PW", "TEST_USER_EMAIL", "TEST_USER_PW"];
const missing = required.filter((key) => !process.env[key]);
if (missing.length > 0) {
	console.error(`Missing from the ${target} env file: ${missing.join(", ")}`);
	process.exit(1);
}

const envFile =
	target === "prod"
		? "Tests/API/Production/ManaProgressAku Prod - DANGER.postman_environment.json"
		: "Tests/API/Local/ManaProgressAku Local - safe.postman_environment.json";

newman.run(
	{
		collection: require(
			path.join(
				projectRoot,
				"Tests/API/Local/MPA Smoke Test - Local [v1.5.0].postman_collection.json",
			),
		),
		environment: require(path.join(projectRoot, envFile)),
		envVar: [
			{ key: "adminEmail", value: process.env.ADMIN_EMAIL },
			{ key: "adminPw", value: process.env.ADMIN_PW },
			{ key: "testUserEmail", value: process.env.TEST_USER_EMAIL },
			{ key: "testUserPw", value: process.env.TEST_USER_PW },
		],
		reporters: "cli",
	},
	(err) => {
		if (err) {
			throw err;
		}
		console.log("Collection run complete.");
	},
);
