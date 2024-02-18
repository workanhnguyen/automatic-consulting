-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: graduation_project_db
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actions`
--

DROP TABLE IF EXISTS `actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actions` (
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_oe93gku1sb0in93ahwynjt5in` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actions`
--

LOCK TABLES `actions` WRITE;
/*!40000 ALTER TABLE `actions` DISABLE KEYS */;
INSERT INTO `actions` VALUES (1708160303028,1,NULL,'ask','Hỏi đáp',NULL);
/*!40000 ALTER TABLE `actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actions_seq`
--

DROP TABLE IF EXISTS `actions_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actions_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actions_seq`
--

LOCK TABLES `actions_seq` WRITE;
/*!40000 ALTER TABLE `actions_seq` DISABLE KEYS */;
INSERT INTO `actions_seq` VALUES (51);
/*!40000 ALTER TABLE `actions_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversations`
--

DROP TABLE IF EXISTS `conversations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversations` (
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `request_text` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `response_text` varchar(10000) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpltqvfcbkql9svdqwh0hw4g1d` (`user_id`),
  CONSTRAINT `FKpltqvfcbkql9svdqwh0hw4g1d` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversations`
--

LOCK TABLES `conversations` WRITE;
/*!40000 ALTER TABLE `conversations` DISABLE KEYS */;
INSERT INTO `conversations` VALUES (1708226591769,2,NULL,52,'điểm tuyển sinh ngành khmt năm 2024 năm 2023','Điểm xét tuyển của ngành Khoa học máy tính - Trường Đại học Mở TP. HCM như sau:\n1. Theo phương thức thi tốt nghiệp THPT của Bộ Giáo dục và Đào tạo:\n+ Năm 2023: 24.00\n+ Năm 2022: 24.50\n+ Năm 2021: 25.55\n+ Năm 2020: 23.00\n+ Năm 2019: 19.20\n+ Năm 2018: 17.00\n+ Năm 2017: 20.75\n+ Năm 2016: 20.00\n2. Theo phương thức xét tuyển học bạ THPT:\n+ Năm 2023: Nhận hết HSG + Học bạ có CCNN + Học bạ: 28.00\n+ Năm 2022: 27.25 (HSG)\n+ Năm 2021: 25.25\n+ Năm 2020: 23.80\n+ Năm 2019: 25.50'),(1708226883079,52,NULL,52,'Các phương thức xét tuyển','Về các phương thức tuyển sinh của Trường Đại học Mở thành phố Hồ Chí Minh, vui lòng xem thêm tại đường dẫn sau: \n1. Chương trình đại trà: https://tuyensinh.ou.edu.vn/thong-tin-tuyen-sinh-nam-2023-truong-dai-hoc-mo-tp-hcm-ma-truong-mbs\n2. Chương trình chất lượng cao: https://tuyensinh.ou.edu.vn/gioi-thieu-khoa-dao-tao-dac-biet-chuong-trinh-chat-luong-cao'),(1708227426061,53,NULL,52,'Tổ hợp xét tuyển ngành khmt clc','Các môn xét tuyển của ngành Khoa học máy tính (Chất lượng cao) - Trường Đại học Mở TP. Hồ Chí Minh bao gồm:\n1. Khối A00: Toán, Lý, Hóa.\n2. Khối A01: Toán, Lý, Anh.\n3. Khối D01: Toán, Văn, Anh.\n4. Khối D07: Toán, Hóa, Anh.\n\nMôn Toán ở tất cả tổ hợp trên đều được nhân hệ số 2.');
/*!40000 ALTER TABLE `conversations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conversations_seq`
--

DROP TABLE IF EXISTS `conversations_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversations_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conversations_seq`
--

LOCK TABLES `conversations_seq` WRITE;
/*!40000 ALTER TABLE `conversations_seq` DISABLE KEYS */;
INSERT INTO `conversations_seq` VALUES (151);
/*!40000 ALTER TABLE `conversations_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrance_method_groups`
--

DROP TABLE IF EXISTS `entrance_method_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrance_method_groups` (
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrance_method_groups`
--

