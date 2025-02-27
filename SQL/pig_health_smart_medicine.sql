/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : localhost:3306
 Source Schema         : pig_health_smart_medicine

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 27/02/2025 23:18:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article_types
-- ----------------------------
DROP TABLE IF EXISTS `article_types`;
CREATE TABLE `article_types`  (
  `type_id` int NOT NULL AUTO_INCREMENT COMMENT '文章类型ID',
  `type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章类型名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_types
-- ----------------------------

-- ----------------------------
-- Table structure for articles
-- ----------------------------
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `type_id` int NULL DEFAULT NULL COMMENT '文章类型ID，外键关联article_types表',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type_id`(`type_id` ASC) USING BTREE,
  CONSTRAINT `articles_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `article_types` (`type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of articles
-- ----------------------------

-- ----------------------------
-- Table structure for conversation
-- ----------------------------
DROP TABLE IF EXISTS `conversation`;
CREATE TABLE `conversation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `user_input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户输入',
  `ai_response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'AI回复',
  `conversation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '对话时间',
  `model_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'AI模型名称',
  `response_time` decimal(10, 2) NULL DEFAULT NULL COMMENT 'AI响应时间（秒）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '对话' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of conversation
-- ----------------------------

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈用户',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '反馈内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '反馈' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES (6, '路人甲', '31952874@qq.com', '测试一号', '测试这个系统有问题吗？', '2022-05-03 16:13:59', '2022-05-03 16:13:59');
INSERT INTO `feedback` VALUES (7, '路人乙', '31952874@qq.com', '测试二号', '惆怅长岑长错错错错错错', '2022-05-03 16:14:20', '2022-05-03 16:14:20');

-- ----------------------------
-- Table structure for files
-- ----------------------------
DROP TABLE IF EXISTS `files`;
CREATE TABLE `files`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件在 MinIO 中的路径',
  `file_size` bigint NOT NULL COMMENT '文件大小，单位为字节',
  `content_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件的 MIME 类型',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件的url',
  `upload_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '文件上传时间',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件存储的 MinIO 桶名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of files
-- ----------------------------
INSERT INTO `files` VALUES (1, 'd5d096a22b6340d182016beba5183f2f.docx', '/pig-health/2025/02/26/d5d096a22b6340d182016beba5183f2f.docx', 2995033, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'http://172.17.45.92:9000/pig-health/2025/02/26/d5d096a22b6340d182016beba5183f2f.docx', '2025-02-26 10:11:31', 'pig-health');
INSERT INTO `files` VALUES (2, '330c5255373841029496aa15d5c384b9.png', '/pig-health/2025/02/26/330c5255373841029496aa15d5c384b9.png', 159686, 'image/png', 'http://172.17.45.92:9000/pig-health/2025/02/26/330c5255373841029496aa15d5c384b9.png', '2025-02-26 10:12:50', 'pig-health');
INSERT INTO `files` VALUES (3, 'c1f5601d95014cf8aea1b12aff78a89d.png', '/pig-health/2025/02/26/c1f5601d95014cf8aea1b12aff78a89d.png', 159686, 'image/png', 'http://172.17.45.92:9000/pig-health/2025/02/26/c1f5601d95014cf8aea1b12aff78a89d.png', '2025-02-26 10:14:49', 'pig-health');
INSERT INTO `files` VALUES (4, '570e607aadfc41d0b2a49e7447c01e5d.png', '/pig-health/2025/02/26/570e607aadfc41d0b2a49e7447c01e5d.png', 159686, 'image/png', 'http://172.17.45.92:9000/pig-health/2025/02/26/570e607aadfc41d0b2a49e7447c01e5d.png', '2025-02-26 10:15:46', 'pig-health');
INSERT INTO `files` VALUES (5, 'f0017491d55e49679607c485d373a977.png', '/pig-health/2025/02/26/f0017491d55e49679607c485d373a977.png', 159686, 'image/png', 'http://172.17.45.92:9000/pig-health/2025/02/26/f0017491d55e49679607c485d373a977.png', '2025-02-26 15:41:11', 'pig-health');

-- ----------------------------
-- Table structure for history
-- ----------------------------
DROP TABLE IF EXISTS `history`;
CREATE TABLE `history`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户搜索历史主键id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '搜索关键字',
  `operate_type` int NULL DEFAULT NULL COMMENT '类型：1搜索，2科目，3药品',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of history
-- ----------------------------
INSERT INTO `history` VALUES (126, 4, '10,无', 1, '2022-05-03 16:09:34', '2022-05-03 16:09:34');
INSERT INTO `history` VALUES (127, 4, '10,无', 1, '2022-05-03 16:09:40', '2022-05-03 16:09:40');
INSERT INTO `history` VALUES (128, 4, '病毒性感冒', 2, '2022-05-03 16:09:48', '2022-05-03 16:09:48');
INSERT INTO `history` VALUES (129, 4, '10,无', 1, '2022-05-03 16:09:52', '2022-05-03 16:09:52');
INSERT INTO `history` VALUES (130, 4, '湿疹', 2, '2022-05-03 16:12:15', '2022-05-03 16:12:15');
INSERT INTO `history` VALUES (131, 4, '偏头痛', 2, '2022-05-03 16:12:49', '2022-05-03 16:12:49');
INSERT INTO `history` VALUES (132, 5, '7,无', 1, '2022-05-03 16:17:41', '2022-05-03 16:17:41');
INSERT INTO `history` VALUES (133, 5, '湿疹', 2, '2022-05-03 16:17:53', '2022-05-03 16:17:53');
INSERT INTO `history` VALUES (134, 5, '感冒', 2, '2022-05-03 16:18:08', '2022-05-03 16:18:08');
INSERT INTO `history` VALUES (135, 5, '17,无', 1, '2022-05-03 16:18:22', '2022-05-03 16:18:22');
INSERT INTO `history` VALUES (136, 5, '17,溃疡', 1, '2022-05-03 16:18:28', '2022-05-03 16:18:28');
INSERT INTO `history` VALUES (137, 5, '溃疡', 2, '2022-05-03 16:18:28', '2022-05-03 16:18:28');
INSERT INTO `history` VALUES (138, 5, '17,溃疡', 1, '2022-05-03 16:26:48', '2022-05-03 16:26:48');
INSERT INTO `history` VALUES (139, 5, '溃疡', 2, '2022-05-03 16:26:48', '2022-05-03 16:26:48');
INSERT INTO `history` VALUES (140, 5, '17,溃疡', 1, '2022-05-03 16:28:20', '2022-05-03 16:28:20');
INSERT INTO `history` VALUES (141, 5, '溃疡', 2, '2022-05-03 16:28:20', '2022-05-03 16:28:20');
INSERT INTO `history` VALUES (142, 5, '17,溃疡', 1, '2022-05-03 16:33:52', '2022-05-03 16:33:52');
INSERT INTO `history` VALUES (143, 5, '溃疡', 2, '2022-05-03 16:33:52', '2022-05-03 16:33:52');
INSERT INTO `history` VALUES (144, 5, '溃疡', 2, '2022-05-03 16:34:08', '2022-05-03 16:34:08');
INSERT INTO `history` VALUES (145, 5, '7,无', 1, '2022-05-03 16:37:57', '2022-05-03 16:37:57');
INSERT INTO `history` VALUES (146, 5, '9,无', 1, '2022-05-03 16:38:34', '2022-05-03 16:38:34');
INSERT INTO `history` VALUES (147, 5, '9,无', 1, '2022-05-03 16:41:59', '2022-05-03 16:41:59');
INSERT INTO `history` VALUES (148, 5, '9,无', 1, '2022-05-03 16:42:14', '2022-05-03 16:42:14');
INSERT INTO `history` VALUES (149, 5, '9,无', 1, '2022-05-03 16:42:45', '2022-05-03 16:42:45');
INSERT INTO `history` VALUES (150, 5, '9,无', 1, '2022-05-03 16:43:54', '2022-05-03 16:43:54');
INSERT INTO `history` VALUES (151, 5, '9,无', 1, '2022-05-03 16:44:26', '2022-05-03 16:44:26');
INSERT INTO `history` VALUES (152, 5, '9,无', 1, '2022-05-03 16:44:45', '2022-05-03 16:44:45');
INSERT INTO `history` VALUES (153, 5, '2,无', 1, '2022-05-03 16:44:51', '2022-05-03 16:44:51');
INSERT INTO `history` VALUES (154, 5, '2,无', 1, '2022-05-03 16:45:46', '2022-05-03 16:45:46');
INSERT INTO `history` VALUES (155, 5, '1', 3, '2022-05-07 15:34:34', '2022-05-07 15:34:34');
INSERT INTO `history` VALUES (156, 4, '牙周炎', 2, '2022-07-14 19:32:05', '2022-07-14 19:32:05');
INSERT INTO `history` VALUES (157, 4, '9,无', 1, '2022-07-14 19:32:52', '2022-07-14 19:32:52');
INSERT INTO `history` VALUES (158, 4, '1,无', 1, '2022-07-14 19:32:56', '2022-07-14 19:32:56');
INSERT INTO `history` VALUES (159, 4, '17,无', 1, '2022-07-14 19:32:59', '2022-07-14 19:32:59');

-- ----------------------------
-- Table structure for illness
-- ----------------------------
DROP TABLE IF EXISTS `illness`;
CREATE TABLE `illness`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '疾病id',
  `kind_id` int NULL DEFAULT NULL COMMENT '疾病分类ID',
  `illness_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '疾病名字',
  `include_reason` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '诱发因素',
  `illness_symptom` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '疾病症状',
  `special_symptom` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '特殊症状',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 121225 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '疾病' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of illness
-- ----------------------------
INSERT INTO `illness` VALUES (121213, 18, '猪肺炎1', '环境潮湿', '咳嗽、呼吸困难', '体温升高', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121214, 18, '猪肺炎2', '饲料霉变', '食欲减退、消瘦', '肺部啰音', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121215, 18, '猪肺炎3', '病毒感染', '精神萎靡、咳嗽', '肺部炎症', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121216, 19, '水肿病1', '饲料营养不足', '眼睑水肿、行动不便', '皮肤发亮', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121217, 19, '水肿病2', '细菌感染', '腹部膨胀、食欲不振', '皮肤紧张', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121218, 19, '水肿病3', '寄生虫侵袭', '四肢水肿、呼吸困难', '皮肤发紫', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121219, 20, '白痢病1', '环境卫生差', '白色粪便、脱水', '肛门红肿', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121220, 20, '白痢病2', '饲料变质', '腹泻、食欲减退', '腹部疼痛', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121221, 20, '白痢病3', '细菌感染', '体温升高、粪便恶臭', '脱水严重', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121222, 49, '嗜血杆菌病1', '环境拥挤', '咳嗽、流鼻涕', '关节肿胀', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121223, 49, '嗜血杆菌病2', '通风不良', '呼吸困难、发热', '肺部病变', '2025-02-25 23:45:49', '2025-02-25 23:45:49');
INSERT INTO `illness` VALUES (121224, 49, '嗜血杆菌病3', '免疫力低下', '食欲减退、消瘦', '全身症状', '2025-02-25 23:45:49', '2025-02-25 23:45:49');

-- ----------------------------
-- Table structure for illness_kind
-- ----------------------------
DROP TABLE IF EXISTS `illness_kind`;
CREATE TABLE `illness_kind`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类名称',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '疾病种类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of illness_kind
-- ----------------------------
INSERT INTO `illness_kind` VALUES (18, '猪肺炎', '一种影响猪呼吸系统的疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (19, '水肿病', '一种导致猪体水肿的疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (20, '白痢病', '仔猪常见的一种肠道疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (21, '黄痢病', '仔猪常见的一种急性肠道疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (22, '沙门氏菌病', '由沙门氏菌引起的一种传染病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (23, '淋巴肿胀', '淋巴系统异常肿胀的疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (24, '脑膜炎', '影响猪脑膜的炎症', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (25, '败血症', '血液感染导致的全身性疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (26, '鼻炎', '猪鼻黏膜的炎症', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (27, '猪瘟', '一种高度传染的病毒性疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (28, '丹毒', '由丹毒杆菌引起的皮肤病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (29, '猪狂犬病', '由狂犬病病毒引起的神经系统疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (30, '猪流感', '由猪流感病毒引起的呼吸道疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (31, '猪痘', '由猪痘病毒引起的皮肤病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (32, '传染性胸膜炎', '影响猪胸腔的传染性疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (33, '蓝耳病', '由猪繁殖与呼吸综合征病毒引起', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (34, '水泡病', '导致猪体出现水泡的病毒性疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (35, '细小病毒', '引起母猪繁殖障碍的病毒', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (36, '脑炎肠', '影响猪脑和肠道的疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (37, '胃炎', '猪胃黏膜的炎症', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (38, '仔猪副伤寒', '仔猪常见的肠道传染病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (39, '巴氏杆菌', '由巴氏杆菌引起的传染病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (40, '气喘病', '猪呼吸系统的慢性疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (41, '真菌病', '由真菌引起的皮肤病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (42, '子宫内膜炎', '母猪子宫黏膜的炎症', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (43, '弓形虫病', '由弓形虫引起的寄生虫病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (44, '腹泻', '猪常见的肠道疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (45, '猪痢疾', '由猪痢疾密螺旋体引起的肠道疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (46, '链球菌病', '由链球菌引起的传染病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (47, '猪大肠杆菌', '由大肠杆菌引起的肠道疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (48, '猪魏氏梭菌病', '由魏氏梭菌引起的肠道疾病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');
INSERT INTO `illness_kind` VALUES (49, '嗜血杆菌病', '由嗜血杆菌引起的传染病', '2025-02-25 23:42:16', '2025-02-25 23:42:16');

-- ----------------------------
-- Table structure for illness_medicine
-- ----------------------------
DROP TABLE IF EXISTS `illness_medicine`;
CREATE TABLE `illness_medicine`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '病和药品关联id',
  `illness_id` int NULL DEFAULT NULL COMMENT '病id',
  `medicine_id` int NULL DEFAULT NULL COMMENT '药品id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '疾病-药物' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of illness_medicine
-- ----------------------------
INSERT INTO `illness_medicine` VALUES (6, 3, 1, '2022-05-03 16:10:35', '2022-05-03 16:10:35');
INSERT INTO `illness_medicine` VALUES (7, 2, 1, '2022-05-03 16:10:37', '2022-05-03 16:10:37');
INSERT INTO `illness_medicine` VALUES (8, 1, 1, '2022-05-03 16:10:38', '2022-05-03 16:10:38');
INSERT INTO `illness_medicine` VALUES (9, 4, 1, '2022-05-03 16:10:42', '2022-05-03 16:10:42');
INSERT INTO `illness_medicine` VALUES (10, 7, 1, '2022-05-03 16:10:44', '2022-05-03 16:10:44');
INSERT INTO `illness_medicine` VALUES (11, 1, 2, '2022-05-03 16:10:59', '2022-05-03 16:10:59');
INSERT INTO `illness_medicine` VALUES (12, 2, 2, '2022-05-03 16:11:01', '2022-05-03 16:11:01');
INSERT INTO `illness_medicine` VALUES (13, 5, 3, '2022-05-03 16:11:16', '2022-05-03 16:11:16');
INSERT INTO `illness_medicine` VALUES (14, 13, 5, '2022-05-03 16:11:29', '2022-05-03 16:11:29');
INSERT INTO `illness_medicine` VALUES (15, 8, 4, '2022-05-03 16:11:39', '2022-05-03 16:11:39');
INSERT INTO `illness_medicine` VALUES (16, 7, 6, '2022-05-03 16:11:50', '2022-05-03 16:11:50');
INSERT INTO `illness_medicine` VALUES (17, 4, 7, '2022-05-03 16:12:01', '2022-05-03 16:12:01');
INSERT INTO `illness_medicine` VALUES (18, 2, 7, '2022-05-03 16:12:03', '2022-05-03 16:12:03');
INSERT INTO `illness_medicine` VALUES (19, 1, 7, '2022-05-03 16:12:04', '2022-05-03 16:12:04');
INSERT INTO `illness_medicine` VALUES (20, 3, 7, '2022-05-03 16:12:05', '2022-05-03 16:12:05');

-- ----------------------------
-- Table structure for medicine
-- ----------------------------
DROP TABLE IF EXISTS `medicine`;
CREATE TABLE `medicine`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '药品主键ID',
  `medicine_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '药的名字',
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关键字搜索',
  `medicine_effect` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '药的功效',
  `medicine_brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '药的品牌',
  `interaction` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '药的相互作用',
  `taboo` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '禁忌',
  `us_age` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用法用量',
  `medicine_type` int NULL DEFAULT NULL COMMENT '药的类型，0西药，1中药，2中成药',
  `img_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关图片路径',
  `medicine_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '药的价格',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '药品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medicine
-- ----------------------------
INSERT INTO `medicine` VALUES (8, '阿莫西林', '抗菌,消炎', '用于严重的猪肺炎、子宫炎，乳房炎及泌尿道感染', '未知', '与四环素等酸性药物及磺胺类药有配伍禁忌', '怀孕母猪慎用', '2~7毫克/千克体重，肌肉注射', 0, NULL, 10.50, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (9, '卡那霉素', '抗菌,呼吸道感染', '对呼吸道感染，特别对咳嗽、喘气较好', '未知', '毒性较大', '怀孕母猪慎用', '5~15毫克/千克体重', 0, NULL, 8.75, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (10, '阿维菌素', '驱虫,抗菌', '新型驱虫药，对线虫、绦虫、吸虫及皮肤寄生虫疥癣均有较好效果', '未知', '毒性较大，易造成母猪流产', '怀孕母猪禁用', '2~7毫克/千克体重，肌肉注射', 0, NULL, 12.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (11, '喹乙醇', '抗菌,促生长', '属于喹噁啉类，有抗菌、促生长作用', '未知', '抗菌药物', '无特殊禁忌', '50毫克/千克体重拌料', 0, NULL, 9.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (12, '安乃近', '解热镇痛', '起解热镇痛作用，临床上常用安乃近配合青霉素治疗一般性不吃料的猪病', '未知', '对怀孕母猪使用的剂量不能过大，否则会导致流产', '怀孕母猪慎用', '2~7毫克/千克体重，肌肉注射', 0, NULL, 7.50, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (13, '链霉素', '抗菌,抗革兰氏阴性菌', '抗革兰氏阴性菌，在临床上常与青霉素配合使用', '未知', '与青霉素配合使用', '无特殊禁忌', '10毫克/千克体重', 0, NULL, 6.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (14, '氨苄西林', '抗菌,消炎', '作为二线药，用于严重的肺炎，子宫炎，乳房炎及泌尿道感染', '未知', '抗菌药物', '无特殊禁忌', '2~7毫克/千克体重，肌肉注射', 0, NULL, 9.25, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (15, '氯前列腺素', '催情,催产', '有催情、催产、同期分娩等功效', '未知', '催情药物', '怀孕母猪禁用', '20毫克/千克体重', 0, NULL, 11.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (16, '大黄', '泻药', '泻药，用于治疗便秘', '未知', '泻药', '无特殊禁忌', '2~10毫克/千克体重，肌肉注射', 0, NULL, 5.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (17, '肾上腺素', '抗过敏,抗休克', '抗过敏、休克用，对疫苗过敏要立即肌肉注射进行解救', '未知', '抗过敏药物', '无特殊禁忌', '按1:5比例与磺胺类合用', 0, NULL, 8.50, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (18, '地塞米松', '抗炎,抗毒', '抗炎，抗毒，配合青霉素和安痛定使用，但会导致母猪流产和泌乳减少', '未知', '抗炎药物', '怀孕母猪慎用', '2~10毫克/千克体重，肌肉注射', 0, NULL, 10.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (19, '丁胺卡那', '抗菌,呼吸道感染', '对呼吸道感染，特别对咳嗽、喘气较好，也有一定的毒性', '未知', '抗菌药物', '怀孕母猪慎用', '2~10毫克/千克体重', 0, NULL, 7.75, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (20, '青霉素钠', '抗菌,消炎', '抗菌药物，用于治疗细菌感染', '未知', '抗菌药物', '无特殊禁忌', '5万～10万单位/千克体重，肌注', 0, NULL, 6.50, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (21, '氨苄西林', '抗菌,消炎', '抗菌药物，用于治疗细菌感染', '未知', '抗菌药物', '无特殊禁忌', '0.02%～0.05% 拌料；25～40毫克/千克体重，肌注', 0, NULL, 7.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (22, '红霉素', '抗菌,消炎', '抗菌药物，用于治疗细菌感染', '未知', '抗菌药物', '无特殊禁忌', '0.005%～0.02% 饮水；0.01～0.03% 拌料', 0, NULL, 9.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (23, '泰乐菌素', '抗菌,呼吸道感染', '抗菌药物，用于治疗猪喘气、咳嗽', '未知', '抗菌药物', '无特殊禁忌', '0.005%～0.01% 饮水；0.01%～0.02% 拌料', 0, NULL, 10.50, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (24, '四环素', '抗菌,消炎', '广谱抗菌素，对肠炎、腹泻效果好', '未知', '抗菌药物', '无特殊禁忌', '0.02%～0.05% 饮水；0.05%～0.1% 拌料', 0, NULL, 8.25, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (25, '强力霉素', '抗菌,消炎', '抗菌药物，用于治疗细菌感染', '未知', '抗菌药物', '无特殊禁忌', '0.01%～0.05% 饮水；0.02%～0.08% 拌料', 0, NULL, 9.50, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (26, '土霉素', '抗菌,消炎', '抗菌药物，用于治疗细菌感染', '未知', '抗菌药物', '无特殊禁忌', '0.02%～0.05% 饮水；0.1%～0.2% 拌料', 0, NULL, 7.75, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (27, '大观霉素', '抗菌,消炎', '抗菌药物，用于治疗细菌感染', '未知', '抗菌药物', '无特殊禁忌', '7.5～10毫克/千克体重，肌注', 0, NULL, 8.00, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (28, '庆大霉素', '抗菌,消炎', '抗菌药物，用于治疗细菌感染', '未知', '抗菌药物', '无特殊禁忌', '0.01%～0.02% 饮水；5～10 毫克/千克体重，肌注', 0, NULL, 7.50, '2025-02-25 23:48:51', '2025-02-25 23:48:51');
INSERT INTO `medicine` VALUES (29, '清开灵', '清热解毒, 凉血泻火', '具有清热解毒、凉血泻火、止咳利喉、镇静安神以及抗病毒、抗细菌、抗应激与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '肌肉注射', 1, NULL, 15.00, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (30, '大青解毒散', '清热解毒, 抗病毒', '具有清热解毒、凉血泻火、利喉消肿、润肺止咳以及抗病毒、抗细菌、抗过敏与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 12.50, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (31, '柴胡散', '清热解毒, 保肝利胆', '具有清热解毒、保肝利胆、镇咳祛痰、解痉镇痛以及抗病毒、抗细菌、抗过敏与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 14.00, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (32, '清肺散', '清热解毒, 润肺平喘', '具有清热解毒、润肺平喘、止咳化痰、消肿利喉以及抗病毒、抗细菌、抗过敏与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 13.50, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (33, '黄连解毒散', '清热解毒, 凉血泻火', '具有清热解毒、凉血泻火、镇痛镇静以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 12.75, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (34, '双黄连散', '清热解毒, 抗病毒', '具有清热解毒、辛凉解表、止咳利喉、消肿散结以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 11.00, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (35, '穿心莲散', '清热解毒, 凉血泻火', '具有清热解毒、凉血泻火、润肺止咳、利喉消肿以及抗病毒、抗细菌、抗应激与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 10.50, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (36, '鱼腥草散', '清热解毒, 消肿散痈', '具有清热解毒、消肿散痈、润肺化痰、利湿通淋以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 9.75, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (37, '银翘散', '清热解毒, 疏散风热', '具有清热解毒、疏散风热、发汗镇痛以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 8.00, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (38, '加味麻杏石甘散', '止咳平喘, 清热解毒', '具有清热解毒、润肺止咳、消肿散结以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 7.50, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (39, '清肺止咳散', '清热解毒, 润肺止咳', '具有清热解毒、润肺止咳、消肿散结以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 6.75, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (40, '清瘟败毒散', '清热解毒, 宣肺活血', '具有清热解毒、宣肺活血、消肿散结以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 5.00, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (41, '肝病消', '清热解毒, 消肿散结', '具有清热解毒、消肿散结、润肺止咳以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 4.50, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (42, '大承气散', '清热解毒, 消肿散结', '具有清热解毒、消肿散结、润肺止咳以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 3.75, '2025-02-26 00:05:07', '2025-02-26 00:05:07');
INSERT INTO `medicine` VALUES (43, '荆防败毒散', '清热解毒, 消肿散结', '具有清热解毒、消肿散结、润肺止咳以及抗病毒、抗细菌与提高机体免疫力的功能', '未知', '与抗生素类药物合用需谨慎', '怀孕母猪慎用', '口服', 1, NULL, 2.25, '2025-02-26 00:05:07', '2025-02-26 00:05:07');

-- ----------------------------
-- Table structure for news_articles
-- ----------------------------
DROP TABLE IF EXISTS `news_articles`;
CREATE TABLE `news_articles`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转载url',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新闻标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '新闻内容',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `publish_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间，默认为当前时间',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻来源',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '新闻摘要',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻资讯' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news_articles
-- ----------------------------

-- ----------------------------
-- Table structure for pageview
-- ----------------------------
DROP TABLE IF EXISTS `pageview`;
CREATE TABLE `pageview`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pageviews` int NULL DEFAULT NULL COMMENT '浏览量',
  `illness_id` int NULL DEFAULT NULL COMMENT '病的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '浏览量' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pageview
-- ----------------------------
INSERT INTO `pageview` VALUES (5, 5, 1);
INSERT INTO `pageview` VALUES (6, 4, 13);
INSERT INTO `pageview` VALUES (7, 2, 4);
INSERT INTO `pageview` VALUES (8, 1, 2);
INSERT INTO `pageview` VALUES (9, 1, 3);
INSERT INTO `pageview` VALUES (10, 1, 5);
INSERT INTO `pageview` VALUES (11, 1, 6);
INSERT INTO `pageview` VALUES (12, 2, 7);
INSERT INTO `pageview` VALUES (13, 1, 8);
INSERT INTO `pageview` VALUES (14, 1, 9);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户主键id',
  `user_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户账号',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户的真实名字',
  `user_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `user_age` int NULL DEFAULT NULL COMMENT '用户年龄',
  `user_sex` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户性别',
  `user_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `user_tel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `role_status` int NULL DEFAULT NULL COMMENT '角色状态，1管理员，0普通用户',
  `img_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'admin', '林总', '$2a$10$xEnUOD0KyHD6H6UGoagAOOpHHvc1OqvVrroLxjjriGibWNHau49xy', 18, '男', '13@qq.com', '', 1, 'https://tse4-mm.cn.bing.net/th/id/OIP-C.Xd88RmKtrH3ORAfPnL3gwAAAAA?w=168&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7', '2025-02-27 23:00:40', '2025-02-27 23:00:49');
INSERT INTO `user` VALUES (6, 'linyi', '林一', '$2a$10$aE8s0MmPVyE1x/QBc9kxXuF172CML5whPA23S2ACu0V.H/zOHYobC', 18, '男', '13112665250@qq.com', NULL, 0, 'https://tse4-mm.cn.bing.net/th/id/OIP-C.Xd88RmKtrH3ORAfPnL3gwAAAAA?w=168&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7', '2025-02-25 23:15:33', '2025-02-25 23:33:21');

SET FOREIGN_KEY_CHECKS = 1;
