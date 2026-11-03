SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `activity` (
  `activityid` varchar(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `exerciseid` int NOT NULL,
  `sessionid` varchar(20) NOT NULL,
  PRIMARY KEY (`activityid`),
  KEY `FKs8yx147if6748ah6mf81aa0ii` (`sessionid`),
  CONSTRAINT `FKs8yx147if6748ah6mf81aa0ii` FOREIGN KEY (`sessionid`) REFERENCES `session` (`sessionid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `activityset` (
  `setid` bigint NOT NULL AUTO_INCREMENT,
  `reps` int NOT NULL,
  `set_number` int NOT NULL,
  `weight` decimal(6,2) NOT NULL,
  `activityid` varchar(20) NOT NULL,
  PRIMARY KEY (`setid`),
  KEY `FKgnmy803rqmmtbo18d9r9tnv30` (`activityid`),
  CONSTRAINT `FKgnmy803rqmmtbo18d9r9tnv30` FOREIGN KEY (`activityid`) REFERENCES `activity` (`activityid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `ascend_api_usage` (
  `id` int NOT NULL,
  `monthly_count` int NOT NULL DEFAULT '0',
  `monthly_period_start` date NOT NULL,
  `hourly_count` int NOT NULL DEFAULT '0',
  `hourly_period_start` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `exercise` (
  `exerciseid` int NOT NULL AUTO_INCREMENT,
  `exercise_name` varchar(50) NOT NULL,
  `exercise_type` varchar(15) DEFAULT NULL,
  `general_info` varchar(255) DEFAULT NULL,
  `is_bodyweight` bit(1) NOT NULL,
  `ascend_exercise_id` varchar(50) DEFAULT NULL,
  `equipment` tinyint DEFAULT NULL,
  PRIMARY KEY (`exerciseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `session` (
  `sessionid` varchar(20) NOT NULL,
  `date` date NOT NULL,
  `time` time(6) NOT NULL,
  `user_id` bigint NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`sessionid`),
  KEY `fk_session_user` (`user_id`),
  CONSTRAINT `fk_session_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `user_profile` (
  `user_id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER','PREFER_NOT_TO_SAY') DEFAULT NULL,
  `height_cm` decimal(5,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `weight_kg` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FKuganfwvnbll4kn2a3jeyxtyi` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `password_changed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


SET FOREIGN_KEY_CHECKS=1;