LOCK TABLES `entrance_method_groups` WRITE;
/*!40000 ALTER TABLE `entrance_method_groups` DISABLE KEYS */;
INSERT INTO `entrance_method_groups` VALUES (1708160302906,1,NULL,'Bộ Giáo dục và Đào tạo',NULL),(1708160302912,2,NULL,'Trường Đại học Mở thành phố Hồ Chí Minh',NULL);
/*!40000 ALTER TABLE `entrance_method_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrance_method_groups_seq`
--

DROP TABLE IF EXISTS `entrance_method_groups_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrance_method_groups_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrance_method_groups_seq`
--

LOCK TABLES `entrance_method_groups_seq` WRITE;
/*!40000 ALTER TABLE `entrance_method_groups_seq` DISABLE KEYS */;
INSERT INTO `entrance_method_groups_seq` VALUES (101);
/*!40000 ALTER TABLE `entrance_method_groups_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrance_methods`
--

DROP TABLE IF EXISTS `entrance_methods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrance_methods` (
  `created_date` bigint NOT NULL,
  `entrance_method_group_id` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `name` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `FKi5hjl0j4deihd7kq7b5tgm0o4` (`entrance_method_group_id`),
  CONSTRAINT `FKi5hjl0j4deihd7kq7b5tgm0o4` FOREIGN KEY (`entrance_method_group_id`) REFERENCES `entrance_method_groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrance_methods`
--

LOCK TABLES `entrance_methods` WRITE;
/*!40000 ALTER TABLE `entrance_methods` DISABLE KEYS */;
INSERT INTO `entrance_methods` VALUES (1708160302921,1,1,NULL,'Xét tuyển thẳng, ưu tiên xét tuyển theo quy chế tuyển sinh của Trường Đại học Mở thành phố Hồ Chí Minh và Bộ Giáo dục và Đào tạo',NULL),(1708160302926,1,2,NULL,'Xét tuyển theo kết quả thi tốt nghiệp Trung học Phổ thông',NULL),(1708160302929,1,3,NULL,'Ưu tiên xét tuyển đối với thí sinh có:\n\n+ Bằng tú tài quốc tế (IB) điểm từ 26;\n\n+ Chứng chỉ quốc tế A-Level của Trung tâm khảo thí ĐH Cambridge (Anh) theo điểm 3 môn thi (trở lên) đảm bảo mức điểm mỗi môn thi đạt từ C trở lên+ Kết quả trong kỳ thi chuẩn hóa SAT (Scholastic Assessment Test, Hoa kỳ) đạt điểm từ 1100/1600.',NULL),(1708160302932,2,4,NULL,'Ưu tiên xét tuyển học sinh Giỏi THPT (nhóm 1 và nhóm 2) có chứng chỉ Ngoại ngữ quốc tế theo quy định.',NULL),(1708160302934,2,5,NULL,'Ưu tiên xét tuyển học sinh Giỏi THPT (nhóm 1 và nhóm 2).',NULL),(1708160302938,2,6,NULL,'Ưu tiên xét tuyển kết quả học bạ THPT có chứng chỉ quốc tế.',NULL),(1708160302941,2,7,NULL,'Xét tuyển học bạ THPT.',NULL);
/*!40000 ALTER TABLE `entrance_methods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrance_methods_seq`
--

DROP TABLE IF EXISTS `entrance_methods_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrance_methods_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrance_methods_seq`
--

LOCK TABLES `entrance_methods_seq` WRITE;
/*!40000 ALTER TABLE `entrance_methods_seq` DISABLE KEYS */;
INSERT INTO `entrance_methods_seq` VALUES (101);
/*!40000 ALTER TABLE `entrance_methods_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrance_score_information`
--

DROP TABLE IF EXISTS `entrance_score_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entrance_score_information` (
  `year` varchar(4) COLLATE utf8mb4_unicode_ci NOT NULL,
  `entrance_method_id` bigint NOT NULL,
  `id` bigint NOT NULL,
  `major_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKck71hk8mbfb0jn3sasboicjp9` (`entrance_method_id`),
  KEY `FKsvsfyxs03620j6gajdm4o6nqi` (`major_id`),
  CONSTRAINT `FK21904m69njoypbahm504678gi` FOREIGN KEY (`id`) REFERENCES `information` (`id`),
  CONSTRAINT `FKck71hk8mbfb0jn3sasboicjp9` FOREIGN KEY (`entrance_method_id`) REFERENCES `entrance_methods` (`id`),
  CONSTRAINT `FKsvsfyxs03620j6gajdm4o6nqi` FOREIGN KEY (`major_id`) REFERENCES `majors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrance_score_information`
--

LOCK TABLES `entrance_score_information` WRITE;
/*!40000 ALTER TABLE `entrance_score_information` DISABLE KEYS */;
/*!40000 ALTER TABLE `entrance_score_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faculties`
--

DROP TABLE IF EXISTS `faculties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculties` (
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4u63apqkwoe8yh153mwyq93f5` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculties`
--

LOCK TABLES `faculties` WRITE;
/*!40000 ALTER TABLE `faculties` DISABLE KEYS */;
INSERT INTO `faculties` VALUES (1708160302877,1,NULL,'Công nghệ thông tin',NULL);
/*!40000 ALTER TABLE `faculties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faculties_seq`
--

DROP TABLE IF EXISTS `faculties_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculties_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculties_seq`
--

LOCK TABLES `faculties_seq` WRITE;
/*!40000 ALTER TABLE `faculties_seq` DISABLE KEYS */;
INSERT INTO `faculties_seq` VALUES (51);
/*!40000 ALTER TABLE `faculties_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `information`
--

DROP TABLE IF EXISTS `information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `information` (
  `action_id` bigint NOT NULL,
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `scope_id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `content` varchar(10000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `FKq7qqi91xpqvpnrkuqeq9gdmf` (`action_id`),
  KEY `FKn1j37hqwfq28md9rkhxu7s048` (`scope_id`),
  KEY `FKms2rkvdbqslvgexldjgx6bgqq` (`topic_id`),
  CONSTRAINT `FKms2rkvdbqslvgexldjgx6bgqq` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`),
  CONSTRAINT `FKn1j37hqwfq28md9rkhxu7s048` FOREIGN KEY (`scope_id`) REFERENCES `scopes` (`id`),
  CONSTRAINT `FKq7qqi91xpqvpnrkuqeq9gdmf` FOREIGN KEY (`action_id`) REFERENCES `actions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `information`
--

LOCK TABLES `information` WRITE;
/*!40000 ALTER TABLE `information` DISABLE KEYS */;
INSERT INTO `information` VALUES (1,1708223689364,52,NULL,2,1,'Liên hệ văn phòng Khoa Công nghệ thông tin:\n- Phòng 604 (Lầu 6), số 35-37 Hồ Hảo Hớn, Phường Cô Giang, Quận 1, Thành phố Hồ Chí Minh, Việt Nam.\n- Điện thoại: (84-028) 3838.6603\n- Website: www.it.ou.edu.vn\n- Email: fcs@ou.edu.vn',''),(1,1708223733015,53,NULL,2,2,'Sau khi hoàn thành các yêu cầu của chương trình đào tạo, sinh viên được cấp bằng Đại học chính quy tập trung, ngành Khoa học máy tính hoặc Hệ thống thông tin quản lý. Theo học chế tín chỉ, từ khóa 2009, sinh viên không còn thi tốt nghiệp mà chỉ cần hoàn thành các môn học theo quy định của chương trình.',''),(1,1708223788207,54,NULL,2,12,'Khoa Công Nghệ Thông Tin (CNTT) được thành lập từ tháng 09/1990 và là một trong những Khoa được thành lập đầu tiên của Trường Đại học Mở TP. Hồ Chí Minh. Trải qua hơn 30 năm xây dựng và phát triển, hiện nay Khoa có đội ngũ giảng viên cơ hữu giàu kinh nghiệm và tận tâm gồm các Tiến sĩ, Thạc sĩ tốt nghiệp từ các trường danh tiếng trong và ngoài nước cùng với các giảng viên thỉnh giảng đến từ các trường Đại học, viện nghiên cứu uy tín ở TP HCM. Khoa đã đào tạo và cung cấp nguồn nhân lực có chất lượng cho xã hội với số lượng xấp xỉ 5.500 cử nhân khoa học hệ chính quy khối ngành Công nghệ thông tin (Khoa học máy tính, Tin học, Hệ thống thông tin quản lý) và hàng ngàn cử nhân Tin học hệ không chính quy.\n\nKhoa đào tạo nhân lực cho ngành CNTT có đạo đức, có kiến thức lý thuyết tốt, có kỹ năng thực hành thành thạo, có khả năng nắm bắt được những vấn đề mới của sự phát triển ngành, phục vụ sự nghiệp công nghiệp hóa hiện đại hóa đất nước thông qua quá trình tin học hóa các lĩnh vực quản lý kinh tế, quản lý sản xuất, quản lý xã hội, giáo dục, …\n\nĐiều đáng tự hào chính là cựu sinh viên tốt nghiệp từ Khoa không chỉ là những nhân viên tin học - lập trình viên - nghiên cứu viên giỏi ở các công ty trong nước - các nước phát triển, mà còn là giảng viên của các trường Cao đẳng – Đại học, mà họ còn nắm giữ các vị trí điều hành quản lý tại các công ty và Viện nghiên cứu. Sau khi hoàn thành chương trình Đại học, rất nhiều cựu sinh viên của Khoa đã tiếp tục học ở các bậc học cao hơn như Thạc sĩ, Tiến sĩ tại các trường danh tiếng trong và ngoài nước.',''),(1,1708223825202,55,NULL,2,3,'Sinh viên tốt nghiệp hệ cao đẳng có thể học liên thông lên bậc Đại học.\n\nSinh viên tốt nghiệp hệ Đại học có thể học tiếp các bậc học cao hơn như Thạc sĩ, Tiến sĩ thuộc ngành Khoa học máy tính, Mạng máy tính, Công nghệ thông tin, Hệ thống thông tin.',''),(1,1708223869179,56,NULL,2,4,'Dưới đây là một số liên kết của Khoa Công nghệ thông tin:\n1. it.ou.edu.vn - Trang chủ của Khoa Công nghệ thông tin.\n2. https://www.facebook.com/tinhocmo - Trang Facebook chính thức của Khoa Công nghệ thông tin.\n3. https://www.facebook.com/ouityouth - Trang Facebook chính thức của Đoàn - Hội Khoa Công nghệ thông tin.',''),(1,1708223902767,57,NULL,2,5,'Khoa hiện đang đào tạo bậc học Đại Học (4 năm) với 5 chương trình: Khoa học máy tính, Công nghệ thông tin, Công nghệ thông tin tăng cường tiếng Nhật,  Hệ thống thông tin quản lý  thuộc chương trình hệ đào tạo đại trà và ngành Khoa học máy tính (chất lượng cao).',''),(1,1708223946760,58,NULL,2,6,'Khoa tuyển sinh trong hệ thống tuyển sinh đại học chung của nhà trường, thực hiện theo quy định của Bộ Giáo dục và Đào tạo. Xét tuyển dựa trên điểm tốt nghiệp phổ thông trung học của các môn thuộc khối A, A1 và D1 (riêng ngành Khoa học Máy tính, điểm môn Toán được tính hệ số 2).',''),(1,1708223996497,59,NULL,2,7,'Khoa có các mối quan hệ hợp tác với các doanh nghiệp trong lĩnh vực CNTT, ứng dụng CNTT vào kinh tế, sản xuất, quản lý,…  tại TP HCM để đưa sinh viên đi thực tập và tham quan học hỏi hằng năm.\nĐoàn Thanh niên và Hội sinh viên của Khoa có nhiều hoạt động hỗ trợ học tập cho sinh viên của Khoa bằng các hội thảo, báo cáo chuyên đề nhằm củng cố, bổ trợ kiến thức, rèn luyện các kỹ năng mềm cho sinh viên.\nCác chuyến dã ngoại, cắm trại, cuộc thi văn nghệ, thể thao và hoạt động thanh niên tình nguyện được tổ chức định kỳ nhằm phát triển các kỹ năng xã hội của sinh viên.',''),(1,1708224032117,60,NULL,2,8,'Địa điểm học tập chương trình đại trà: Khu dân cư Nhơn Đức, Huyện Nhà Bè, TP. Hồ Chí Minh.\n\nĐịa điểm học tập chương trình chất lượng cao (CLC): 97 Võ Văn Tần, phường Võ Thị Sáu, phường 3, TP. Hồ Chí Minh.',''),(1,1708224075391,61,NULL,2,9,'Chương trình đào tạo của ngành khoa học máy tính gồm 122 tín chỉ và của ngành hệ thống thông tin quản lý là 127 tín chỉ với thời gian là 4 năm. Sinh viên có thể tốt nghiệp sớm hơn hoặc dài hơn thời gian trên tùy theo kế hoạch và năng lực học tập.\n\nChương trình đào tạo gồm hai khối kiến thức chính:\nKhối kiến thức giáo dục đại cương bao gồm các môn học cơ sở về tin học (cơ sở lập trình, nhập môn tin học…), các môn học công cụ như toán, tin học, ngoại ngữ, giáo dục thể chất và quốc phòng… \nKhối kiến thức chuyên nghiệp bao gồm các môn học cơ sở ngành và chuyên ngành\nKhi vào giai đoạn chuyên ngành, sinh viên được chọn chuyên ngành phù hợp với năng lực và nguyện vọng. Với mỗi định hướng, sinh viên sẽ chọn những môn học phù hợp để sau khi ra trường có thể thích ứng nhanh chóng với môi trường làm việc.\n\nTrước khi ra trường, sinh viên phải trải qua một kỳ thực tập tại các doanh nghiệp, cơ quan, hoạt động trong lĩnh vực CNTT nhằm thực hành việc vận dụng lý thuyết vào thực tế. Sinh viên có thể chủ động lựa chọn đơn vị thực tập phù hợp với định hướng nghề nghiệp sau khi ra trường.\n\nChi tiết cụ thể xem tại đây: http://it.ou.edu.vn/pages/view/6-chuong-trinh-dao-tao',''),(1,1708224124837,62,NULL,2,10,'Ngành Công nghệ Thông tin đã, đang và sẽ là ngành mũi nhọn để thúc đẩy sự phát triển kinh tế đất nước, được Đảng và Nhà nước đặc biệt quan tâm phát triển. Tại các nước phát triển, ngành CNTT đóng một vai trò rất quan trọng trong sự phát triển kinh tế, xã hội… Do đó, sinh viên tốt nghiệp ngành công nghệ thông tin hứa hẹn những cơ hội nghề nghiệp phong phú, công việc làm ổn định tại các doanh nghiệp trong và ngoài nước với nguồn thu nhập hấp dẫn.\n\nSinh viên tốt nghiệp có cơ hội làm việc tại các doanh nghiệp, công ty có ứng dụng trực tiếp hoặc gián tiếp đến lĩnh vực CNTT tại Việt Nam và cả ở một số nước tiên tiến trên Thế Giới (ví dụ: Nhật Bản, …).\n\nSinh viên tốt nghiệp có thể tham gia nghiên cứu khoa học với vai trò Nghiên cứu viên, giảng dạy tại các cơ sở Trung cấp chuyên nghiệp, Cao đẳng, Đại học,... về CNTT.\n\nSinh viên tốt nghiệp từ Khoa Công nghệ Thông tin - Trường Đại Học Mở ngày nay có mặt khắp mọi miền đất nước trong nhiều lĩnh vực khác nhau với nhiều cương vị từ lập trình viên; phát triển phần mềm; nhân viên tin học; quản trị hệ thống; chuyên viên trong các cơ quan kinh tế, sản xuất, quản lý,…; kinh doanh…; đến lãnh đạo và giảng dạy tại các cơ sở giáo dục Cao đẳng – Đại học,…\n\nPhần lớn các sinh viên ra trường có việc làm sau khi ra trường.Theo thống kê trong nhiều năm liền, tỉ lệ sinh viên của Khoa Công Nghệ Thông Tin – Trường Đại Học Mở Tp. Hồ Chí Minh sau khi tốt nghiệp có việc làm đúng ngành nghề là hơn 90%. Trong đó có đến gần 47% sinh viên có việc làm trước khi tốt nghiệp. Mức lương trung bình cho sinh viên mới tốt nghiệp từ khoảng 8 – 10 triệu đồng/tháng (có 21% lương trên 10 triệu đồng). Sau khi làm việc 2-5 năm, có thể thu nhập từ 700 USD đến 1200 USD/tháng. Một số trường hợp xuất sắc còn có mức thu nhập cao hơn rất nhiều.',''),(1,1708224247785,63,NULL,1,4,'Các thông tin về tuyển sinh, tra cứu kết quả nộp trúng tuyển, phương thức tuyển sinh cụ thể, vui lòng truy cập vào Cổng thông tin Tuyển sinh - Trường Đại học Mở TP. Hồ Chí Minh tại địa chỉ https://tuyensinh.ou.edu.vn/\n\nHoặc theo dõi thông tin tuyển sinh mới nhất trên Facebook: https://www.facebook.com/tuyensinh.ou.edu.vn',''),(1,1708224289976,64,NULL,1,14,'Về các phương thức tuyển sinh của Trường Đại học Mở thành phố Hồ Chí Minh, vui lòng xem thêm tại đường dẫn sau: \n1. Chương trình đại trà: https://tuyensinh.ou.edu.vn/thong-tin-tuyen-sinh-nam-2023-truong-dai-hoc-mo-tp-hcm-ma-truong-mbs\n2. Chương trình chất lượng cao: https://tuyensinh.ou.edu.vn/gioi-thieu-khoa-dao-tao-dac-biet-chuong-trinh-chat-luong-cao',''),(1,1708224720800,65,NULL,53,11,'Điểm xét tuyển của ngành Khoa học máy tính (Chất lượng cao) - Trường Đại học Mở TP. HCM như sau:\n1. Theo phương thức thi tốt nghiệp THPT của Bộ Giáo dục và Đào tạo:\n+ Năm 2023: 22.70\n+ Năm 2022: 24.30\n+ Năm 2021: 24.00\n2. Theo phương thức xét tuyển học bạ THPT:\n+ Năm 2023: Nhận hết HSG + Học bạ có CCNN + Học bạ: 25.3\n+ Năm 2022: 26.50\n+ Năm 2021: 20.00\n\nXem thêm thông tin điểm xét tuyển các năm tại đây: https://tuyensinh.ou.edu.vn/diem-trung-tuyen-2017-2019-1581928239',''),(1,1708224790243,66,NULL,53,12,'Chương trình cử nhân ngành Khoa học máy tính (KHMT) chất lượng cao (CLC) được xây dựng nhằm đào tạo sinh viên đạt tiêu chuẩn chất lượng cao hơn chương trình đại trà, từng bước tiếp cận với chất lương đào tạo của các đại học tiên tiến khác trên thế giới. Trong đó các lĩnh vực đang nóng và rất “khát” nhân lực trong ngành Khoa học máy tính được được chú trọng đào tạo chuyên sâu bởi các giảng viên có nhiều năm kinh nghiệm như Trí tuệ nhân tạo, máy học, khoa học dữ liệu. Dưới đây là một số thông tin tổng quát về ngành Khoa học máy tính (Chất lượng cao) của Khoa Công nghệ thông tin - Trường Đại học Mở TP. Hồ Chí Minh: \n1. Tên ngành: Khoa học máy tính/Computer Science. \n2. Mã ngành: 7480101C. \n3. Trình độ đào tạo: Đại học. \n4. Hình thức đào tạo: Chính quy. \n5. Thời gian đào tạo: 4 năm. \n6. Khối lượng kiến thức toàn khóa (tổng số tín chỉ): 141 tín chỉ. \n7. Văn bằng tốt nghiệp: Cử nhân. \n\nNgoài ra, có thể tham khảo thêm thông tin tại đường dẫn: http://it.ou.edu.vn/pages/view/6-chuong-trinh-dao-tao',''),(1,1708225053953,67,NULL,53,13,'Các môn xét tuyển của ngành Khoa học máy tính (Chất lượng cao) - Trường Đại học Mở TP. Hồ Chí Minh bao gồm:\n1. Khối A00: Toán, Lý, Hóa.\n2. Khối A01: Toán, Lý, Anh.\n3. Khối D01: Toán, Văn, Anh.\n4. Khối D07: Toán, Hóa, Anh.\n\nMôn Toán ở tất cả tổ hợp trên đều được nhân hệ số 2.',''),(1,1708225113442,68,NULL,53,10,'Sau khi hoàn thành chương trình học, cử nhân ngành KHMT CLC có phẩm chất đạo đức, có kỷ luật và có trách nhiệm trong công việc, trình độ chuyên môn vững vàng, kỹ năng thực hành thành thảo, khả năng nghiên cứu khoa học, năng lực sáng tạo cao, áp dụng thành quả nghiên cứu khoa học vào thực tiễn, phân tích, giải quyết các vấn thực tiễn, cạnh tranh, tự tin trong giao tiếp, sử dụng ngoại ngữ chuyên môn thành thạo. Cử nhân ngành KHMT CLC có khả năng áp dụng các kiến thức chuyên môn giải quyết các vấn đề về Công nghệ Thông tin.',''),(1,1708225164505,69,NULL,52,11,'Điểm xét tuyển của ngành Khoa học máy tính - Trường Đại học Mở TP. HCM như sau:\n1. Theo phương thức thi tốt nghiệp THPT của Bộ Giáo dục và Đào tạo:\n+ Năm 2023: 24.00\n+ Năm 2022: 24.50\n+ Năm 2021: 25.55\n+ Năm 2020: 23.00\n+ Năm 2019: 19.20\n+ Năm 2018: 17.00\n+ Năm 2017: 20.75\n+ Năm 2016: 20.00\n2. Theo phương thức xét tuyển học bạ THPT:\n+ Năm 2023: Nhận hết HSG + Học bạ có CCNN + Học bạ: 28.00\n+ Năm 2022: 27.25 (HSG)\n+ Năm 2021: 25.25\n+ Năm 2020: 23.80\n+ Năm 2019: 25.50\n\nXem thêm thông tin điểm tuyển sinh các năm tại đây: https://tuyensinh.ou.edu.vn/diem-trung-tuyen-2017-2019-1581928239',''),(1,1708225209724,70,NULL,52,12,'Ngành Khoa học máy tính đào tạo các cử nhân có kiến thức cơ bản và chuyên sâu về khoa học máy tính, đáp ứng các yêu cầu về nghiên cứu phát triển và hiện thực các ứng dụng công nghệ thông tin (CNTT).\n\nDưới đây là một số thông tin tổng quát về ngành Khoa học máy tính của Khoa Công nghệ thông tin - Trường Đại học Mở TP. Hồ Chí Minh:\n1. Tên ngành: Khoa học máy tính/Computer Science.\n2. Mã ngành: 7480101.\n3. Trình độ đào tạo: Đại học.\n4. Hình thức đào tạo: Chính quy.\n5. Thời gian đào tạo: 4 năm.\n6. Khối lượng kiến thức toàn khóa (tổng số tín chỉ): 127 tín chỉ.\n7. Văn bằng tốt nghiệp: Cử nhân.\n8. Ngôn ngữ đào tạo: Tiếng Việt.\n\nNgoài ra, có thể tham khảo thêm thông tin tại đường dẫn: http://it.ou.edu.vn/pages/view/6-chuong-trinh-dao-tao',''),(1,1708225246131,71,NULL,52,13,'Các môn xét tuyển của ngành Khoa học máy tính - Trường Đại học Mở TP. Hồ Chí Minh bao gồm:\n1. Khối A00: Toán, Lý, Hóa.\n2. Khối A01: Toán, Lý, Anh.\n3. Khối D01: Toán, Văn, Anh.\n4. Khối D07: Toán, Hóa, Anh.\n\nMôn Toán ở tất cả tổ hợp trên đều được nhân hệ số 2.',''),(1,1708225275827,72,NULL,52,10,'Sinh viên tốt nghiệp ngành Khoa học máy tính có khả năng làm việc với tư cách là lập trình viên, nhân viên tin học, chuyên viên máy tính, cố vấn về CNTT tại các doanh nghiệp, quản trị hệ thống mạng, người phân tích thiết kế và triển khai hệ thống mạng, quản lý dự án, nghiên cứu viên, giảng viên tại các trường Trung cấp chuyên nghiệp – Cao đẳng – Đại học, kinh doanh trong lĩnh vực CNTT. Có 3 hướng chuyên ngành với định hướng đặc thù riêng:\n\n1. Hướng Mạng máy tính: Sinh viên có thể đảm nhận các công việc như: chuyên viên lập trình các ứng dụng mạng, chuyên viên thiết kế - triển khai mạng và chuyên viên quản trị hệ thống mạng cho các doanh nghiệp.\n2. Hướng Công nghệ phần mềm: Sinh viên có thể đảm nhận các công việc như: lập trình/sản xuất phần mềm, chuyên viên phát triển phần mềm, chuyên viên triển khai và ứng dụng các hệ thống thông tin cho việc quản lý kinh tế, dịch vụ, sản xuất và hành chính; thiết kế và quản trị cơ sở dữ liệu.\n3. Hướng Trí tuệ nhân tạo: Sinh viên theo hướng này sẽ được đào tạo về các kiến thức và kỹ năng liên quan đến trí tuệ nhân tạo, bao gồm việc phát triển và triển khai các hệ thống trí tuệ nhân tạo, xử lý ngôn ngữ tự nhiên, thị giác máy tính, học máy, và các ứng dụng khác trong lĩnh vực Công nghệ Thông tin. Có khả năng tham gia vào việc phát triển ứng dụng trí tuệ nhân tạo cho các lĩnh vực như dự đoán và phân tích dữ liệu, hệ thống tự động hóa, và tối ưu hóa quyết định.\n4. Hướng Khoa học dữ liệu: Sinh viên theo hướng này sẽ tập trung vào việc thu thập, xử lý, và phân tích dữ liệu lớn để đưa ra thông tin quan trọng và hỗ trợ quyết định. Có khả năng sử dụng các công cụ và phương pháp khoa học dữ liệu để hiểu rõ hơn về xu hướng và mô hình trong dữ liệu, từ đó giúp doanh nghiệp đưa ra những chiến lược và quyết định có tính chiến lược.',''),(1,1708225371523,73,NULL,55,11,'Điểm xét tuyển của ngành Hệ thống thông tin quản lý - Trường Đại học Mở TP. HCM như sau:\n1. Theo phương thức thi tốt nghiệp THPT của Bộ Giáo dục và Đào tạo:\n+ Năm 2023: 23.70\n+ Năm 2022: 23.50\n+ Năm 2021: 25.90\n+ Năm 2020: 23.20\n+ Năm 2019: 18.90\n+ Năm 2018: 16.10\n+ Năm 2017: 19.50\n+ Năm 2016: 15.00\n2. Theo phương thức xét tuyển học bạ THPT:\n+ Năm 2023: Nhận hết HSG + Học bạ có chứng chỉ ngoại ngữ + Học bạ: 27.70\n+ Năm 2022: Chỉ nhận HSG\n+ Năm 2021: 22.75\n+ Năm 2020: 20.00\n+ Năm 2019: 21.50\n\nXem thêm thông tin điểm tuyển sinh các năm tại đây: https://tuyensinh.ou.edu.vn/diem-trung-tuyen-2017-2019-1581928239',''),(1,1708225411670,74,NULL,55,12,'Ngành Hệ thống thông tin quản lý đào tạo cử nhân có phẩm chất đạo đức, được trang bị đầy đủ khối kiến thức giáo dục đại cương, các vấn đề lý thuyết và các kỹ năng thực hành cơ bản của ngành Công nghệ Thông tin, các kiến thức về kinh tế và quản trị, nắm vững về Tin học quản lý. Dưới đây là một số thông tin tổng quát về ngành Hệ thống thông tin quản lý của Khoa Công nghệ thông tin - Trường Đại học Mở TP. Hồ Chí Minh: 1. Tên ngành: Hệ thống thông tin quản lý/Management Information System. 2. Mã ngành: 7340405. 3. Trình độ đào tạo: Đại học. 4. Hình thức đào tạo: Chính quy. 5. Thời gian đào tạo: 4 năm. 6. Khối lượng kiến thức toàn khóa (tổng số tín chỉ): 131 tín chỉ. 7. Văn bằng tốt nghiệp: Cử nhân. 8. Ngôn ngữ đào tạo: Tiếng Việt. Ngoài ra, có thể tham khảo thêm thông tin tại đường dẫn: http://it.ou.edu.vn/pages/view/6-chuong-trinh-dao-tao',''),(1,1708225450537,75,NULL,55,13,'Các môn xét tuyển của ngành Hệ thống thông tin quản lý - Trường Đại học Mở TP. Hồ Chí Minh bao gồm:\n1. Khối A00: Toán, Lý, Hóa.\n2. Khối A01: Toán, Lý, Anh.\n3. Khối D01: Toán, Văn, Anh.\n4. Khối D07: Toán, Hóa, Anh.',''),(1,1708225488033,76,NULL,55,10,'Ngành Hệ thống thông tin quản lý - Trường Đại học Mở TP. HCM có 2 hướng sau khi tốt nghiệp.\n1. Hướng quản lý: Sinh viên có thể làm việc tại vị trí Chuyên viên của phòng ban chức năng như Phòng kinh doanh, Phòng kế hoạch, Phòng Marketing, Phòng tổ chức, Phòng hỗ trợ - giao dịch khách hàng, Phòng nhân sự hoặc Trợ lý cho các nhà quản lý các cấp trong bộ máy quản lý,... Có thể trở thành Doanh nhân hay Giám đốc điều hành, Giám đốc bộ phận tại các tập đoàn, công ty trong và ngoài nước. Một số vị trí tiêu biểu như sau:\n- Marketing quản cáo - eMarketing, SEO.\n- Bán hàng - Point of Sale.\n- Dịch vụ khách hàng - CRM.\n- Quản lý chuỗi cung ứng - SCM.\n- Hoạch định nguồn lực - ERP.\n- Nhân sự, tuyển dụng - HRM.\n2. Hướng công nghệ thông tin: Sinh viên có khả năng làm chuyên viên công nghệ thông tin, chuyên viên quản trị cơ sở dữ liệu, chuyên viên tích hợp hệ thống, chuyên viên tư vấn thực hiện việc tổ chức và phát triển các ứng dụng công nghệ thông tin trong hoạt động của các tổ chức kinh tế - xã hội; có khả năng quản lý các doanh nghiệp công nghệ thông tin, cán bộ quản lý dự án; lập trình viên; làm nghiên cứu- giảng viên. Một số vị trí tiêu biểu như sau:\n- Chuyên viên quản trị, vận hành hệ thống (System Administrator, Operator).\n- Chuyên viên quản trị CSDL (DB Administrator).\n- Chuyên viên quản trị hệ thống Web, Thương mại điện tử (Web Admin).\n- Chuyên viên phân tích nghiệp vụ (Business Analyst).\n- Chuyên viên phân tích hệ thống (System Analyst).\n- Chuyên viên phát triển phần mềm (Software Developer).\n- Chuyên gia huấn luyện CNTT trong doanh nghiệp (IT Trainer).\n- Chuyên gia tư vấn triển khai ERP (ERP Consultant).\n- Chuyên gia tư vấn, cố vấn CNTT (IT Consultant).\n- Giám đốc công nghệ thông tin (CIO).',''),(1,1708225563669,77,NULL,56,12,'Ngành Công nghệ thông tin tăng cường tiếng Nhật đào tạo cử nhân có phẩm chất chính trị và ý thức trách nhiệm đối với xã hội, có sức khỏe, kiến thức và năng lực chuyên môn trong lĩnh vực ngành Công nghệ thông tin, có khả năng làm việc trong môi trường sử dụng tiếng Anh và tiếng Nhật để phát triển nghề nghiệp và bản thân trong bối cảnh hội nhập và phát triển của đất nước.\n\nDưới đây là một số thông tin tổng quát về ngành Công nghệ thông tin (tăng cường tiếng Nhật) của Khoa Công nghệ thông tin - Trường Đại học Mở TP. Hồ Chí Minh:\n1. Tên ngành: Công nghệ thông tin (Tăng cường tiếng Nhật).\n2. Mã ngành: 7480201.\n3. Trình độ đào tạo: Đại học.\n4. Hình thức đào tạo: Chính quy.\n5. Thời gian đào tạo: 4 năm.\n6. Khối lượng kiến thức toàn khóa (tổng số tín chỉ): 142 tín chỉ.\n7. Văn bằng tốt nghiệp: Cử nhân.\n8. Ngôn ngữ đào tạo: Tiếng Việt.\n\nNgoài ra, có thể tham khảo thêm thông tin tại đường dẫn: http://it.ou.edu.vn/pages/view/6-chuong-trinh-dao-tao',''),(1,1708226158364,78,NULL,56,10,'Sau khi học xong chương trình, cử nhân ngành Công nghệ thông tin tăng cường tiếng Nhật có thể làm việc ở các vị trí sau:\n- Lập trình viên phát triển phần mềm, ứng dụng trong lĩnh vực được đào tạo (Programmer/Software Developer).\n- Chuyên viên quản trị và vận hành các hệ thống thông tin, hệ thống Cơ sở dữ liệu, hệ thống Web. \n- Chuyên viên phân tích – khai phá dữ liệu.\n- Chuyên viên thiết kế, triển khai, quản trị hệ thống mạng.\n- Chuyên viên an toàn và bảo mật thông tin.',''),(1,1708226216299,79,NULL,54,11,'Điểm xét tuyển của ngành Công nghệ thông tin - Trường Đại học Mở TP. HCM như sau:\n1. Theo phương thức thi tốt nghiệp THPT của Bộ Giáo dục và Đào tạo:\n+ Năm 2023: 24.50\n+ Năm 2022: 25.40\n+ Năm 2021: 26.10\n+ Năm 2020: 24.50\n+ Năm 2019: 20.85\n+ Năm 2018: 18.25\n2. Theo phương thức xét tuyển học bạ THPT:\n+ Năm 2023: Chỉ nhận HSG: 26.20\n+ Năm 2022: 27.25 (HSG)\n+ Năm 2021: 25.25\n+ Năm 2020: 23.80\n+ Năm 2019: 25.50\n\nXem thêm thông tin điểm tuyển sinh các năm tại đây: https://tuyensinh.ou.edu.vn/diem-trung-tuyen-2017-2019-1581928239',''),(1,1708226243385,80,NULL,54,12,'Ngành Công nghệ thông tin đào tạo các cử nhân có phẩm chất đạo đức, có kỷ luật và có trách nhiệm trong công việc; được trang bị đầy đủ khối kiến thức giáo dục đại cương, nắm vững các kiến thức chuyên môn; có khả năng vận hành, quản lý, phát triển các ứng dụng công nghệ thông tin; đáp ứng nhu cầu nhân lực Công nghệ thông tin trong các lĩnh vực chuyên môn khác nhau.\n\nDưới đây là một số thông tin tổng quát về ngành Công nghệ thông tin của Khoa Công nghệ thông tin - Trường Đại học Mở TP. Hồ Chí Minh:\n1. Tên ngành: Công nghệ thông tin/Information Technologies.\n2. Mã ngành: 7480201.\n3. Trình độ đào tạo: Đại học.\n4. Hình thức đào tạo: Chính quy.\n5. Thời gian đào tạo: 4 năm.\n6. Khối lượng kiến thức toàn khóa (tổng số tín chỉ): 127 tín chỉ.\n7. Văn bằng tốt nghiệp: Cử nhân.\n8. Ngôn ngữ đào tạo: Tiếng Việt.\n\nNgoài ra, có thể tham khảo thêm thông tin tại đường dẫn: http://it.ou.edu.vn/pages/view/6-chuong-trinh-dao-tao',''),(1,1708226305213,81,NULL,54,13,'Các môn xét tuyển của ngành Công nghệ thông tin - Trường Đại học Mở TP. Hồ Chí Minh bao gồm:\n1. Khối A00: Toán, Lý, Hóa.\n2. Khối A01: Toán, Lý, Anh.\n3. Khối D01: Toán, Văn, Anh.\n4. Khối D07: Toán, Hóa, Anh.\n\nMôn Toán ở tất cả tổ hợp trên đều được nhân hệ số 2.',''),(1,1708226355578,82,NULL,54,10,'Sau khi học xong chương trình, cử nhân ngành Công nghệ thông tin có thể làm việc ở các vị trí sau:\n- Lập trình viên/Chuyên viên phát triển phần mềm (Programmer/Software Developer).\n- Chuyên viên quản trị, vận hành hệ thống (System Administrator).\n- Chuyên viên thiết kế hệ thống mạng, quản trị hệ thống mạng (Network Administrator).\n- Chuyên viên khai thác dữ liệu và thông tin ứng dụng cho các doanh nghiệp trong vấn đề phân tích định lượng, hoặc quản trị Cơ sở dữ liệu (Database Administrator).\n- Chuyên viên quản trị hệ thống Web, Thương mại điện tử (Web Administrator).\n- Chuyên viên lập trình ứng dụng đồ họa và Game, Chuyên viên công nghệ thông tin trong lãnh vực quảng cáo/phim (Graphics Programmer in the games).\n- Chuyên gia huấn luyện Công nghệ thông tin trong doanh nghiệp, giảng dạy (IT Trainer).\n- Chuyên gia tư vấn, cố vấn Công nghệ thông tin (IT Consultant).','');
/*!40000 ALTER TABLE `information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `information_seq`
--

DROP TABLE IF EXISTS `information_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `information_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `information_seq`
--

LOCK TABLES `information_seq` WRITE;
/*!40000 ALTER TABLE `information_seq` DISABLE KEYS */;
INSERT INTO `information_seq` VALUES (151);
/*!40000 ALTER TABLE `information_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `links`
--

DROP TABLE IF EXISTS `links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `links` (
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `scope_id` bigint NOT NULL,
  `link` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `FK69mwj9sw5b6ldgfugotxtpvfl` (`scope_id`),
  CONSTRAINT `FK69mwj9sw5b6ldgfugotxtpvfl` FOREIGN KEY (`scope_id`) REFERENCES `scopes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `links`
--

LOCK TABLES `links` WRITE;
/*!40000 ALTER TABLE `links` DISABLE KEYS */;
/*!40000 ALTER TABLE `links` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `links_seq`
--

DROP TABLE IF EXISTS `links_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `links_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `links_seq`
--

LOCK TABLES `links_seq` WRITE;
/*!40000 ALTER TABLE `links_seq` DISABLE KEYS */;
INSERT INTO `links_seq` VALUES (1);
/*!40000 ALTER TABLE `links_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `majors`
--

DROP TABLE IF EXISTS `majors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `majors` (
  `created_date` bigint NOT NULL,
  `faculty_id` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `FKitqtm0b9li7x2h872rumqnqol` (`faculty_id`),
  CONSTRAINT `FKitqtm0b9li7x2h872rumqnqol` FOREIGN KEY (`faculty_id`) REFERENCES `faculties` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `majors`
--

LOCK TABLES `majors` WRITE;
/*!40000 ALTER TABLE `majors` DISABLE KEYS */;
INSERT INTO `majors` VALUES (1708160302888,1,1,NULL,'Khoa học máy tính',NULL),(1708160302894,1,2,NULL,'Công nghệ thông tin',NULL),(1708160302897,1,3,NULL,'Hệ thống thông tin quản lý',NULL);
/*!40000 ALTER TABLE `majors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `majors_seq`
--

DROP TABLE IF EXISTS `majors_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `majors_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `majors_seq`
--

LOCK TABLES `majors_seq` WRITE;
/*!40000 ALTER TABLE `majors_seq` DISABLE KEYS */;
INSERT INTO `majors_seq` VALUES (101);
/*!40000 ALTER TABLE `majors_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scopes`
--

DROP TABLE IF EXISTS `scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scopes` (
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_rrd68urclfqxbnb98jwgxltak` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scopes`
--

LOCK TABLES `scopes` WRITE;
/*!40000 ALTER TABLE `scopes` DISABLE KEYS */;
INSERT INTO `scopes` VALUES (1708160303042,1,NULL,'university','Phạm vi trường đại học',NULL),(1708160303051,2,NULL,'faculty','Phạm vi khoa',NULL),(1708160303054,3,NULL,'major','Phạm vi ngành',NULL),(1708224452023,52,NULL,'major-cs','Phạm vi ngành Khoa học máy tính','Khoa Công nghệ thông tin'),(1708224482710,53,NULL,'major-cs-hq','Phạm vi ngành Khoa học máy tính CLC','Khoa Đào tạo đặc biệt'),(1708224501855,54,NULL,'major-it','Phạm vi ngành Công nghệ thông tin','Khoa Công nghệ thông tin'),(1708224534244,55,NULL,'major-im','Phạm vi ngành Hệ thống thông tin quản lý','Khoa Công nghệ thông tin'),(1708225538990,56,NULL,'major-it-japan','Phạm vi ngành Công nghệ thông tin (Tăng cường tiếng Nhật)','Khoa Công nghệ thông tin');
/*!40000 ALTER TABLE `scopes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scopes_seq`
--

DROP TABLE IF EXISTS `scopes_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scopes_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scopes_seq`
--

LOCK TABLES `scopes_seq` WRITE;
/*!40000 ALTER TABLE `scopes_seq` DISABLE KEYS */;
INSERT INTO `scopes_seq` VALUES (151);
/*!40000 ALTER TABLE `scopes_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topics` (
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_7tuhnscjpohbffmp7btit1uff` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics`
--

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;
INSERT INTO `topics` VALUES (1708160302954,1,NULL,'Thông tin liên hệ','contact-info',NULL),(1708160302960,2,NULL,'Văn bằng tốt nghiệp','diploma',NULL),(1708160302964,3,NULL,'Thông tin cao học (Thạc sĩ, Tiến sĩ)','higher-study-chance',NULL),(1708160302969,4,NULL,'Liên kết','links',NULL),(1708160302976,5,NULL,'Thông tin về các ngành của khoa','majors',NULL),(1708160302981,6,NULL,'Thông tin về điều kiện tuyển sinh đầu vào','prerequisite-admission',NULL),(1708160302986,7,NULL,'Thông tin về các hoạt động sinh viên','student-activity',NULL),(1708160302991,8,NULL,'Thông tin về cơ sở học tập','study-place',NULL),(1708160302995,9,NULL,'Thông tin về chương trình đào tạo','study-program',NULL),(1708160303001,10,1708224142104,'Thông tin về cơ hội nghề nghiệp','work-chance',''),(1708160303006,11,1708222928581,'Điểm tuyển sinh đầu vào','entrance-score',''),(1708160303009,12,NULL,'Thông tin tổng quát','general-info',NULL),(1708160303013,13,NULL,'Thông tin về tổ hợp xét tuyển','subject-combination',NULL),(1708160303016,14,NULL,'Thông tin phương thức tuyển sinh','entrance-method',NULL);
/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topics_seq`
--

DROP TABLE IF EXISTS `topics_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topics_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics_seq`
--

LOCK TABLES `topics_seq` WRITE;
/*!40000 ALTER TABLE `topics_seq` DISABLE KEYS */;
INSERT INTO `topics_seq` VALUES (151);
/*!40000 ALTER TABLE `topics_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `is_enabled` bit(1) NOT NULL,
  `created_date` bigint NOT NULL,
  `id` bigint NOT NULL,
  `last_modified_date` bigint DEFAULT NULL,
  `first_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` tinytext COLLATE utf8mb4_unicode_ci,
  `role` enum('ROLE_USER','ROLE_ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary '',1708160302186,1,NULL,'Admin','admin@gmail.com','Account','$2a$10$ASqVO7Vbt34.NNvUJbSxQ.x9EqU2relLB70GGOLIckuGmIArQQAaO',NULL,'ROLE_ADMIN'),(_binary '',1708160302292,2,NULL,'Anh','anh@gmail.com','Nguyen','$2a$10$hKbWpsKf.B5pYT.sqpK46.HnKymCkewtQ6JItmknDTTsnQEWC247K',NULL,'ROLE_USER'),(_binary '',1708160302352,3,NULL,'Duy','duy@gmail.com','Le','$2a$10$Hr07m7JTUHOJvcn.KlPEU.nI3wgx08IIchVaKtiiMy8RbOmmixJtW',NULL,'ROLE_USER'),(_binary '',1708160302413,4,NULL,'Dat','dat@gmail.com','Luong','$2a$10$AZd3RdcdUmSI5Yj4olvCE.qU08xLtzJx7Y9nVu2DRWo2gzS9E1P0y',NULL,'ROLE_USER'),(_binary '',1708160302473,5,NULL,'Linh','linh@gmail.com','Vu','$2a$10$.61igyi8JxD.f1uNfQ7UK.7g6mDGdg54FV4LqxX0h4TibdaQh6qja',NULL,'ROLE_USER'),(_binary '\0',1708160302535,6,NULL,'Tien','tien@gmail.com','Sinh','$2a$10$g0XQlcU5dBX33L71MPOTOeLlnzAIMJClrN91KtHYVpOHrcq7OFBta',NULL,'ROLE_USER'),(_binary '',1708160302597,7,NULL,'Mai','mai@gmail.com','Dang','$2a$10$HE8gzg94XuGMDITpvzJPJebfSE.OiLlVbgAyC/jTSDX1wILOAZyCO',NULL,'ROLE_USER'),(_binary '\0',1708160302659,8,NULL,'Nhung','nhung@gmail.com','Trang','$2a$10$.MuqVdVnZ.IHGNx06fLcQeC5yYraNZ5bYiO0jJ2jBkkiPnLRzGMXS',NULL,'ROLE_USER'),(_binary '',1708160302724,9,NULL,'Binh','binh@gmail.com','Nhu','$2a$10$4sut/lJsRyRZwNZmbuh3hOCBdj2FyF2HMh8hh299trLlDL6RifbZ6',NULL,'ROLE_USER'),(_binary '',1708160302789,10,NULL,'Huong','huong@gmail.com','Pham','$2a$10$0TWhahx8tSAYcO8E8a7TEO00DZpEZib7CtleoSrtA7AEpfawMdvk2',NULL,'ROLE_USER'),(_binary '\0',1708160302854,11,NULL,'Thinh','thinh@gmail.com','Dinh','$2a$10$hHLj35T/VlRpaYRYj3zxO.9GmtPv3rc4JR42FYSlereV9x1tNCCD.',NULL,'ROLE_USER'),(_binary '',1708226512451,52,NULL,'Anh','an@gmail.com','Nguyen','$2a$10$cmSSkcvMj8RNScyKOwfd/.GErmU4rPMoV7jAOy65ZeN4w.iZmK7RO',NULL,'ROLE_USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_seq`
--

DROP TABLE IF EXISTS `users_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_seq`
--

LOCK TABLES `users_seq` WRITE;
/*!40000 ALTER TABLE `users_seq` DISABLE KEYS */;
INSERT INTO `users_seq` VALUES (151);
/*!40000 ALTER TABLE `users_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-18 14:52:56
