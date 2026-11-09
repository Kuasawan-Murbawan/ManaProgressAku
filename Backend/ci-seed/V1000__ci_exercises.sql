-- CI-only seed. Never loaded in production: it sits outside the default Flyway path
-- and runs only when a CI job adds filesystem:ci-seed to spring.flyway.locations.
-- Keep it in step with the exercise table: if a later V file changes that table,
-- update this file in the same change.
--
-- The 15 default exercises the local DataInitializer inserts. On an empty table they
-- get ids 1 to 15 in this order. is_bodyweight and equipment are false and NULL, the
-- entity defaults DataInitializer relies on, since it never sets either field.
INSERT INTO `exercise` (`exercise_name`, `exercise_type`, `general_info`, `is_bodyweight`, `equipment`) VALUES
('Bench Press', '1', 'Menekan Kerusi', b'0', NULL),
('Push Up', '1', 'Upper body strength training', b'0', NULL),
('Leg Raises', '2', 'Lower body strength training', b'0', NULL),
('Leg Curl', '2', 'Lower Leg strength', b'0', NULL),
('Preacher Curl', '1', 'For bicep', b'0', NULL),
('Deadlift', '2', 'Full lower body and back strength', b'0', NULL),
('Pull Up', '1', 'Back and biceps bodyweight exercise', b'0', NULL),
('Bicep Curl', '1', 'Dumbbell or barbell biceps exercise', b'0', NULL),
('Squat', '2', 'Compound lower body exercise', b'0', NULL),
('Shoulder Press', '1', 'Shoulder and triceps compound movement', b'0', NULL),
('Chest Fly', '1', 'Chest isolation exercise', b'0', NULL),
('Lat Pulldown', '1', 'Back and lats strengthening exercise', b'0', NULL),
('Tricep Dips', '1', 'Triceps and chest bodyweight exercise', b'0', NULL),
('Lunges', '2', 'Lower body balance and strength exercise', b'0', NULL),
('Calf Raises', '2', 'Strengthening calves', b'0', NULL);
