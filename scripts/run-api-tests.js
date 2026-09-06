// scripts/run-api-tests.js
const path = require("path");
require("dotenv").config({ path: path.join(__dirname, "..", ".env.local") });
const newman = require("newman");

const target = process.argv[2] || "local";

const projectRoot = path.join(__dirname, "..");

const envFile =
	target === "prod"
		? path.join(
				projectRoot,
				"Tests/API/Production/ManaProgressAku Prod - DANGER.postman_environment.json",
			)
		: path.join(
				projectRoot,
				"Tests/API/Local/ManaProgressAku Local - safe.postman_environment.json",
			);

const collectionFile = path.join(
	projectRoot,
	"Tests/API/Local/MPA Smoke Test - Local [v1.2.0].postman_collection.json",
);

newman.run(
	{
		collection: require(collectionFile),
		environment: require(envFile),
		envVar: [
			{ key: "adminEmail", value: process.env.ADMIN_EMAIL },
			{ key: "adminPw", value: process.env.ADMIN_PW },
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
