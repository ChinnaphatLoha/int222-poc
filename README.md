# Spring REST API - Integrated Project PoC (INT222, SIT KMUTT 2024/1)

This repository serves as a Proof of Concept (PoC) for the Integrated Project in the INT222 course at SIT KMUTT (2024/1). The PoC demonstrates the use of multiple Database Management Systems (DBMS) in a Spring Boot application, alongside implementing authentication and authorization with Spring Security.

## Purpose

The primary objectives of this PoC are:

- **Multi-DBMS Configuration:** Showcasing how to connect and configure multiple DBMS within a single Spring Boot application.
- **Authentication and Authorization:** Implementing secure access controls using Spring Security to manage user authentication and authorization across different services.
- **Project Structure:** Illustrating an effective folder structure to support these configurations and functionalities.

## MySQL DDL Script

```sql
CREATE DATABASE `user_account`;
USE `user_account`;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE TABLE `users` (
    `oid` char(36) NOT NULL UNIQUE,
    `name` varchar(100) NOT NULL,
    `username` varchar(50) NOT NULL,
    `email` varchar(50) NOT NULL,
    `password` varchar(100) NOT NULL,
    `role` enum('LECTURER', 'STAFF', 'STUDENT') NOT NULL DEFAULT 'STUDENT',
    PRIMARY KEY (`oid`)
);

CREATE DATABASE `project_management`;
USE `project_management`;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE TABLE `boards` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(45) UNIQUE NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `owner_id` char(36) NOT NULL,
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
  `collaborator_id` char(36) NOT NULL,
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
