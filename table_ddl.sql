CREATE DATABASE `superCodingProject1st` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE DATABASE `superCodingProject1st` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `comments` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `post_id` int NOT NULL,
                            `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            `content` text NOT NULL,
                            `created_at` datetime NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `comments_posts_FK` (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `great` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `post_id` int NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `great_posts_FK` (`post_id`),
                         CONSTRAINT `great_posts_FK` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `posts` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `title` text NOT NULL,
                         `content` longtext NOT NULL,
                         `created_at` datetime NOT NULL,
                         `user_id` int NOT NULL,
                         `comments_id` int DEFAULT NULL,
<<<<<<< HEAD
=======
                         -- like 갯수를 저장할  column 추가
                         `like_count` int DEFAULT 0,

>>>>>>> develop_great
                         PRIMARY KEY (`id`),
                         KEY `posts_users_FK` (`user_id`),
                         CONSTRAINT `posts_users_FK` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `roles` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tokenStatus` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `token` text NOT NULL,
                               `isValid` tinyint(1) NOT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `userPrincipalRoles` (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `user_id` int NOT NULL,
                                      `role_id` int NOT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `userPrincipalRoles_users_FK` (`user_id`),
                                      KEY `userPrincipalRoles_roles_FK` (`role_id`),
                                      CONSTRAINT `userPrincipalRoles_roles_FK` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
                                      CONSTRAINT `userPrincipalRoles_users_FK` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `user_name` varchar(100) NOT NULL,
                         `password` varchar(100) NOT NULL,
                         `email` varchar(100) NOT NULL,
                         `created_at` datetime NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `users_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;