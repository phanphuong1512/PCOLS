-- MySQL dump 10.13  Distrib 8.0.41, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: pcshop
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authority` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `authority` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES (1,'ROLE_USER'),(2,'ROLE_ADMIN');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UKrdxh7tq2xs66r485cc8dkxt77` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brand`
--

LOCK TABLES `brand` WRITE;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` VALUES (5,'Asus'),(3,'E-DRA'),(1,'Intel'),(4,'LG'),(2,'Nvidia'),(8,'PC'),(6,'Razer'),(7,'SteelSeries');
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) NOT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (7,'AIO'),(6,'CASE'),(1,'CPU'),(12,'Gear'),(10,'Headset'),(8,'Keyboard'),(13,'Monitor'),(9,'Mouse'),(11,'PC'),(5,'PSU'),(3,'RAM'),(4,'SSD'),(2,'VGA');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `images` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `product_id` bigint NOT NULL,
                          `url` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKghwsjbjo7mg3iufxruvq6iu3q` (`product_id`),
                          CONSTRAINT `FKghwsjbjo7mg3iufxruvq6iu3q` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images`
--

LOCK TABLES `images` WRITE;
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
INSERT INTO `images` VALUES (1,1,'https://www.tncstore.vn/media/product/250-11383-cpu-core-ultra-9-285k--1-.jpg'),(2,2,'https://www.tncstore.vn/media/product/250-12004-tnc-store-card-man-hinh-msi-geforce-rtx-5070-12g-vanguard-soc--2-.jpg'),(3,3,'https://www.tncstore.vn/media/product/250-11384-cpu-core-ultra-7-265k--1-.jpg'),(4,4,'https://www.tncstore.vn/media/product/250-11386-cpu-core-ultra-5-245k--1-.jpg'),(5,5,'https://www.tncstore.vn/media/product/250-4625-card-man-hinh-galax-gtx-1650-super-ex-1-click-oc-01.jpg'),(6,6,'/uploads/79a55e32-d66e-4f74-8481-207f3f5b90d8_pc1.jpg'),(7,7,'https://www.tncstore.vn/media/product/250-11335-tnc-store-card-card-man-hinh-gigabyte-geforce-gt-1030-oc-2g-gv-n1030oc-2gi--2-.jpg'),(8,8,'https://www.tncstore.vn/media/product/250-11617-card-man-hinh-asus-rog-astral-geforce-rtx-5090-32gb-gddr7-oc-edition--5-.jpg'),(9,9,'https://www.tncstore.vn/media/product/250-9247-man-asus-4.jpg'),(10,10,'https://www.tncstore.vn/media/product/250-9073-man-hinh-gaming-lg-uttra-gear-27-gr75q-b-5.jpg'),(11,11,'https://www.tncstore.vn/media/product/250-11465-man-hinh-gaming-lg-24gs65f-b--6-.jpg'),(12,12,'https://www.tncstore.vn/media/product/250-8340-razer-deathadder-v3-pro-ergonomic-white.jpg'),(13,13,'https://www.tncstore.vn/media/product/250-4854-ban-phim-co-e-dra-ek387-v2-brown-switch-1a.jpg'),(14,14,'https://www.tncstore.vn/media/product/250-2316-tai-nghe-steelseries-arctis-pro-gamedac-11.jpg'),(15,15,'https://www.tncstore.vn/media/product/250-9979-pc-gaming-sentinel-i4090-wh--16-.jpg'),(16,16,'https://www.tncstore.vn/media/product/250-9835-tnc-store-pc-gaming-nova-i4080-super-bl-15-.jpg'),(17,12,'https://www.tncstore.vn/media/product/8340-tnc-store-chuot-razer-deathadder-v3-pro-wireless-white-21-.png'),(18,12,'https://www.tncstore.vn/media/product/8340-tnc-store-chuot-razer-deathadder-v3-pro-wireless-white-20-.png'),(19,12,'https://www.tncstore.vn/media/product/8340-razer-deathadder-v3-pro-ergonomic-white-1.jpg'),(20,12,'https://www.tncstore.vn/media/product/8340-razer-deathadder-v3-pro-ergonomic-white-2.jpg'),(21,12,'https://www.tncstore.vn/media/product/8340-razer-deathadder-v3-pro-ergonomic-white-3.jpg'),(22,10,'https://www.tncstore.vn/media/product/9073-man-hinh-gaming-lg-uttra-gear-27-gr75q-b-3.jpg'),(23,10,'https://www.tncstore.vn/media/product/9073-man-hinh-gaming-lg-uttra-gear-27-gr75q-b-4.jpg'),(24,10,'https://www.tncstore.vn/media/product/9073-tnc-store-man-hinh-gaming-lg-ultragear-27gr75q-b.jpg'),(25,10,'https://www.tncstore.vn/media/product/75-9073-man-hinh-gaming-lg-uttra-gear-27-gr75q-b-2.jpg'),(26,10,'https://www.tncstore.vn/media/product/9073-man-hinh-gaming-lg-uttra-gear-27-gr75q-b-1.jpg'),(27,13,'https://www.tncstore.vn/media/product/4854-2.jpg'),(28,13,'https://www.tncstore.vn/media/product/4854-3.jpg'),(29,13,'https://www.tncstore.vn/media/product/4854-4.jpg'),(30,14,'https://www.tncstore.vn/media/product/2316-tai-nghe-steelseries-arctis-pro-gamedac-22.jpg'),(31,14,'https://www.tncstore.vn/media/product/2316-tai-nghe-steelseries-arctis-pro-gamedac-3.jpg'),(32,14,'https://www.tncstore.vn/media/product/2316-tai-nghe-steelseries-arctis-pro-gamedac-4.jpg'),(33,6,'https://www.tncstore.vn/media/product/11739-pc-gaming-supernova-i5080--2-.jpg'),(34,6,'https://www.tncstore.vn/media/product/11739-pc-gaming-supernova-i5080--3-.jpg'),(35,6,'https://www.tncstore.vn/media/product/11739-pc-gaming-supernova-i5080--6-.jpg'),(36,6,'https://www.tncstore.vn/media/product/11739-pc-gaming-supernova-i5080--5-.jpg'),(37,6,'https://www.tncstore.vn/media/product/11739-pc-gaming-supernova-i5080--4-.jpg'),(38,15,'https://www.tncstore.vn/media/product/9979-pc-gaming-sentinel-i4090-wh--4-.jpg'),(39,15,'https://www.tncstore.vn/media/product/9979-pc-gaming-sentinel-i4090-wh--3-.jpg'),(40,15,'https://www.tncstore.vn/media/product/75-9979-pc-gaming-sentinel-i4090-wh--2-.jpg'),(41,15,'https://www.tncstore.vn/media/product/75-9979-pc-gaming-sentinel-i4090-wh--1-.jpg'),(42,16,'https://www.tncstore.vn/media/product/9835-pc-gaming-nova-i4080-super-bl--11-.png'),(43,16,'https://www.tncstore.vn/media/product/75-9835-pc-gaming-nova-i4080-super-bl--12-.png'),(44,16,'https://www.tncstore.vn/media/product/75-9835-pc-gaming-nova-i4080-super-bl--15-.png'),(45,16,'https://www.tncstore.vn/media/product/75-9835-pc-gaming-nova-i4080-super-bl--14-.png'),(46,16,'https://www.tncstore.vn/media/product/75-9835-pc-gaming-nova-i4080-super-bl--13-.png'),(47,9,'https://www.tncstore.vn/media/product/75-9247-tnc-store-man-hinh-gaming-rog-strix-xg27aqmr--2-.jpg'),(48,9,'https://www.tncstore.vn/media/product/75-9247-tnc-store-man-hinh-gaming-rog-strix-xg27aqmr--1-.jpg'),(49,9,'https://www.tncstore.vn/media/product/75-9247-man-asus-1.jpg'),(50,9,'https://www.tncstore.vn/media/product/75-9247-man-asus-2.jpg'),(51,9,'https://www.tncstore.vn/media/product/75-9247-man-asus-3.jpg'),(52,11,'https://www.tncstore.vn/media/product/75-11465-man-hinh-gaming-lg-24gs65f-b--2-.jpg'),(53,11,'https://www.tncstore.vn/media/product/75-11465-man-hinh-gaming-lg-24gs65f-b--1-.jpg'),(54,11,'https://www.tncstore.vn/media/product/75-11465-man-hinh-gaming-lg-24gs65f-b--5-.jpg'),(55,11,'https://www.tncstore.vn/media/product/75-11465-man-hinh-gaming-lg-24gs65f-b--4-.jpg'),(56,11,'https://www.tncstore.vn/media/product/75-11465-man-hinh-gaming-lg-24gs65f-b--3-.jpg'),(57,2,'https://www.tncstore.vn/media/product/75-12004-tnc-store-card-man-hinh-msi-geforce-rtx-5070-12g-vanguard-soc--6-.jpg'),(58,2,'https://www.tncstore.vn/media/product/75-12004-tnc-store-card-man-hinh-msi-geforce-rtx-5070-12g-vanguard-soc--5-.jpg'),(59,2,'https://www.tncstore.vn/media/product/75-12004-tnc-store-card-man-hinh-msi-geforce-rtx-5070-12g-vanguard-soc--4-.jpg'),(60,2,'https://www.tncstore.vn/media/product/75-12004-tnc-store-card-man-hinh-msi-geforce-rtx-5070-12g-vanguard-soc--3-.jpg'),(61,2,'https://www.tncstore.vn/media/product/75-12004-tnc-store-card-man-hinh-msi-geforce-rtx-5070-12g-vanguard-soc--1-.jpg'),(62,5,'https://www.tncstore.vn/media/product/75-4625-card-man-hinh-galax-gtx-1650-super-ex-1-click-oc-02.jpg'),(63,5,'https://www.tncstore.vn/media/product/75-4625-card-man-hinh-galax-gtx-1650-super-ex-1-click-oc-04.jpg'),(64,5,'https://www.tncstore.vn/media/product/75-4625-card-man-hinh-galax-gtx-1650-super-ex-1-click-oc-03.jpg'),(65,7,'https://www.tncstore.vn/media/product/75-11335-tnc-store-card-card-man-hinh-gigabyte-geforce-gt-1030-oc-2g-gv-n1030oc-2gi--3-.jpg'),(66,7,'https://www.tncstore.vn/media/product/75-11335-tnc-store-card-card-man-hinh-gigabyte-geforce-gt-1030-oc-2g-gv-n1030oc-2gi--1-.jpg'),(67,7,'https://www.tncstore.vn/media/product/75-11335-tnc-store-card-card-man-hinh-gigabyte-geforce-gt-1030-oc-2g-gv-n1030oc-2gi--4-.jpg'),(68,8,'https://www.tncstore.vn/media/product/75-11617-card-man-hinh-asus-rog-astral-geforce-rtx-5090-32gb-gddr7-oc-edition--6-.jpg'),(69,8,'https://www.tncstore.vn/media/product/75-11617-card-man-hinh-asus-rog-astral-geforce-rtx-5090-32gb-gddr7-oc-edition--1-.jpg'),(70,8,'https://www.tncstore.vn/media/product/75-11617-card-man-hinh-asus-rog-astral-geforce-rtx-5090-32gb-gddr7-oc-edition--3-.jpg'),(71,8,'https://www.tncstore.vn/media/product/75-11617-card-man-hinh-asus-rog-astral-geforce-rtx-5090-32gb-gddr7-oc-edition--2-.jpg'),(72,8,'https://www.tncstore.vn/media/product/75-11617-card-man-hinh-asus-rog-astral-geforce-rtx-5090-32gb-gddr7-oc-edition--4-.jpg');
/*!40000 ALTER TABLE `images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
                                 `price` decimal(38,2) NOT NULL,
                                 `quantity` int NOT NULL,
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `order_id` bigint NOT NULL,
                                 `product_id` bigint NOT NULL,
                                 `product_name` varchar(255) NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
                                 KEY `FK4q98utpd73imf4yhttm3w0eax` (`product_id`),
                                 CONSTRAINT `FK4q98utpd73imf4yhttm3w0eax` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
                                 CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (3200.00,12,3,1,12,'Razer DeathAdder V3 Pro');
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `order_date` datetime(6) NOT NULL,
                          `user_id` bigint NOT NULL,
                          `address` varchar(255) DEFAULT NULL,
                          `city` varchar(255) DEFAULT NULL,
                          `country` varchar(255) DEFAULT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `first_name` varchar(255) DEFAULT NULL,
                          `last_name` varchar(255) DEFAULT NULL,
                          `payment_method` varchar(255) DEFAULT NULL,
                          `phone` varchar(255) DEFAULT NULL,
                          `shipping_method` varchar(255) DEFAULT NULL,
                          `zip_code` varchar(255) DEFAULT NULL,
                          `status` enum('CANCELLED','PAID','PENDING','SHIPPED') NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
                          CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2025-03-24 02:05:56.091939',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'PENDING');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
                            `price` double NOT NULL,
                            `stock` int DEFAULT NULL,
                            `category_id` bigint DEFAULT NULL,
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `description` varchar(2000) DEFAULT NULL,
                            `name` varchar(255) NOT NULL,
                            `brand_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
                            KEY `FKl2cyj2st6mjygl2pgwd057ivu` (`brand_id`),
                            CONSTRAINT `FKl2cyj2st6mjygl2pgwd057ivu` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
                            CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (2000,1000,1,1,'CPU Intel Core Ultra 9 285K là bộ vi xử lý mạnh nhất thuộc dòng Intel Core Ultra thế hệ thứ 2, mang đến hiệu năng đỉnh cao cho các tác vụ đòi hỏi khắt khe nhất, từ chơi game AAA, sáng tạo nội dung đến xử lý dữ liệu phức tạp. Với 24 nhân, 24 luồng, xung nhịp tối đa lên đến 5.7GHz và bộ nhớ đệm 36MB, CPU Intel Core Ultra 9 285K sẽ là trái tim của hệ thống PC mạnh mẽ, đáp ứng mọi nhu cầu của bạn.','CPU Intel Core Ultra 9 285K',1),(2000,1000,2,2,'MSI GeForce RTX 5070 12G VANGUARD SOC','MSI GeForce RTX 5070 12G',2),(2000,1000,1,3,'CPU Intel Core Ultra 7 265K thuộc dòng CPU Arrow Lake-S thế hệ thứ 15, Intel Core Ultra 7 265K mang đến sự kết hợp giữa sức mạnh vượt trội và hiệu quả cao với 20 nhân, 20 luồng và khả năng đạt xung nhịp lên đến 5.5 GHz. Đây là lựa chọn lý tưởng cho những người dùng cần một CPU có khả năng xử lý tốt cả trong các tác vụ gaming lẫn công việc đồ họa hay xử lý video.','CPU Intel Core Ultra 7 265K',1),(2000,1000,1,4,'CPU Intel Core Ultra 5 245K là bộ vi xử lý mạnh mẽ thuộc dòng Intel Core Ultra thế hệ thứ 2, được thiết kế để mang đến hiệu năng vượt trội cho các tác vụ hàng ngày, từ làm việc văn phòng, học tập đến giải trí đa phương tiện. Với 14 nhân, 14 luồng, xung nhịp tối đa lên đến 5.2GHz và bộ nhớ đệm 24MB, CPU Intel Core Ultra 5 245K sẽ đáp ứng mọi nhu cầu của bạn một cách mượt mà và hiệu quả.','CPU Intel Core Ultra 5 245K',1),(2000,1000,2,5,'Card Màn Hình Galax Geforce GTX 1650 Super ( 1-Click OC ) là dòng VGA quốc dân với hiệu năng tầm trung cùng với mức giá hấp dẫn. Hiện sản phẩm đang được bán tại TNC Store.','Galax Geforce GTX 1650 Super',2),(5000,1000,11,6,'PC Gaming Supernova i5080 là bộ PC cao cấp sử dụng card màn hình RTX 5080 của nhà Colorful, đảm bảo về hiệu năng nhưng giá thành vẫn ở mức tầm trung phù hợp với game thủ và những người làm trong lĩnh vực đồ họa.','SuperNova i5080',8),(2000,1000,2,7,'Card Màn Hình Gigabyte GeForce GT 1030 OC 2G. Với thiết kế độc lạ cùng những hiệu năng không kém phần nổi bật, card màn hình Gigabyte GT 1030 OC 2G là lựa chọn tuyệt vời cho những ai đang tìm kiếm trải nghiệm gaming đỉnh cao.','Gigabyte GeForce GT 1030',2),(2000,1000,2,8,'Card Màn Hình Asus ROG Astral RTX 5090 32GB OC là sản phẩm cao cấp thuộc dòng VGA Nvidia RTX 5000 Series phù hợp cho các game thủ và người làm đồ họa chuyên nghiệp. Nổi bật với những công nghệ tiên tiến nhất, hiệu suất vượt trội và khả năng xử lý hình ảnh đáng kinh ngạc.','Asus ROG Astral GeForce RTX',2),(3200,1000,13,9,'Màn hình Gaming Asus Rog Strix XG27AQMR nằm trong dòng Asus Rog Strix, màn hình được thiết kế linh hoạt, thanh thoát. Màn hình được trang bị nhiều tính năng khiến đại đa số người dùng cảm thấy hài lòng trong từng khung hình.','ASUS ROG Strix XG27AQMR',3),(2800,1000,13,10,'LG cung cấp thêm tùy chọn màn hình chơi game cho người dùng ở dải sản phẩm Ultra Gear, lần này là mẫu 27GR750Q-B. Xét về thông số kỹ thuật, có vẻ như LG đã tiến hành cắt giảm vài chỗ của phiên bản Ultra Gear 27GP83B-B để tạo ra lựa chọn mới có giá rẻ hơn (dự đoán), nhưng vẫn là màn hình chơi game hiệu năng cao.','LG UltraGear 27GR75Q-B',4),(3200,1000,13,11,'Màn hình LG UltraGear 24 Inch IPS FHD 180Hz 24GS65F-B là lựa chọn hoàn hảo cho game thủ đam mê tốc độ và chất lượng hình ảnh tuyệt đỉnh. Với thiết kế hiện đại và công nghệ tiên tiến, chiếc màn hình này sẽ nâng tầm trải nghiệm chơi game của bạn lên một tầm cao mới.','LG 24GS65F-B',4),(3200,1000,12,12,'Razer DeathAdder V3 Pro Wireless được xem là một trong những dòng chuột gaming không dây đáng sở hữu nhất hiện nay. Sở hữu thời lượng pin \"Trâu\" lên đến 90 tiếng sử dụng liên tục. Đặc biệt, Razer DeathAdder V3 Pro Wireless White sử dụng switch quang học tránh được tình trạng double click.','Razer DeathAdder V3 Pro',6),(1600,1000,12,13,'Dòng bàn phím gaming của E-Dra được trang bị 3 loại switch là Blue / Red / Brown đa dạng hóa cho người dùng tùy chọn. Nó là bản nâng cấp từ thế hệ trước và điểm nhấn nằm ở hệ thống LED RGB 16.8 triệu màu. Đem đến độ nảy phím tốt dù sử dụng trong thời gian dài.','E-DRA EK387 V2 TKL',5),(6200,1000,12,14,'Tai nghe SteelSeries Arctis Pro + GameDAC hỗ trợ âm thanh 24 bit, cho chất lượng tuyệt vời, xứng tầm đẳng cấp. Sản phẩm hiện đang được bán tại TNC Store.','SteelSeries Arctis Pro',7),(109000,1000,11,15,'Nếu như bạn là fan của Intel, muốn trải nghiệm các tựa game 2K hoặc 4K, hãy tham khảo cấu hình PC Gaming - Sentinel I4090 - WH.','Sentinel I4090 - WH',8),(77000,1000,11,16,'Nếu bạn là một game thủ đam mê Liên Minh Huyền Thoại và muốn chơi mượt mà, bộ PC gaming Nova I4080 Super - BL sẽ là lựa chọn tuyệt vời.','Nova I4080 Super - BL',8);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
                           `rating` int NOT NULL,
                           `created_at` datetime(6) NOT NULL,
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `product_id` bigint NOT NULL,
                           `user_id` bigint NOT NULL,
                           `comment` varchar(1000) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKpl51cejpw4gy5swfar8br9ngi` (`product_id`),
                           KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
                           CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                           CONSTRAINT `FKpl51cejpw4gy5swfar8br9ngi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (5,'2025-03-21 10:00:00.000000',1,1,1,'Sản phẩm tuyệt vời! Hiệu năng mạnh mẽ, chạy mượt mà mọi tác vụ.'),(4,'2025-03-21 11:00:00.000000',2,2,2,'Card đồ họa rất tốt, chơi game mượt, nhưng giá hơi cao.'),(5,'2025-03-21 12:00:00.000000',3,6,1,'PC này quá đỉnh! Đồ họa đẹp, chơi game 4K không giật lag.'),(3,'2025-03-21 13:00:00.000000',4,12,2,'Chuột dùng ổn, nhưng cảm giác không thoải mái lắm khi cầm lâu.'),(4,'2025-03-21 14:00:00.000000',5,15,1,'Cấu hình mạnh, nhưng cần cải thiện hệ thống tản nhiệt.'),(1,'2025-03-26 01:43:18.029167',6,6,2,'                        nhu cut                            '),(5,'2025-03-26 10:42:22.217218',7,13,2,'                    ban phim oge                                '),(1,'2025-03-26 10:42:35.910124',8,13,2,'ban phim lach ka lach kach                                                    '),(1,'2025-03-26 10:42:54.880688',9,13,2,'ban phim rat ko ok                                                    '),(5,'2025-03-26 10:43:13.365648',10,14,2,'             tai nghe kha ba ngon                                       '),(3,'2025-03-26 11:46:46.733268',11,14,2,'     tai nghe vip pro                                               '),(1,'2025-03-26 15:13:00.559229',12,9,2,'         man nhu cc                                           '),(5,'2025-03-26 15:13:16.871003',13,9,2,'man kha ba ngon                                                    '),(1,'2025-03-26 19:39:48.436513',14,12,2,'chuot nhu cac                                                    ');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_authority`
--

DROP TABLE IF EXISTS `user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_authority` (
                                  `authority_id` bigint NOT NULL,
                                  `user_id` bigint NOT NULL,
                                  KEY `FKgvxjs381k6f48d5d2yi11uh89` (`authority_id`),
                                  KEY `FKhi46vu7680y1hwvmnnuh4cybx` (`user_id`),
                                  CONSTRAINT `FKgvxjs381k6f48d5d2yi11uh89` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`),
                                  CONSTRAINT `FKhi46vu7680y1hwvmnnuh4cybx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_authority`
--

LOCK TABLES `user_authority` WRITE;
/*!40000 ALTER TABLE `user_authority` DISABLE KEYS */;
INSERT INTO `user_authority` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `email_verified` bit(1) DEFAULT NULL,
                         `enabled` bit(1) DEFAULT NULL,
                         `expiration_time` datetime(6) DEFAULT NULL,
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `phone` varchar(10) DEFAULT NULL,
                         `address` varchar(255) DEFAULT NULL,
                         `avatar` varchar(255) DEFAULT NULL,
                         `email` varchar(255) NOT NULL,
                         `otp` varchar(255) DEFAULT NULL,
                         `password` varchar(255) NOT NULL,
                         `pending_email` varchar(255) DEFAULT NULL,
                         `username` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
                         UNIQUE KEY `UKr53o2ojjw4fikudfnsuuga336` (`password`),
                         UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
                         UNIQUE KEY `UK7rdjfmm6benwhhxon8tqdw445` (`pending_email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary '',_binary '','2025-03-24 00:54:01.000000',1,'0906118728','Dong Da',NULL,'fanphuong215@gmail.com','201893','$2a$10$qhiafOCSYSgXBX/PGDRUv.8.P1RbHL.HivbUrYS.uUzV4YqW8sgBy',NULL,'user'),(_binary '',_binary '','2025-03-24 00:55:10.000000',2,'0856200368','Ha Dong','/uploads/avatars/2_obito1.jpeg','phuongandphan@gmail.com','903456','$2a$10$NxRhAHOQ1RT/.RIS15FB5.joMiJcD/jqBAZO2hDiz.uaUgDr2uvoa',NULL,'admin'),(_binary '',_binary '','2025-03-26 19:41:56.630927',3,'0002020202','cai thang tinh le','/uploads/avatars/3_hinh-anh-naruto-ngau.jpg','nghiabinn2k3@gmail.com','444095','$2a$10$d3CVracSME2cYZaiXYwered77R4A2fzMOH1uQVxDSbzKdvs330sxG',NULL,'bintuantran'),(_binary '',_binary '\0','2025-03-27 13:20:54.512091',4,NULL,NULL,'https://engineering.usask.ca/images/no_avatar.jpg','phuongpvhe170793@fpt.edu.vn','899026','$2a$10$JBW037/LYaAnXykFcerWve2KynWOUP./cQ8JreNEcOP8qQnYC1G1a',NULL,'phan');
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

-- Dump completed on 2025-03-27 15:56:22
