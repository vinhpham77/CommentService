-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: commentservice
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment_details`
--

DROP TABLE IF EXISTS `comment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` tinytext NOT NULL,
  `created_by` varchar(255) NOT NULL,
  `left` int NOT NULL,
  `right` int NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `comment_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKajaan4vhpc9j3qrj8v0v0d5il` (`comment_id`),
  CONSTRAINT `FKajaan4vhpc9j3qrj8v0v0d5il` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_details`
--

LOCK TABLES `comment_details` WRITE;
/*!40000 ALTER TABLE `comment_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `target_id` int NOT NULL,
  `type` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,1,_binary '\0'),(2,2,_binary '\0'),(3,3,_binary '\0'),(4,4,_binary '\0'),(5,5,_binary '\0'),(6,6,_binary '\0'),(7,7,_binary '\0'),(8,8,_binary '\0'),(9,9,_binary '\0'),(10,1,_binary ''),(11,2,_binary ''),(12,3,_binary ''),(13,18,_binary '\0'),(14,19,_binary '\0'),(15,20,_binary '\0'),(16,21,_binary '\0'),(17,22,_binary '\0'),(18,23,_binary '\0'),(19,24,_binary '\0'),(20,25,_binary '\0'),(21,26,_binary '\0'),(22,27,_binary '\0'),(23,28,_binary '\0'),(24,29,_binary '\0'),(25,30,_binary '\0'),(26,31,_binary '\0'),(27,32,_binary '\0'),(28,33,_binary '\0'),(29,34,_binary '\0'),(30,8,_binary '\0'),(31,9,_binary '\0'),(32,10,_binary '\0'),(33,11,_binary '\0'),(34,35,_binary '\0'),(35,36,_binary '\0'),(36,37,_binary '\0'),(37,38,_binary '\0'),(38,39,_binary '\0'),(39,40,_binary '\0'),(40,41,_binary '\0'),(41,12,_binary '\0'),(42,13,_binary '\0'),(43,14,_binary '\0'),(44,15,_binary ''),(45,16,_binary ''),(46,17,_binary ''),(47,18,_binary '');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-22  5:56:53
