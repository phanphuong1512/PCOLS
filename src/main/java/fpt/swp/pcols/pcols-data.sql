-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
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
-- Table structure for table `order_details`
--

--
-- Dumping data for table `order_details`
--

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `brand` varchar(255) DEFAULT NULL,
                            `description` varchar(2000) DEFAULT NULL,
                            `name` varchar(255) NOT NULL,
                            `price` double NOT NULL,
                            `stock` int DEFAULT NULL,
                            `category_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
                            CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Intel','CPU Intel Core Ultra 9 285K là bộ vi xử lý mạnh nhất thuộc dòng Intel Core Ultra thế hệ thứ 2, mang đến hiệu năng đỉnh cao cho các tác vụ đòi hỏi khắt khe nhất, từ chơi game AAA, sáng tạo nội dung đến xử lý dữ liệu phức tạp. Với 24 nhân, 24 luồng, xung nhịp tối đa lên đến 5.7GHz và bộ nhớ đệm 36MB, CPU Intel Core Ultra 9 285K sẽ là trái tim của hệ thống PC mạnh mẽ, đáp ứng mọi nhu cầu của bạn.','CPU Intel Core Ultra 9 285K',2000,1000,1),
                              (2,'Nvidia','MSI GeForce RTX 5070 12G VANGUARD SOC','MSI GeForce RTX 5070 12G VANGUARD SOC',2000,1000,2),
                              (3,'Intel','CPU Intel Core Ultra 7 265K thuộc dòng CPU Arrow Lake-S thế hệ thứ 15, Intel Core Ultra 7 265K mang đến sự kết hợp giữa sức mạnh vượt trội và hiệu quả cao với 20 nhân, 20 luồng và khả năng đạt xung nhịp lên đến 5.5 GHz. Đây là lựa chọn lý tưởng cho những người dùng cần một CPU có khả năng xử lý tốt cả trong các tác vụ gaming lẫn công việc đồ họa hay xử lý video.','CPU Intel Core Ultra 7 265K',2000,1000,1),
                              (4,'Intel','CPU Intel Core Ultra 5 245K là bộ vi xử lý mạnh mẽ thuộc dòng Intel Core Ultra thế hệ thứ 2, được thiết kế để mang đến hiệu năng vượt trội cho các tác vụ hàng ngày, từ làm việc văn phòng, học tập đến giải trí đa phương tiện. Với 14 nhân, 14 luồng, xung nhịp tối đa lên đến 5.2GHz và bộ nhớ đệm 24MB, CPU Intel Core Ultra 5 245K sẽ đáp ứng mọi nhu cầu của bạn một cách mượt mà và hiệu quả.','CPU Intel Core Ultra 5 245K',2000,1000,1),
                              (5,'Nvidia','Card Màn Hình Galax Geforce GTX 1650 Super ( 1-Click OC ) là dòng VGA quốc dân với hiệu năng tầm trung cùng với mức giá hấp dẫn. Hiện sản phẩm đang được bán tại TNC Store.','Galax Geforce GTX 1650 Super',2000,1000,2),
                              (6,'PC','PC Gaming Supernova i5080 là bộ PC cao cấp sử dụng card màn hình RTX 5080 của nhà Colorful, đảm bảo về hiệu năng nhưng giá thành vẫn ở mức tầm trung phù hợp với game thủ và những người làm trong lĩnh vực đồ họa.','SuperNova i5080',5000,1000,11),
                              (7,'Nvidia','Card Màn Hình Gigabyte GeForce GT 1030 OC 2G\nVới thiết kế độc lạ cùng những hiệu năng không kém phần nổi bật, card màn hình Gigabyte GT 1030 OC 2G là lựa chọn tuyệt vời cho những ai đang tìm kiếm trải nghiệm gaming đỉnh cao. ','Gigabyte GeForce GT 1030 OC 2G',2000,1000,2),
                              (8,'Nvidia','Card Màn Hình Asus ROG Astral RTX 5090 32GB OC là sản phẩm cao cấp thuộc dòng VGA Nvidia RTX 5000 Series phù hợp cho các game thủ và người làm đồ họa chuyên nghiệp. Nổi bật với những công nghệ tiên tiến nhất,  hiệu suất vượt trội và khả năng xử lý hình ảnh đáng kinh ngạc.','Asus ROG Astral GeForce RTX 5090 32GB GDDR7 OC Edition',2000,1000,2),
                              (9,'Asus','Màn hình Gaming Asus Rog Strix XG27AQMR nằm trong dòng Asus Rog Strix, màn hình được thiết kế linh hoạt, thanh thoát. Màn hình được trang bị nhiều tính năng khiến đại đa số người dùng cảm thấy hài lòng trong từng khung hình.','ASUS ROG Strix XG27AQMR 27 Inch/ 2K QHD/ Fast IPS/ 300Hz/ 1ms',3200,1000,13),
                              (10,'LG','LG cung cấp thêm tùy chọn màn hình chơi game cho người dùng ở dải sản phẩm Ultra Gear, lần này là mẫu 27GR750Q-B. Xét về thông số kỹ thuật, có vẻ như LG đã tiến hành cắt giảm vài chỗ của phiên bản Ultra Gear 27GP83B-B để tạo ra lựa chọn mới có giá rẻ hơn (dự đoán), nhưng vẫn là màn hình chơi game hiệu năng cao.','LG UltraGear 27GR75Q-B 27 Inch/ QHD/ IPS/ 165Hz / 1ms',2800,1000,13),
                              (11,'LG','Màn hình LG UltraGear 24 Inch IPS FHD 180Hz 24GS65F-B là lựa chọn hoàn hảo cho game thủ đam mê tốc độ và chất lượng hình ảnh tuyệt đỉnh. Với thiết kế hiện đại và công nghệ tiên tiến, chiếc màn hình này sẽ nâng tầm trải nghiệm chơi game của bạn lên một tầm cao mới.','LG 24GS65F-B 24 inch/ FHD/ IPS/ 180Hz/ 1ms',3200,1000,13),
                              (12,'Razer','Razer DeathAdder V3 Pro Wireless được xem là một trong những dòng chuột gaming không dây đáng sở hữu nhất hiện nay. Sở hữu thời lượng pin “Trâu” lên đến 90 tiếng sử dụng liên tục. Đặc biệt, Razer DeathAdder V3 Pro Wireless White sử dụng switch quang học tráng được tình trạng double click.','Razer DeathAdder V3 Pro Wireless - White',3200,1000,12),
                              (13,'E-DRA','Dòng bàn phím gaming của E-Dra được trang bị 3 loại switch là Blue / Red / Brown đa dạng hóa cho người dùng tùy chọn theo sở thích cá nhân của mình. Nó là bản nâng cấp từ thế hệ trước và sự đặc biệt nằm ở hệ thống LED RGB 16.8 triệu màu. Đem đến độ nảy phím cực đã dù có phải sử dụng trong thời gian dài bạn cũng sẽ không hề thấy mỏi.','E-DRA EK387 V2 TKL Brown Switch (E-Dra Switch, New 2022)',1600,1000,12),
                              (14,'SteelSeries','Tai nghe SteelSeries Arctis Pro + GameDAC là dòng tai nghe Gaming hỗ trợ chuẩn âm thanh Hi Res đầu tiên trên thế giới , hỗ trợ âm thanh 24 bit cho chất lượng âm thanh tuyệt vời, sứng tầm đẳng cấp dành cho người sử dụng. Sản phẩm hiện đang được bán tại TNC Store','SteelSeries Arctis Pro',6200,1000,12),
                              (15,'PC','Nếu như bạn là fan ruột của Intel, mong muốn trải nghiệm các tựa game game 2K, thậm chí có thể lên thêm 4K thì chắc chắn không thể bỏ qua cấu hình PC Gaming - Sentinel I4090 - WH.','Sentinel I4090 - WH',109000,1000,11),
                              (16,'PC','Nếu bạn là một game thủ đam mê các tựa game như Liên Minh Huyền Thoại, chắc hẳn bạn mong muốn có trải nghiệm chơi game tốt và mượt mà nhất. Bộ PC gaming Nova I4080 Super - BL sẽ là lựa chọn tuyệt vời cho bạn.','Nova I4080 Super - BL',77000,1000,11);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
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

CREATE TABLE `images` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `product_id` bigint NOT NULL,
                         `url` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FK_image_product` (`product_id`),
                         CONSTRAINT `FK_image_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO `images` (`product_id`, `url`) VALUES
                                                    (1, 'https://www.tncstore.vn/media/product/250-11383-cpu-core-ultra-9-285k--1-.jpg'),
                                                    (2, 'https://www.tncstore.vn/media/product/250-12004-tnc-store-card-man-hinh-msi-geforce-rtx-5070-12g-vanguard-soc--2-.jpg'),
                                                    (3, 'https://www.tncstore.vn/media/product/250-11384-cpu-core-ultra-7-265k--1-.jpg'),
                                                    (4, 'https://www.tncstore.vn/media/product/250-11386-cpu-core-ultra-5-245k--1-.jpg'),
                                                    (5, 'https://www.tncstore.vn/media/product/250-4625-card-man-hinh-galax-gtx-1650-super-ex-1-click-oc-01.jpg'),
                                                    (6, '/uploads/79a55e32-d66e-4f74-8481-207f3f5b90d8_pc1.jpg'),
                                                    (7, 'https://www.tncstore.vn/media/product/250-11335-tnc-store-card-card-man-hinh-gigabyte-geforce-gt-1030-oc-2g-gv-n1030oc-2gi--2-.jpg'),
                                                    (8, 'https://www.tncstore.vn/media/product/250-11617-card-man-hinh-asus-rog-astral-geforce-rtx-5090-32gb-gddr7-oc-edition--5-.jpg'),
                                                    (9, 'https://www.tncstore.vn/media/product/250-9247-man-asus-4.jpg'),
                                                    (10, 'https://www.tncstore.vn/media/product/250-9073-man-hinh-gaming-lg-uttra-gear-27-gr75q-b-5.jpg'),
                                                    (11, 'https://www.tncstore.vn/media/product/250-11465-man-hinh-gaming-lg-24gs65f-b--6-.jpg'),
                                                    (12, 'https://www.tncstore.vn/media/product/250-8340-razer-deathadder-v3-pro-ergonomic-white.jpg'),
                                                    (13, 'https://www.tncstore.vn/media/product/250-4854-ban-phim-co-e-dra-ek387-v2-brown-switch-1a.jpg'),
                                                    (14, 'https://www.tncstore.vn/media/product/250-2316-tai-nghe-steelseries-arctis-pro-gamedac-11.jpg'),
                                                    (15, 'https://www.tncstore.vn/media/product/250-9979-pc-gaming-sentinel-i4090-wh--16-.jpg'),
                                                    (16, 'https://www.tncstore.vn/media/product/250-9835-tnc-store-pc-gaming-nova-i4080-super-bl-15-.jpg');
--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `enabled` bit(1) DEFAULT NULL,
                         `role_id` int DEFAULT NULL,
                         `expiration_time` datetime(6) DEFAULT NULL,
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `phone` varchar(10) DEFAULT NULL,
                         `address` varchar(255) DEFAULT NULL,
                         `email` varchar(255) NOT NULL,
                         `otp` varchar(255) DEFAULT NULL,
                         `password` varchar(255) NOT NULL,
                         `username` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
                         UNIQUE KEY `UKr53o2ojjw4fikudfnsuuga336` (`password`),
                         UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--
INSERT INTO reviews (product_id, user_id, rating, comment, created_at) VALUES
                                                                           (1, 1, 5, 'Sản phẩm tuyệt vời! Hiệu năng mạnh mẽ, chạy mượt mà mọi tác vụ.', '2025-03-21 10:00:00'),
                                                                           (2, 2, 4, 'Card đồ họa rất tốt, chơi game mượt, nhưng giá hơi cao.', '2025-03-21 11:00:00'),
                                                                           (6, 1, 5, 'PC này quá đỉnh! Đồ họa đẹp, chơi game 4K không giật lag.', '2025-03-21 12:00:00'),
                                                                           (12, 2, 3, 'Chuột dùng ổn, nhưng cảm giác không thoải mái lắm khi cầm lâu.', '2025-03-21 13:00:00'),
                                                                           (15, 1, 4, 'Cấu hình mạnh, nhưng cần cải thiện hệ thống tản nhiệt.', '2025-03-21 14:00:00');
LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary '',0,'2025-03-12 16:42:41.436144',1,NULL,NULL,'phuongandphan@gmail.com','901212','$2a$10$qhiafOCSYSgXBX/PGDRUv.8.P1RbHL.HivbUrYS.uUzV4YqW8sgBy','user'),(_binary '',0,'2025-03-12 16:43:34.945308',2,NULL,NULL,'fanphuong215@gmail.com','261995','$2a$10$L.Wcd0tD01tBWwE5/TpYruqhv.UNHaTHZO/M6DG8C4CMUPJxT/dwe','admin');
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

-- Dump completed on 2025-03-17 17:50:22
