# Spring REST API - Multi-DBMS Configuration PoC

This repository demonstrates a Proof of Concept (PoC) for connecting and configuring multiple Database Management Systems (DBMS) in a Spring Boot application. It also illustrates the use of a proper folder structure to support this configuration.

## MySQL DDL Script
```sql
CREATE DATABASE `user_account`;
USE `user_account`;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) UNIQUE NOT NULL,
  `email` varchar(45) UNIQUE NOT NULL,
  `password` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE DATABASE `project_management`;
USE `project_management`;

CREATE TABLE `boards` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(45) UNIQUE NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `owner_id` int NOT NULL,
  `is_public` boolean NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `board_permissions` (
  `id` tinyint NOT NULL,
  `name` varchar(45) UNIQUE NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `board_collaborators` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `board_id` bigint NOT NULL,
  `collaborator_id` bigint NOT NULL,
  `permission_id` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `board_id_idx` (`board_id`),
  KEY `permission_id_idx` (`permission_id`),
  CONSTRAINT `board_id` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`),
  CONSTRAINT `permission_id` FOREIGN KEY (`permission_id`) REFERENCES `board_permissions` (`id`)
);
```

## References
- [GitHub: Multiple Datasource PoC](https://github.com/PlayProCode/PlayJava/tree/master/multi-datasource-poc)
- [YouTube: Spring Boot Multi-Database Configuration](https://www.youtube.com/watch?v=k-1OboD7X5w)
