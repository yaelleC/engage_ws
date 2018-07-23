-- MySQL dump 10.13  Distrib 5.7.12, for Win32 (AMD64)
--
-- Host: 127.0.0.1    Database: engage
-- ------------------------------------------------------
-- Server version	5.6.27-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config_files`
--

DROP TABLE IF EXISTS `config_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_files` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config` text,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `submited` tinyint(1) DEFAULT '0',
  `idSG` int(11) DEFAULT NULL,
  `idDeveloper` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=635 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `developer`
--

DROP TABLE IF EXISTS `developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `developer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84000088 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `developer` WRITE;
/*!40000 ALTER TABLE `developer` DISABLE KEYS */;
INSERT INTO `developer` VALUES (1,'Yaelle','Chaudy','yaelle.chaudy@uws.ac.uk','password',1);
/*!40000 ALTER TABLE `developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `message` text,
  `type` enum('POSITIVE','NEGATIVE','NEUTRAL','BADGE','HINT','ADAPTATION') DEFAULT NULL,
  `final` tinyint(1) NOT NULL DEFAULT '0',
  `idSG` int(11) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `win` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idSG_idx` (`idSG`)
) ENGINE=InnoDB AUTO_INCREMENT=4836 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `feedback_trigger`
--

DROP TABLE IF EXISTS `feedback_trigger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_trigger` (
  `idSG` int(11) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `idFeedback` int(11) NOT NULL,
  `idOutcome` int(11) DEFAULT NULL,
  `actionName` varchar(200) DEFAULT NULL,
  `inactivity` tinyint(1) NOT NULL DEFAULT '0',
  `limitValue` int(11) NOT NULL,
  `inferior` tinyint(1) NOT NULL DEFAULT '1',
  `repeatBool` tinyint(1) NOT NULL DEFAULT '1',
  `immediate` tinyint(1) DEFAULT NULL,
  KEY `idSG` (`idSG`),
  KEY `idFeedback` (`idFeedback`),
  KEY `idOutcome` (`idOutcome`),
  CONSTRAINT `feedback_trigger_ibfk_1` FOREIGN KEY (`idSG`) REFERENCES `seriousgame` (`id`) ON DELETE CASCADE,
  CONSTRAINT `feedback_trigger_ibfk_2` FOREIGN KEY (`idFeedback`) REFERENCES `feedback` (`id`),
  CONSTRAINT `feedback_trigger_ibfk_3` FOREIGN KEY (`idOutcome`) REFERENCES `learningoutcome` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `game_outcome`
--

DROP TABLE IF EXISTS `game_outcome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_outcome` (
  `idGP` int(11) NOT NULL,
  `idOutcome` int(11) NOT NULL,
  `value` float NOT NULL,
  KEY `idGP` (`idGP`),
  KEY `idOutcome` (`idOutcome`),
  CONSTRAINT `game_outcome_ibfk_1` FOREIGN KEY (`idGP`) REFERENCES `gameplay` (`id`),
  CONSTRAINT `game_outcome_ibfk_2` FOREIGN KEY (`idOutcome`) REFERENCES `learningoutcome` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `gameplay`
--

DROP TABLE IF EXISTS `gameplay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gameplay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idSG` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastAction` timestamp NULL DEFAULT NULL,
  `ended` timestamp NULL DEFAULT NULL,
  `idPlayer` int(11) NOT NULL,
  `win` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idPlayer` (`idPlayer`)
) ENGINE=InnoDB AUTO_INCREMENT=6243 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idTeacher` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=421 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `learningoutcome`
--

DROP TABLE IF EXISTS `learningoutcome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `learningoutcome` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `type` enum('KNOWLEDGE','SKILL','COMPETENCE') DEFAULT NULL,
  `startingValue` float NOT NULL DEFAULT '0',
  `idSG` int(11) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idSG` (`idSG`),
  CONSTRAINT `learningoutcome_ibfk_1` FOREIGN KEY (`idSG`) REFERENCES `seriousgame` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1775 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `log_gameplay_action`
--

DROP TABLE IF EXISTS `log_gameplay_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_gameplay_action` (
  `idGP` int(11) NOT NULL,
  `action` varchar(200) NOT NULL COMMENT 'action is a json object {"action": "name", "values":{"param1": "val1", "param2":"val2" ...}}',
  `idOutcome` int(11) NOT NULL,
  `mark` float NOT NULL DEFAULT '0',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idGP`,`time`,`action`,`idOutcome`),
  KEY `idGP` (`idGP`),
  CONSTRAINT `log_gameplay_action_ibfk_1` FOREIGN KEY (`idGP`) REFERENCES `gameplay` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `log_gameplay_feedback`
--

DROP TABLE IF EXISTS `log_gameplay_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_gameplay_feedback` (
  `idGP` int(11) NOT NULL,
  `idFeedback` int(11) DEFAULT NULL,
  `feedback` varchar(500) NOT NULL COMMENT 'JSON of the feedback',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `idGP` (`idGP`),
  KEY `idFeedback` (`idFeedback`),
  CONSTRAINT `log_gameplay_feedback_ibfk_1` FOREIGN KEY (`idGP`) REFERENCES `gameplay` (`id`),
  CONSTRAINT `log_gameplay_feedback_ibfk_2` FOREIGN KEY (`idFeedback`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player_113_0`
--

DROP TABLE IF EXISTS `player_113_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_113_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idStudent` int(11) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` varchar(200) DEFAULT NULL,
  `country` varchar(200) DEFAULT NULL,
  `ABtest` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_113_0`
--

LOCK TABLES `player_113_0` WRITE;
/*!40000 ALTER TABLE `player_113_0` DISABLE KEYS */;
INSERT INTO `player_113_0` VALUES (1,0,'Yaelle',26,'female','Scotland','group2'),(2,0,'Théo',31,'male','France','group2'),(3,0,'Carla',15,'female','Italy','group2'),(4,0,'Marcela',25,'female','Colombia','group2'),(5,0,'John',31,'male','Singapore','group1'),(6,0,'Julia',10,'female','France','group2'),(7,0,'Daniella',50,'female','Italy','group2'),(8,0,'Tom',19,'male','England','group1'),(9,0,'r',1,'female','rs','group1'),(10,0,'Giorgio',27,'male','France','group1'),(15,0,'Giorgio',27,'male','France','group2'),(16,0,'Peter',0,'male','UK','group1'),(17,0,'Yaly',26,'female','Scotland','group2'),(18,0,'What\'s your name?',0,'Are you a boy or a girl?','UK','group2'),(19,0,'Fany',18,'female','UK','group2'),(20,0,'Giorgio',27,'male','France','group1'),(21,0,'olivier',27,'male','France','group2'),(22,0,'Samuel',0,'male','UK','group2'),(23,0,'David',22,'male','France','group1'),(24,0,'Etienne',27,'male','France','group1'),(25,0,'Etienne',46,'male','France','group1'),(26,0,'Test',26,'female','UK','group2'),(27,0,'Test',27,'female','UK','group2'),(28,0,'Jonah',33,'male','France','group2'),(29,0,'Samuel',12,'male','Scotland','group1'),(30,0,'Peter',21,'male','UK','group1'),(31,0,'David',22,'male','France','group2'),(32,0,'Géry',28,'male','France','group2'),(33,0,'David',22,'male','France','group1'),(34,0,'kirm',23,'male','France','group2'),(35,0,'Giorgio',27,'male','France','group1'),(36,0,'David',22,'male','France','group1'),(37,0,'Noah',18,'male','USA','group2'),(38,0,'Cloe',23,'female','France','group1'),(39,0,'Giorgio',27,'male','France','group1'),(40,0,'Chloé ',14,'female','Where are you from (country)?','group1'),(41,0,'Test',1,'male','Scotland','group2'),(42,0,'guill',29,'male','France','group2'),(43,0,'Géry',28,'male','France','group1'),(44,0,'Grace',16,'female','Germany','group2'),(45,0,'FDGFGH',111,'male','CVCV','group2'),(46,0,'Valentin',10,'male','France','group1'),(47,0,'mary',6,'female','UK','group1'),(48,0,'Nico',29,'male','France','group1'),(49,0,'Julien',26,'male','France','group1'),(50,0,'Topi',24,'male','Finland','group2'),(51,0,'Miriam ',12,'female','Italia','group2'),(52,0,'What\'s your name?',0,'Are you a boy or a girl?','Where are you from (country)?','group1'),(53,0,'lena',13,'female',' Belgique','group2'),(54,0,'What\'s your name?',0,'Are you a boy or a girl?','Where are you from (country)?','group1');
/*!40000 ALTER TABLE `player_113_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin','2014-11-06 00:38:12','2014-11-06 00:38:12'),(2,'developer','2014-11-06 00:38:13','2014-11-06 00:38:13'),(3,'teacher','2014-11-06 00:38:13','2014-11-06 00:38:13'),(4,'student','2014-11-06 00:38:13','2014-11-06 00:38:13');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schema_migrations`
--

DROP TABLE IF EXISTS `schema_migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_migrations` (
  `version` varchar(255) NOT NULL,
  UNIQUE KEY `unique_schema_migrations` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schema_migrations`
--

LOCK TABLES `schema_migrations` WRITE;
/*!40000 ALTER TABLE `schema_migrations` DISABLE KEYS */;
INSERT INTO `schema_migrations` VALUES ('20141104145349'),('20141104153946'),('20141104213920'),('20141104213923'),('20141104214500'),('20141108205104'),('20141111230208'),('20141117223157'),('20141117232317'),('20141118231358'),('20141118233723');
/*!40000 ALTER TABLE `schema_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school`
--

DROP TABLE IF EXISTS `school`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `school` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `country` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school`
--

LOCK TABLES `school` WRITE;
/*!40000 ALTER TABLE `school` DISABLE KEYS */;
INSERT INTO `school` VALUES (1,'test','test','test'),(2,'Napier','UK','UK');
/*!40000 ALTER TABLE `school` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seriousgame`
--

DROP TABLE IF EXISTS `seriousgame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seriousgame` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT '0',
  `idDeveloper` int(11) NOT NULL,
  `idTeacher` int(11) DEFAULT NULL,
  `nameVersion` varchar(100) DEFAULT NULL,
  `comments` text,
  `name` varchar(150) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` text,
  `ageMin` int(2) DEFAULT NULL,
  `ageMax` int(2) DEFAULT NULL,
  `language` varchar(2) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `public` tinyint(1) NOT NULL DEFAULT '0',
  `configFile_short` text,
  `configFile_long` mediumtext,
  `url` varchar(1000) DEFAULT NULL,
  `screenshot_url` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`,`version`),
  KEY `idDev_idx` (`idDeveloper`),
  CONSTRAINT `idDev` FOREIGN KEY (`idDeveloper`) REFERENCES `developer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=343 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seriousgame`
--

LOCK TABLES `seriousgame` WRITE;
/*!40000 ALTER TABLE `seriousgame` DISABLE KEYS */;
INSERT INTO `seriousgame` VALUES (113,0,2,NULL,'initial',NULL,'EU mouse','2015-04-10 18:43:24','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL),(113,1,2,2,NULL,NULL,'EU mouse','2015-05-04 22:23:32','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL),(113,2,2,2,NULL,NULL,'EU mouse','2015-05-04 22:54:55','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL),(113,3,2,2,NULL,NULL,'EU mouse','2015-05-04 22:56:56','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL),(113,4,2,2,NULL,NULL,'EU mouse','2015-05-06 21:49:07','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL),(113,5,2,2,NULL,NULL,'EU mouse','2015-05-06 21:49:18','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL),(113,6,2,2,NULL,NULL,'EU mouse','2015-05-06 22:11:59','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL),(113,7,2,2,NULL,NULL,'EU mouse','2015-05-06 22:12:06','This is a mini serious game that teaches you to identify countries part of the European Union. \n\n Click on the screen to make the mouse fly and collect ALL EU countries.',10,99,'EN','UK',1,'{\"feedback\":{\"master_time\":{\"message\":\"You played more than 30 minutes\",\"type\":\"badge\"},\"end_win\":{\"message\":\"Well done, you won the game :)\",\"final\":\"win\"},\"speedGame\":{\"message\":\"You\'re too good, let\'s make things more challenging\",\"type\":\"adaptation\"},\"novice_time\":{\"message\":\"You played more than 10 minutes\",\"type\":\"badge\"},\"gold_medal\":{\"message\":\"You found 200 EU countries\",\"type\":\"badge\"},\"performance\":{\"message\":\"You won 10+ times\",\"type\":\"badge\"},\"end_lose\":{\"message\":\"Sorry, you lost the game :(\",\"final\":\"lose\"},\"slowGame\":{\"message\":\"Hey, let\'s slow things down a bit\",\"type\":\"adaptation\"},\"correct_country\":{\"message\":\"Well done, [country] is indeed part of the EU\",\"type\":\"positive\"},\"effort\":{\"message\":\"You played 10+ times\",\"type\":\"badge\"},\"silver_medal\":{\"message\":\"You found 100 EU countries\",\"type\":\"badge\"},\"expert_time\":{\"message\":\"You played more than 60 minutes\",\"type\":\"badge\"},\"bronze_medal\":{\"message\":\"You found 50 EU countries! Well done\",\"type\":\"badge\"},\"wrong_country\":{\"message\":\"Nope, [country] is not part of the EU\",\"type\":\"negative\"}},\"learningOutcomes\":{\"eu_countries\":{\"desc\":\"distinct countries of the EU left to find\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_win\"}]},{\"limit\":11,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]},{\"limit\":19,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"speedGame\"}]}],\"value\":28},\"lives\":{\"desc\":\"number of lives the player has\",\"feedbackTriggered\":[{\"limit\":1,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"end_lose\"}]},{\"limit\":2,\"sign\":\"<\",\"feedback\":[{\"immediate\":true,\"name\":\"slowGame\"}]}],\"value\":10},\"eu_score\":{\"desc\":\"overall score, number of correct countries identified\",\"feedbackTriggered\":[],\"value\":0}},\"inactivityFeedback\":[],\"player\":[{\"name\":\"name\",\"question\":\"What\'s your name?\",\"type\":\"String\"},{\"name\":\"age\",\"question\":\"How old are you?\",\"type\":\"Int\"},{\"name\":\"gender\",\"question\":\"Are you a boy or a girl?\",\"type\":\"String\"},{\"name\":\"country\",\"question\":\"Where are you from (country)?\",\"type\":\"String\"},{\"name\":\"ABtest\",\"type\":\"String\"}],\"evidenceModel\":{\"countryReSelected\":{\"desc\":\"When a player selects a country he\\/she had already selected\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}},\"newCountrySelected\":{\"desc\":\"When a player selects a country for the first time\",\"reactions\":[{\"feedback\":[{\"immediate\":true,\"name\":\"correct_country\"}],\"values\":[{\"country\":\"austria\"},{\"country\":\"belgium\"},{\"country\":\"bulgaria\"},{\"country\":\"croatia\"},{\"country\":\"cyprus\"},{\"country\":\"czech_republic\"},{\"country\":\"denmark\"},{\"country\":\"estonia\"},{\"country\":\"finland\"},{\"country\":\"france\"},{\"country\":\"germany\"},{\"country\":\"greece\"},{\"country\":\"hungary\"},{\"country\":\"ireland\"},{\"country\":\"italy\"},{\"country\":\"latvia\"},{\"country\":\"lithuania\"},{\"country\":\"luxembourg\"},{\"country\":\"malta\"},{\"country\":\"netherlands\"},{\"country\":\"poland\"},{\"country\":\"portugal\"},{\"country\":\"romania\"},{\"country\":\"slovakia\"},{\"country\":\"slovenia\"},{\"country\":\"spain\"},{\"country\":\"sweden\"},{\"country\":\"united_kingdom\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"eu_countries\"},{\"mark\":1,\"learningOutcome\":\"eu_score\"}]},{\"else\":\"true\",\"feedback\":[{\"immediate\":true,\"name\":\"wrong_country\"}],\"marks\":[{\"mark\":-1,\"learningOutcome\":\"lives\"}]}],\"params\":{\"country\":\"String\"}}},\"badgeModel\":[{\"limit\":4,\"sign\":\">\",\"feedback\":[\"effort\"],\"function\":\"numberGameplays\"},{\"limit\":9,\"sign\":\">\",\"feedback\":[\"performance\"],\"function\":\"numberWin\"},{\"limit\":60,\"sign\":\">\",\"feedback\":[\"expert_time\"],\"function\":\"totalTime\"},{\"limit\":30,\"sign\":\">\",\"feedback\":[\"master_time\"],\"function\":\"totalTime\"},{\"limit\":10,\"sign\":\">\",\"feedback\":[\"novice_time\"],\"function\":\"totalTime\"},{\"limit\":199,\"sign\":\">\",\"feedback\":[\"gold_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":99,\"sign\":\">\",\"feedback\":[\"silver_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"},{\"limit\":49,\"sign\":\">\",\"feedback\":[\"bronze_medal\"],\"outcome\":\"eu_score\",\"function\":\"sumScore\"}],\"seriousGame\":{\"genre\":\"runner\",\"id\":113,\"idDeveloper\":2,\"ageMin\":10,\"description\":\"This is a mini serious game that teaches you to identify countries part of the European Union. \\n\\n Click on the screen to make the mouse fly and collect ALL EU countries.\",\"subject\":\"geography, EU countries\",\"name\":\"EU mouse\",\"ageMax\":99,\"public\":\"true\",\"lang\":\"EN\",\"country\":\"UK\",\"version\":7}}',NULL,NULL,NULL);
/*!40000 ALTER TABLE `seriousgame` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sg_school`
--

DROP TABLE IF EXISTS `sg_school`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sg_school` (
  `idSG` int(11) NOT NULL,
  `idSchool` int(11) NOT NULL,
  KEY `idSchool` (`idSchool`),
  KEY `idSG` (`idSG`),
  CONSTRAINT `sg_school_ibfk_1` FOREIGN KEY (`idSchool`) REFERENCES `school` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sg_school_ibfk_2` FOREIGN KEY (`idSG`) REFERENCES `seriousgame` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sg_school`
--

LOCK TABLES `sg_school` WRITE;
/*!40000 ALTER TABLE `sg_school` DISABLE KEYS */;
/*!40000 ALTER TABLE `sg_school` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sg_student`
--

DROP TABLE IF EXISTS `sg_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sg_student` (
  `idSG` int(11) NOT NULL,
  `idStd` int(11) NOT NULL,
  `versionPlayed` int(11) NOT NULL,
  KEY `idSG` (`idSG`),
  KEY `idStd` (`idStd`),
  CONSTRAINT `sg_student_ibfk_1` FOREIGN KEY (`idSG`) REFERENCES `seriousgame` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sg_student_ibfk_2` FOREIGN KEY (`idStd`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `sg_teacher`
--

DROP TABLE IF EXISTS `sg_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sg_teacher` (
  `idSG` int(11) NOT NULL,
  `idTeacher` int(11) NOT NULL,
  PRIMARY KEY (`idSG`,`idTeacher`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `std_teacher`
--

DROP TABLE IF EXISTS `std_teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `std_teacher` (
  `idStd` int(11) NOT NULL,
  `idTeacher` int(11) NOT NULL,
  `idGroup` int(11) DEFAULT NULL,
  PRIMARY KEY (`idStd`,`idTeacher`),
  KEY `idStd` (`idStd`),
  KEY `idTeacher` (`idTeacher`),
  CONSTRAINT `std_teacher_ibfk_1` FOREIGN KEY (`idStd`) REFERENCES `student` (`id`),
  CONSTRAINT `std_teacher_ibfk_2` FOREIGN KEY (`idTeacher`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(150) NOT NULL,
  `password` varchar(200) NOT NULL,
  `dateBirth` date DEFAULT NULL,
  `gender` enum('m','f') DEFAULT NULL,
  `idSchool` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `idGroup` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_index` (`username`,`password`),
  KEY `idSchool` (`idSchool`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`idSchool`) REFERENCES `school` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2150 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1, 'test', 'test1234', NULL, NULL, 1, 1, NULL);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idSchool` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idSchool` (`idSchool`),
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`idSchool`) REFERENCES `school` (`id`),
  CONSTRAINT `teacher_ibfk_2` FOREIGN KEY (`idSchool`) REFERENCES `school` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=321 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `crypted_password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `remember_me_token` varchar(255) DEFAULT NULL,
  `remember_me_token_expires_at` datetime DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_users_on_email` (`email`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `index_users_on_remember_me_token` (`remember_me_token`)
) ENGINE=InnoDB AUTO_INCREMENT=442 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'yaelle.borghini@gmail.com','$2a$10$hb.ICRln4wblNv7.ppuq0uovsIUazkfpYg/Oy0VoH1r8idMF2228a','nPCokwbUEqzedzsfKfzu','2014-11-06 00:40:18','2014-11-21 21:16:19',NULL,NULL,1,'yaelle.borghini');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-16 17:07:29
