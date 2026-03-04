

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` bigint NOT NULL COMMENT '文章唯一标识',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章分类：BREEDING-养殖知识，DISEASE-疾病防治，NUTRITION-营养饲料，MANAGEMENT-管理技术，NEWS-新闻资讯',
  `author_id` bigint NOT NULL COMMENT '作者用户ID',
  `author_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者姓名',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文章摘要',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `tags` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签（逗号分隔）',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览次数',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞次数',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT-草稿，PUBLISHED-已发布，ARCHIVED-已归档',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_author_id`(`author_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_publish_time`(`publish_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_article
-- ----------------------------

-- ----------------------------
-- Table structure for t_case
-- ----------------------------
DROP TABLE IF EXISTS `t_case`;
CREATE TABLE `t_case`  (
  `id` bigint NOT NULL COMMENT '病例唯一标识',
  `case_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '病例编号',
  `pig_id` bigint NOT NULL COMMENT '生猪标识',
  `user_id` bigint NOT NULL COMMENT '创建用户ID（养殖户或兽医）',
  `veterinarian_id` bigint NULL DEFAULT NULL COMMENT '诊断兽医ID',
  `symptoms` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '症状描述',
  `diagnosis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '诊断结果',
  `disease_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '疾病名称',
  `treatment_plan` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '治疗方案',
  `prescribed_drugs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '处方药品（JSON格式）',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'UNTREATED' COMMENT '病例状态：UNTREATED-未治疗，TREATING-治疗中，CURED-已治愈，DEAD-死亡',
  `diagnosis_time` datetime NULL DEFAULT NULL COMMENT '诊断时间',
  `treatment_start_time` datetime NULL DEFAULT NULL COMMENT '治疗开始时间',
  `treatment_end_time` datetime NULL DEFAULT NULL COMMENT '治疗结束时间',
  `follow_up_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '随访信息',
  `related_pigs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '关联生猪（JSON数组）',
  `related_cases` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '关联病例（JSON数组）',
  `ai_diagnosis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'AI诊断结果',
  `conversation_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联的对话ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_case_number`(`case_number` ASC, `deleted` ASC) USING BTREE,
  INDEX `idx_pig_id`(`pig_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_veterinarian_id`(`veterinarian_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_diagnosis_time`(`diagnosis_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '病例信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_case
-- ----------------------------

-- ----------------------------
-- Table structure for t_conversation
-- ----------------------------
DROP TABLE IF EXISTS `t_conversation`;
CREATE TABLE `t_conversation`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `conversation_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话名称',
  `last_time` datetime NULL DEFAULT NULL COMMENT '最近消息时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_conversation_user`(`conversation_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_user_time`(`user_id` ASC, `last_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会话列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_conversation
-- ----------------------------
INSERT INTO `t_conversation` VALUES (2028030278538108928, '2028030271927885824', '2001523723396308993', '新对话', '2026-03-01 16:51:12', '2026-03-01 16:51:12', '2026-03-01 16:51:12', 0);
INSERT INTO `t_conversation` VALUES (2028030290609315840, '2028030287060934656', '2001523723396308993', '新对话', '2026-03-01 16:51:54', '2026-03-01 16:51:15', '2026-03-01 16:51:15', 0);

-- ----------------------------
-- Table structure for t_conversation_summary
-- ----------------------------
DROP TABLE IF EXISTS `t_conversation_summary`;
CREATE TABLE `t_conversation_summary`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `conversation_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `last_message_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '摘要最后消息ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话摘要内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_conv_user`(`conversation_id` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会话摘要表（与消息表分离存储）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_conversation_summary
-- ----------------------------

-- ----------------------------
-- Table structure for t_disease
-- ----------------------------
DROP TABLE IF EXISTS `t_disease`;
CREATE TABLE `t_disease`  (
  `id` bigint NOT NULL COMMENT '疾病唯一标识',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '疾病名称',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '疾病分类：INFECTIOUS-传染病，PARASITIC-寄生虫病，METABOLIC-代谢病，OTHER-其他',
  `symptoms` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '典型症状',
  `causes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '病因',
  `treatment_methods` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '治疗方法',
  `prevention_measures` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '预防措施',
  `related_drugs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '相关药品（JSON数组）',
  `severity` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '严重程度：MILD-轻度，MODERATE-中度，SEVERE-重度',
  `contagious` tinyint NULL DEFAULT 0 COMMENT '是否传染：0-否，1-是',
  `incubation_period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '潜伏期',
  `mortality_rate` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '死亡率',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC, `deleted` ASC) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '疾病信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_disease
-- ----------------------------
INSERT INTO `t_disease` VALUES (1, '猪瘟', 'INFECTIOUS', '高热、食欲废绝、精神沉郁、皮肤发绀、便秘后腹泻', '猪瘟病毒感染', '无特效治疗，主要采取对症治疗和支持疗法', '定期免疫接种猪瘟疫苗，加强饲养管理，做好消毒工作', NULL, 'SEVERE', 1, NULL, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_disease` VALUES (2, '猪蓝耳病', 'INFECTIOUS', '发热、呼吸困难、耳朵发绀、流产、死胎', '猪繁殖与呼吸综合征病毒', '抗生素控制继发感染，对症治疗', '疫苗免疫，加强生物安全措施', NULL, 'SEVERE', 1, NULL, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_disease` VALUES (3, '猪圆环病毒病', 'INFECTIOUS', '消瘦、呼吸困难、腹泻、皮肤苍白', '猪圆环病毒2型感染', '对症治疗，控制继发感染', '疫苗免疫，改善饲养环境', NULL, 'MODERATE', 1, NULL, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_disease` VALUES (4, '猪流行性腹泻', 'INFECTIOUS', '水样腹泻、呕吐、脱水、仔猪死亡率高', '猪流行性腹泻病毒', '补液、止泻、抗菌防继发感染', '疫苗免疫，保持环境卫生', NULL, 'SEVERE', 1, NULL, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_disease` VALUES (5, '猪丹毒', 'INFECTIOUS', '急性败血症、皮肤疹块、关节炎', '猪丹毒杆菌感染', '青霉素类抗生素治疗效果好', '疫苗免疫，改善卫生条件', NULL, 'MODERATE', 1, NULL, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);

-- ----------------------------
-- Table structure for t_disease_drug
-- ----------------------------
DROP TABLE IF EXISTS `t_disease_drug`;
CREATE TABLE `t_disease_drug`  (
  `id` bigint NOT NULL COMMENT '关联唯一标识',
  `disease_id` bigint NOT NULL COMMENT '疾病ID',
  `drug_id` bigint NOT NULL COMMENT '药品ID',
  `effectiveness` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '有效性：HIGH-高效，MEDIUM-中效，LOW-低效',
  `recommended_dosage` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推荐剂量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_disease_drug`(`disease_id` ASC, `drug_id` ASC) USING BTREE,
  INDEX `idx_drug_id`(`drug_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '疾病-药品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_disease_drug
-- ----------------------------
INSERT INTO `t_disease_drug` VALUES (1, 1, 3, 'MEDIUM', '按体重0.2ml/kg，每日1次，连用5天', '2026-03-01 10:08:25');
INSERT INTO `t_disease_drug` VALUES (2, 1, 4, 'MEDIUM', '每100kg体重用本品100g，连用7天', '2026-03-01 10:08:25');
INSERT INTO `t_disease_drug` VALUES (3, 2, 1, 'HIGH', '按体重2mg/kg，每日1次，连用5天', '2026-03-01 10:08:25');
INSERT INTO `t_disease_drug` VALUES (4, 2, 3, 'MEDIUM', '按体重0.2ml/kg，每日1次，连用5天', '2026-03-01 10:08:25');
INSERT INTO `t_disease_drug` VALUES (5, 3, 2, 'HIGH', '每1L水加本品2g，连用5天', '2026-03-01 10:08:25');
INSERT INTO `t_disease_drug` VALUES (6, 4, 2, 'MEDIUM', '每1L水加本品1.5g，连用3天', '2026-03-01 10:08:25');
INSERT INTO `t_disease_drug` VALUES (7, 5, 1, 'HIGH', '按体重1.5mg/kg，每日1次，连用3天', '2026-03-01 10:08:25');
INSERT INTO `t_disease_drug` VALUES (8, 5, 5, 'HIGH', '按体重5mg/kg，每日1次，连用3天', '2026-03-01 10:08:25');

-- ----------------------------
-- Table structure for t_drug
-- ----------------------------
DROP TABLE IF EXISTS `t_drug`;
CREATE TABLE `t_drug`  (
  `id` bigint NOT NULL COMMENT '兽药唯一标识',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '兽药名称',
  `drug_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '药品类型：CHINESE-中药，WESTERN-西药',
  `manufacturer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `manufacturer_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产厂家地址',
  `approval_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '批准文号',
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格',
  `dosage_form` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '剂型：TABLET-片剂，INJECTION-注射剂，POWDER-粉剂，LIQUID-液体',
  `ingredients` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '成分',
  `indication` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '适应症',
  `usage` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用法',
  `dosage` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用量',
  `contraindication` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '禁忌症',
  `adverse_reaction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '不良反应',
  `drug_interaction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '药物相互作用',
  `storage` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '贮藏',
  `storage_conditions` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '贮存条件',
  `validity_period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '有效期',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `stock_quantity` int NULL DEFAULT 0 COMMENT '库存数量',
  `sales_volume` int NULL DEFAULT 0 COMMENT '销售量',
  `rating` decimal(3, 2) NULL DEFAULT 5.00 COMMENT '评分（1-5）',
  `review_count` int NULL DEFAULT 0 COMMENT '评价数量',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '药品图片URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE,
  INDEX `idx_drug_type`(`drug_type` ASC) USING BTREE,
  INDEX `idx_manufacturer`(`manufacturer` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '兽药信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_drug
-- ----------------------------
INSERT INTO `t_drug` VALUES (1, '头孢噻呋注射液', 'WESTERN', '某某动物药业有限公司', NULL, NULL, '1g/瓶', 'INJECTION', NULL, '用于治疗猪呼吸道感染、泌尿道感染等', '肌肉注射', '按体重1-2mg/kg，每日1次，连用3-5天', NULL, NULL, NULL, NULL, NULL, NULL, 25.00, 0, 0, 4.80, 0, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_drug` VALUES (2, '阿莫西林可溶性粉', 'WESTERN', '某某兽药厂', NULL, NULL, '100g/袋', 'POWDER', NULL, '用于治疗猪细菌性感染', '混饮或拌料', '每1L水加本品1-2g，连用3-5天', NULL, NULL, NULL, NULL, NULL, NULL, 15.00, 0, 0, 4.50, 0, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_drug` VALUES (3, '黄芪多糖注射液', 'CHINESE', '某某中兽药公司', NULL, NULL, '10ml/支', 'INJECTION', NULL, '增强免疫力，辅助治疗病毒性疾病', '肌肉注射', '按体重0.1-0.2ml/kg，每日1次', NULL, NULL, NULL, NULL, NULL, NULL, 8.00, 0, 0, 4.60, 0, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_drug` VALUES (4, '板蓝根颗粒', 'CHINESE', '某某中药制药厂', NULL, NULL, '500g/袋', 'POWDER', NULL, '清热解毒，用于病毒性疾病的辅助治疗', '混饮', '每100kg体重用本品50-100g，连用5-7天', NULL, NULL, NULL, NULL, NULL, NULL, 30.00, 0, 0, 4.70, 0, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);
INSERT INTO `t_drug` VALUES (5, '恩诺沙星注射液', 'WESTERN', '某某动保公司', NULL, NULL, '100ml/瓶', 'INJECTION', NULL, '用于治疗猪细菌性疾病', '肌肉注射', '按体重2.5-5mg/kg，每日1次，连用3-5天', NULL, NULL, NULL, NULL, NULL, NULL, 18.00, 0, 0, 4.75, 0, NULL, '2026-03-01 10:08:25', '2026-03-01 10:08:25', 0);

-- ----------------------------
-- Table structure for t_farm
-- ----------------------------
DROP TABLE IF EXISTS `t_farm`;
CREATE TABLE `t_farm`  (
  `id` bigint NOT NULL COMMENT '养殖场唯一标识',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '养殖场名称',
  `owner_id` bigint NOT NULL COMMENT '场主用户ID',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `scale` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '养殖规模：SMALL-小型，MEDIUM-中型，LARGE-大型',
  `total_pigs` int NULL DEFAULT 0 COMMENT '生猪总数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_owner_id`(`owner_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '养殖场信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_farm
-- ----------------------------
INSERT INTO `t_farm` VALUES (2028103383134519296, '林的', 2001523723396308993, '广州市', '13112665251', 'MEDIUM', 0, '2026-03-01 21:41:42', '2026-03-01 21:41:42', 0);

-- ----------------------------
-- Table structure for t_health_monitor
-- ----------------------------
DROP TABLE IF EXISTS `t_health_monitor`;
CREATE TABLE `t_health_monitor`  (
  `id` bigint NOT NULL COMMENT '监测记录唯一标识',
  `pig_id` bigint NOT NULL COMMENT '生猪ID',
  `monitor_date` datetime NOT NULL COMMENT '监测日期',
  `temperature` decimal(4, 2) NULL DEFAULT NULL COMMENT '体温（℃）',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '体重（kg）',
  `appetite` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '食欲：GOOD-良好，NORMAL-正常，POOR-差',
  `activity_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动水平：ACTIVE-活跃，NORMAL-正常，LETHARGIC-嗜睡',
  `feces_condition` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '粪便状况',
  `respiratory_rate` int NULL DEFAULT NULL COMMENT '呼吸频率（次/分钟）',
  `heart_rate` int NULL DEFAULT NULL COMMENT '心率（次/分钟）',
  `abnormal_signs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常体征',
  `monitor_by` bigint NULL DEFAULT NULL COMMENT '监测人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pig_id`(`pig_id` ASC) USING BTREE,
  INDEX `idx_monitor_date`(`monitor_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '生猪健康监测表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_health_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for t_ingestion_pipeline
-- ----------------------------
DROP TABLE IF EXISTS `t_ingestion_pipeline`;
CREATE TABLE `t_ingestion_pipeline`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '流水线描述',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_ingestion_pipeline_name`(`name` ASC, `deleted` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '摄取流水线定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ingestion_pipeline
-- ----------------------------

-- ----------------------------
-- Table structure for t_ingestion_pipeline_node
-- ----------------------------
DROP TABLE IF EXISTS `t_ingestion_pipeline_node`;
CREATE TABLE `t_ingestion_pipeline_node`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `pipeline_id` bigint NOT NULL COMMENT '流水线ID',
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点标识(同一流水线内唯一)',
  `node_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点类型',
  `next_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '下一个节点ID',
  `settings_json` json NULL COMMENT '节点配置JSON',
  `condition_json` json NULL COMMENT '条件JSON',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_ingestion_pipeline_node`(`pipeline_id` ASC, `node_id` ASC, `deleted` ASC) USING BTREE,
  INDEX `idx_ingestion_pipeline_node_pipeline`(`pipeline_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '摄取流水线节点配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ingestion_pipeline_node
-- ----------------------------

-- ----------------------------
-- Table structure for t_ingestion_task
-- ----------------------------
DROP TABLE IF EXISTS `t_ingestion_task`;
CREATE TABLE `t_ingestion_task`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `pipeline_id` bigint NOT NULL COMMENT '流水线ID',
  `source_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '来源类型',
  `source_location` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '来源地址或URL',
  `source_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原始文件名',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务状态',
  `chunk_count` int NULL DEFAULT 0 COMMENT '分块数量',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '错误信息',
  `logs_json` json NULL COMMENT '节点日志JSON',
  `metadata_json` json NULL COMMENT '扩展元数据JSON',
  `started_at` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `completed_at` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_ingestion_task_pipeline`(`pipeline_id` ASC) USING BTREE,
  INDEX `idx_ingestion_task_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '摄取任务记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ingestion_task
-- ----------------------------

-- ----------------------------
-- Table structure for t_ingestion_task_node
-- ----------------------------
DROP TABLE IF EXISTS `t_ingestion_task_node`;
CREATE TABLE `t_ingestion_task_node`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `pipeline_id` bigint NOT NULL COMMENT '流水线ID',
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点标识',
  `node_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点类型',
  `node_order` int NOT NULL DEFAULT 0 COMMENT '节点顺序',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点状态',
  `duration_ms` bigint NOT NULL DEFAULT 0 COMMENT '执行耗时(毫秒)',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '节点消息',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '错误信息',
  `output_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '节点输出JSON(全量)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_ingestion_task_node_task`(`task_id` ASC) USING BTREE,
  INDEX `idx_ingestion_task_node_pipeline`(`pipeline_id` ASC) USING BTREE,
  INDEX `idx_ingestion_task_node_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '摄取任务节点执行记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ingestion_task_node
-- ----------------------------

-- ----------------------------
-- Table structure for t_intent_node
-- ----------------------------
DROP TABLE IF EXISTS `t_intent_node`;
CREATE TABLE `t_intent_node`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `kb_id` bigint NULL DEFAULT NULL COMMENT '知识库ID',
  `intent_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务唯一标识',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '展示名称',
  `level` tinyint NOT NULL COMMENT '层级 0：DOMAIN 1：CATEGORY 2：TOPIC',
  `parent_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父节点标识',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '语义描述',
  `examples` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '示例问题',
  `collection_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联的Collection名称',
  `top_k` int NULL DEFAULT NULL COMMENT '知识库检索TopK',
  `mcp_tool_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MCP工具ID',
  `kind` tinyint(1) NOT NULL DEFAULT 0 COMMENT '类型 0：RAG知识库类 1：SYSTEM系统交互类',
  `prompt_snippet` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '提示词片段',
  `prompt_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '提示词模板',
  `param_prompt_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '参数提取提示词模板（MCP模式专属）',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序字段',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用 1：启用 0：禁用',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2018166720850104321 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'RAG意图树节点配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_intent_node
-- ----------------------------

-- ----------------------------
-- Table structure for t_knowledge_base
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_base`;
CREATE TABLE `t_knowledge_base`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '知识库名称',
  `embedding_model` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '嵌入模型标识',
  `collection_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Milvus Collection',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_collection_name`(`collection_name` ASC) USING BTREE COMMENT 'Collection 唯一约束',
  INDEX `idx_kb_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2018586042835763201 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAG知识库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_knowledge_base
-- ----------------------------

-- ----------------------------
-- Table structure for t_knowledge_chunk
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_chunk`;
CREATE TABLE `t_knowledge_chunk`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `kb_id` bigint NOT NULL COMMENT '知识库ID',
  `doc_id` bigint NOT NULL COMMENT '文档ID',
  `chunk_index` int NOT NULL COMMENT '分块序号（从0开始）',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分块正文内容',
  `content_hash` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容哈希（用于幂等/去重）',
  `char_count` int NULL DEFAULT NULL COMMENT '字符数（可用于统计/调参）',
  `token_count` int NULL DEFAULT NULL COMMENT 'Token数（可选）',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用 0：禁用 1：启用',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_doc_id`(`doc_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2019941971888291843 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAG知识库文档分块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_knowledge_chunk
-- ----------------------------

-- ----------------------------
-- Table structure for t_knowledge_document
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_document`;
CREATE TABLE `t_knowledge_document`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `kb_id` bigint NOT NULL COMMENT '知识库ID',
  `doc_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文档名称',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用 1：启用 0：禁用',
  `chunk_count` int NULL DEFAULT 0 COMMENT '分块数（chunk 数量）',
  `file_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件地址',
  `file_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件类型',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小（单位字节）',
  `process_mode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'chunk' COMMENT '处理模式',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '状态',
  `source_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源类型：file/url',
  `source_location` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源位置（URL）',
  `schedule_enabled` tinyint(1) NULL DEFAULT NULL COMMENT '定时拉取 0：否 1：是',
  `schedule_cron` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '定时拉取cron表达式',
  `chunk_strategy` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分块策略',
  `chunk_config` json NULL COMMENT '分块参数JSON',
  `pipeline_id` bigint NULL DEFAULT NULL COMMENT '数据通道ID',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_kb_id`(`kb_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2019941957409554433 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'RAG知识库文档表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_knowledge_document
-- ----------------------------

-- ----------------------------
-- Table structure for t_knowledge_document_chunk_log
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_document_chunk_log`;
CREATE TABLE `t_knowledge_document_chunk_log`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `doc_id` bigint NOT NULL COMMENT '文档ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行状态',
  `process_mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理模式',
  `chunk_strategy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分块策略（仅chunk模式）',
  `pipeline_id` bigint NULL DEFAULT NULL COMMENT 'Pipeline ID（仅pipeline模式）',
  `extract_duration` bigint NULL DEFAULT NULL COMMENT '文本提取耗时（毫秒）',
  `chunk_duration` bigint NULL DEFAULT NULL COMMENT '分块耗时（毫秒）',
  `embedding_duration` bigint NULL DEFAULT NULL COMMENT '向量化耗时（毫秒）',
  `total_duration` bigint NULL DEFAULT NULL COMMENT '总耗时（毫秒）',
  `chunk_count` int NULL DEFAULT NULL COMMENT '生成的分块数量',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '错误信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_doc_id`(`doc_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '知识库文档分块日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_knowledge_document_chunk_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_knowledge_document_schedule
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_document_schedule`;
CREATE TABLE `t_knowledge_document_schedule`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `doc_id` bigint NOT NULL COMMENT '文档ID',
  `kb_id` bigint NOT NULL COMMENT '知识库ID',
  `cron_expr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '定时表达式',
  `enabled` tinyint NULL DEFAULT 0 COMMENT '是否启用定时',
  `next_run_time` datetime NULL DEFAULT NULL COMMENT '下一次执行时间',
  `last_run_time` datetime NULL DEFAULT NULL COMMENT '上一次执行时间',
  `last_success_time` datetime NULL DEFAULT NULL COMMENT '上一次成功时间',
  `last_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上一次执行状态',
  `last_error` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上一次执行错误',
  `last_etag` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上一次ETag',
  `last_modified` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上一次Last-Modified',
  `last_content_hash` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上一次内容哈希',
  `lock_owner` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '锁持有者',
  `lock_until` datetime NULL DEFAULT NULL COMMENT '锁到期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_doc_id`(`doc_id` ASC) USING BTREE,
  INDEX `idx_next_run`(`next_run_time` ASC) USING BTREE,
  INDEX `idx_lock_until`(`lock_until` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '知识库文档定时刷新任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_knowledge_document_schedule
-- ----------------------------

-- ----------------------------
-- Table structure for t_knowledge_document_schedule_exec
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_document_schedule_exec`;
CREATE TABLE `t_knowledge_document_schedule_exec`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `schedule_id` bigint NOT NULL COMMENT '定时任务ID',
  `doc_id` bigint NOT NULL COMMENT '文档ID',
  `kb_id` bigint NOT NULL COMMENT '知识库ID',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行状态',
  `message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `file_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
  `content_hash` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容哈希',
  `etag` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'ETag',
  `last_modified` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Last-Modified',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_schedule_time`(`schedule_id` ASC, `start_time` ASC) USING BTREE,
  INDEX `idx_doc_id`(`doc_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '知识库文档定时刷新执行记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_knowledge_document_schedule_exec
-- ----------------------------

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `conversation_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `role` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色：system/user/assistant',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_conversation_user_time`(`conversation_id` ASC, `user_id` ASC, `create_time` ASC) USING BTREE,
  INDEX `idx_conversation_summary`(`conversation_id` ASC, `user_id` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会话消息记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES (2028030277200125952, '2028030271927885824', '2001523723396308993', 'user', '你是什么模型', '2026-03-01 16:51:12', '2026-03-01 16:51:12', 0);
INSERT INTO `t_message` VALUES (2028030289397161984, '2028030287060934656', '2001523723396308993', 'user', '你是什么模型', '2026-03-01 16:51:15', '2026-03-01 16:51:15', 0);
INSERT INTO `t_message` VALUES (2028030341029044224, '2028030287060934656', '2001523723396308993', 'user', '你是什么模型', '2026-03-01 16:51:27', '2026-03-01 16:51:27', 0);
INSERT INTO `t_message` VALUES (2028030351103762432, '2028030287060934656', '2001523723396308993', 'user', '你是什么模型', '2026-03-01 16:51:29', '2026-03-01 16:51:29', 0);
INSERT INTO `t_message` VALUES (2028030445806952448, '2028030287060934656', '2001523723396308993', 'user', '你是什么模型', '2026-03-01 16:51:52', '2026-03-01 16:51:52', 0);
INSERT INTO `t_message` VALUES (2028030456116551680, '2028030287060934656', '2001523723396308993', 'user', '你是什么模型', '2026-03-01 16:51:54', '2026-03-01 16:51:54', 0);

-- ----------------------------
-- Table structure for t_message_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_message_feedback`;
CREATE TABLE `t_message_feedback`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `message_id` bigint NOT NULL COMMENT '关联的消息ID',
  `conversation_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `vote` tinyint(1) NOT NULL COMMENT '反馈值 1：点赞 -1：点踩',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈原因',
  `comment` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '补充说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_msg_user`(`message_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_conversation_id`(`conversation_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会话消息反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message_feedback
-- ----------------------------

-- ----------------------------
-- Table structure for t_pig
-- ----------------------------
DROP TABLE IF EXISTS `t_pig`;
CREATE TABLE `t_pig`  (
  `id` bigint NOT NULL COMMENT '生猪唯一标识',
  `farm_id` bigint NOT NULL COMMENT '养殖场标识',
  `user_id` bigint NOT NULL COMMENT '养殖户用户ID',
  `ear_tag_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '耳标号',
  `breed` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品种',
  `gender` tinyint NOT NULL COMMENT '性别：0-母猪，1-公猪',
  `birth_date` date NOT NULL COMMENT '出生日期',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '体重（kg）',
  `health_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'HEALTHY' COMMENT '健康状态：HEALTHY-健康，SICK-患病，RECOVERING-康复中，DEAD-死亡',
  `pen_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '猪栏编号',
  `feed_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'NORMAL' COMMENT '饲养状态：NORMAL-正常，WEANING-断奶，FATTENING-育肥',
  `immunization_status` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '免疫状态（JSON格式）',
  `genetic_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '遗传信息',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_ear_tag`(`ear_tag_number` ASC, `deleted` ASC) USING BTREE,
  INDEX `idx_farm_id`(`farm_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_health_status`(`health_status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '生猪信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pig
-- ----------------------------

-- ----------------------------
-- Table structure for t_query_term_mapping
-- ----------------------------
DROP TABLE IF EXISTS `t_query_term_mapping`;
CREATE TABLE `t_query_term_mapping`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `domain` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务域/系统标识，如biz、group、data_security等，可选',
  `source_term` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户常说的原始词',
  `target_term` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '归一化后的目标词',
  `match_type` tinyint NOT NULL DEFAULT 1 COMMENT '匹配类型 1：精确匹配 2：前缀匹配 3：正则匹配 4：整词匹配',
  `priority` int NOT NULL DEFAULT 100 COMMENT '优先级，数值越小优先级越高（先匹配长词）',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否生效 1：生效 0：禁用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_domain`(`domain` ASC) USING BTREE,
  INDEX `idx_source`(`source_term` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'RAG关键词归一化映射表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_query_term_mapping
-- ----------------------------

-- ----------------------------
-- Table structure for t_rag_trace_node
-- ----------------------------
DROP TABLE IF EXISTS `t_rag_trace_node`;
CREATE TABLE `t_rag_trace_node`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `trace_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属链路ID',
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点ID',
  `parent_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父节点ID',
  `depth` int NULL DEFAULT 0 COMMENT '节点深度',
  `node_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点类型',
  `node_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点名称',
  `class_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名',
  `method_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'RUNNING' COMMENT 'RUNNING/SUCCESS/ERROR',
  `error_message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `start_time` datetime(3) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(3) NULL DEFAULT NULL COMMENT '结束时间',
  `duration_ms` bigint NULL DEFAULT NULL COMMENT '耗时毫秒',
  `extra_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '扩展字段(JSON)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_run_node`(`trace_id` ASC, `node_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'RAG Trace 节点记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_rag_trace_node
-- ----------------------------
INSERT INTO `t_rag_trace_node` VALUES (2028030277741191169, '2028030275308494848', '2028030277741191168', NULL, 0, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:11.851', '2026-03-01 16:51:11.933', 82, NULL, '2026-03-01 16:51:12', '2026-03-01 16:51:12', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030278886236161, '2028030275308494848', '2028030278886236160', NULL, 0, 'REWRITE', 'query-rewrite-and-split', 'com.nageoffer.ai.ragent.rag.core.rewrite.MultiQuestionRewriteService', 'rewriteWithSplit', 'SUCCESS', NULL, '2026-03-01 16:51:12.124', '2026-03-01 16:51:12.363', 239, NULL, '2026-03-01 16:51:12', '2026-03-01 16:51:12', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030279246946304, '2028030275308494848', '2028030279238557696', '2028030278886236160', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:12.209', '2026-03-01 16:51:12.280', 71, NULL, '2026-03-01 16:51:12', '2026-03-01 16:51:12', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030280240996352, '2028030275308494848', '2028030280199053312', NULL, 0, 'INTENT', 'intent-resolve', 'com.nageoffer.ai.ragent.rag.core.intent.IntentResolver', 'resolve', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:12.437', '2026-03-01 16:51:12.762', 325, NULL, '2026-03-01 16:51:12', '2026-03-01 16:51:13', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030280916279297, '2028030275308494848', '2028030280916279296', '2028030280199053312', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:12.608', '2026-03-01 16:51:12.684', 76, NULL, '2026-03-01 16:51:13', '2026-03-01 16:51:13', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030289959198720, '2028030288415694848', '2028030289950810112', NULL, 0, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:14.762', '2026-03-01 16:51:14.835', 73, NULL, '2026-03-01 16:51:15', '2026-03-01 16:51:15', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030290936471552, '2028030288415694848', '2028030290932277248', NULL, 0, 'REWRITE', 'query-rewrite-and-split', 'com.nageoffer.ai.ragent.rag.core.rewrite.MultiQuestionRewriteService', 'rewriteWithSplit', 'SUCCESS', NULL, '2026-03-01 16:51:14.996', '2026-03-01 16:51:15.237', 241, NULL, '2026-03-01 16:51:15', '2026-03-01 16:51:15', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030291259432961, '2028030288415694848', '2028030291259432960', '2028030290932277248', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:15.074', '2026-03-01 16:51:15.155', 81, NULL, '2026-03-01 16:51:15', '2026-03-01 16:51:15', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030292257677313, '2028030288415694848', '2028030292257677312', NULL, 0, 'INTENT', 'intent-resolve', 'com.nageoffer.ai.ragent.rag.core.intent.IntentResolver', 'resolve', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:15.312', '2026-03-01 16:51:15.625', 313, NULL, '2026-03-01 16:51:15', '2026-03-01 16:51:16', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030292953931776, '2028030288415694848', '2028030292945543168', '2028030292257677312', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:15.476', '2026-03-01 16:51:15.552', 76, NULL, '2026-03-01 16:51:15', '2026-03-01 16:51:16', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030341842739201, '2028030340148240384', '2028030341842739200', NULL, 0, 'REWRITE', 'query-rewrite-and-split', 'com.nageoffer.ai.ragent.rag.core.rewrite.MultiQuestionRewriteService', 'rewriteWithSplit', 'SUCCESS', NULL, '2026-03-01 16:51:27.134', '2026-03-01 16:51:27.378', 244, NULL, '2026-03-01 16:51:27', '2026-03-01 16:51:27', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030342186672129, '2028030340148240384', '2028030342186672128', '2028030341842739200', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:27.216', '2026-03-01 16:51:27.296', 80, NULL, '2026-03-01 16:51:27', '2026-03-01 16:51:27', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030343176527873, '2028030340148240384', '2028030343176527872', NULL, 0, 'INTENT', 'intent-resolve', 'com.nageoffer.ai.ragent.rag.core.intent.IntentResolver', 'resolve', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:27.452', '2026-03-01 16:51:27.775', 323, NULL, '2026-03-01 16:51:27', '2026-03-01 16:51:28', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030343868588033, '2028030340148240384', '2028030343868588032', '2028030343176527872', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:27.617', '2026-03-01 16:51:27.698', 81, NULL, '2026-03-01 16:51:28', '2026-03-01 16:51:28', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030351879708672, '2028030350197792768', '2028030351875514368', NULL, 0, 'REWRITE', 'query-rewrite-and-split', 'com.nageoffer.ai.ragent.rag.core.rewrite.MultiQuestionRewriteService', 'rewriteWithSplit', 'SUCCESS', NULL, '2026-03-01 16:51:29.526', '2026-03-01 16:51:29.776', 250, NULL, '2026-03-01 16:51:30', '2026-03-01 16:51:30', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030352211058688, '2028030350197792768', '2028030352206864384', '2028030351875514368', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:29.605', '2026-03-01 16:51:29.687', 82, NULL, '2026-03-01 16:51:30', '2026-03-01 16:51:30', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030353247051777, '2028030350197792768', '2028030353247051776', NULL, 0, 'INTENT', 'intent-resolve', 'com.nageoffer.ai.ragent.rag.core.intent.IntentResolver', 'resolve', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:29.853', '2026-03-01 16:51:30.165', 312, NULL, '2026-03-01 16:51:30', '2026-03-01 16:51:30', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030353867808769, '2028030350197792768', '2028030353867808768', '2028030353247051776', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:30.001', '2026-03-01 16:51:30.084', 83, NULL, '2026-03-01 16:51:30', '2026-03-01 16:51:30', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030446591287296, '2028030444594798592', '2028030446587092992', NULL, 0, 'REWRITE', 'query-rewrite-and-split', 'com.nageoffer.ai.ragent.rag.core.rewrite.MultiQuestionRewriteService', 'rewriteWithSplit', 'SUCCESS', NULL, '2026-03-01 16:51:52.107', '2026-03-01 16:51:52.345', 238, NULL, '2026-03-01 16:51:52', '2026-03-01 16:51:52', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030446893277185, '2028030444594798592', '2028030446893277184', '2028030446587092992', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:52.180', '2026-03-01 16:51:52.267', 87, NULL, '2026-03-01 16:51:52', '2026-03-01 16:51:52', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030447929270273, '2028030444594798592', '2028030447929270272', NULL, 0, 'INTENT', 'intent-resolve', 'com.nageoffer.ai.ragent.rag.core.intent.IntentResolver', 'resolve', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:52.427', '2026-03-01 16:51:52.745', 318, NULL, '2026-03-01 16:51:52', '2026-03-01 16:51:53', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030448604553216, '2028030444594798592', '2028030448579387392', '2028030447929270272', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:52.582', '2026-03-01 16:51:52.668', 86, NULL, '2026-03-01 16:51:53', '2026-03-01 16:51:53', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030456905080833, '2028030455231553536', '2028030456905080832', NULL, 0, 'REWRITE', 'query-rewrite-and-split', 'com.nageoffer.ai.ragent.rag.core.rewrite.MultiQuestionRewriteService', 'rewriteWithSplit', 'SUCCESS', NULL, '2026-03-01 16:51:54.567', '2026-03-01 16:51:54.807', 240, NULL, '2026-03-01 16:51:55', '2026-03-01 16:51:55', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030457240625153, '2028030455231553536', '2028030457240625152', '2028030456905080832', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:54.647', '2026-03-01 16:51:54.727', 80, NULL, '2026-03-01 16:51:55', '2026-03-01 16:51:55', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030458247258113, '2028030455231553536', '2028030458247258112', NULL, 0, 'INTENT', 'intent-resolve', 'com.nageoffer.ai.ragent.rag.core.intent.IntentResolver', 'resolve', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:54.887', '2026-03-01 16:51:55.206', 319, NULL, '2026-03-01 16:51:55', '2026-03-01 16:51:55', 0);
INSERT INTO `t_rag_trace_node` VALUES (2028030458918346753, '2028030455231553536', '2028030458918346752', '2028030458247258112', 1, 'LLM_ROUTING', 'llm-chat-routing', 'com.nageoffer.ai.ragent.infra.chat.RoutingLLMService', 'chat', 'ERROR', 'NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:55.047', '2026-03-01 16:51:55.132', 85, NULL, '2026-03-01 16:51:55', '2026-03-01 16:51:55', 0);

-- ----------------------------
-- Table structure for t_rag_trace_run
-- ----------------------------
DROP TABLE IF EXISTS `t_rag_trace_run`;
CREATE TABLE `t_rag_trace_run`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `trace_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '全局链路ID',
  `trace_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链路名称',
  `entry_method` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入口方法',
  `conversation_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会话ID',
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务ID',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户ID',
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'RUNNING' COMMENT 'RUNNING/SUCCESS/ERROR',
  `error_message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `start_time` datetime(3) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(3) NULL DEFAULT NULL COMMENT '结束时间',
  `duration_ms` bigint NULL DEFAULT NULL COMMENT '耗时毫秒',
  `extra_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '扩展字段(JSON)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_run_id`(`trace_id` ASC) USING BTREE,
  INDEX `idx_task_id`(`task_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'RAG Trace 运行记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_rag_trace_run
-- ----------------------------
INSERT INTO `t_rag_trace_run` VALUES (2028030275379798016, '2028030275308494848', 'rag-stream-chat', 'com.nageoffer.ai.ragent.rag.service.impl.RAGChatServiceImpl#streamChat', '2028030271927885824', '2028030275308494849', '2001523723396308993', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:11.271', '2026-03-01 16:51:12.843', 1572, '{\"questionLength\":6}', '2026-03-01 16:51:11', '2026-03-01 16:51:13', 0);
INSERT INTO `t_rag_trace_run` VALUES (2028030288415694850, '2028030288415694848', 'rag-stream-chat', 'com.nageoffer.ai.ragent.rag.service.impl.RAGChatServiceImpl#streamChat', '2028030287060934656', '2028030288415694849', '2001523723396308993', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:14.396', '2026-03-01 16:51:15.702', 1306, '{\"questionLength\":6}', '2026-03-01 16:51:14', '2026-03-01 16:51:16', 0);
INSERT INTO `t_rag_trace_run` VALUES (2028030340148240386, '2028030340148240384', 'rag-stream-chat', 'com.nageoffer.ai.ragent.rag.service.impl.RAGChatServiceImpl#streamChat', '2028030287060934656', '2028030340148240385', '2001523723396308993', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:26.730', '2026-03-01 16:51:27.855', 1125, '{\"questionLength\":6}', '2026-03-01 16:51:27', '2026-03-01 16:51:28', 0);
INSERT INTO `t_rag_trace_run` VALUES (2028030350210375680, '2028030350197792768', 'rag-stream-chat', 'com.nageoffer.ai.ragent.rag.service.impl.RAGChatServiceImpl#streamChat', '2028030287060934656', '2028030350197792769', '2001523723396308993', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:29.126', '2026-03-01 16:51:30.246', 1120, '{\"questionLength\":6}', '2026-03-01 16:51:29', '2026-03-01 16:51:30', 0);
INSERT INTO `t_rag_trace_run` VALUES (2028030444594798594, '2028030444594798592', 'rag-stream-chat', 'com.nageoffer.ai.ragent.rag.service.impl.RAGChatServiceImpl#streamChat', '2028030287060934656', '2028030444594798593', '2001523723396308993', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:51.632', '2026-03-01 16:51:52.818', 1186, '{\"questionLength\":6}', '2026-03-01 16:51:52', '2026-03-01 16:51:53', 0);
INSERT INTO `t_rag_trace_run` VALUES (2028030455235747840, '2028030455231553536', 'rag-stream-chat', 'com.nageoffer.ai.ragent.rag.service.impl.RAGChatServiceImpl#streamChat', '2028030287060934656', '2028030455231553537', '2001523723396308993', 'ERROR', 'CompletionException: java.lang.NullPointerException: Cannot invoke \"Object.hashCode()\" because \"key\" is null', '2026-03-01 16:51:54.168', '2026-03-01 16:51:55.286', 1118, '{\"questionLength\":6}', '2026-03-01 16:51:54', '2026-03-01 16:51:55', 0);

-- ----------------------------
-- Table structure for t_sample_question
-- ----------------------------
DROP TABLE IF EXISTS `t_sample_question`;
CREATE TABLE `t_sample_question`  (
  `id` bigint NOT NULL COMMENT 'ID',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '展示标题',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述或提示',
  `question` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '示例问题内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sample_question_deleted`(`deleted` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '示例问题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sample_question
-- ----------------------------

-- ----------------------------
-- Table structure for t_treatment_record
-- ----------------------------
DROP TABLE IF EXISTS `t_treatment_record`;
CREATE TABLE `t_treatment_record`  (
  `id` bigint NOT NULL COMMENT '治疗记录唯一标识',
  `case_id` bigint NOT NULL COMMENT '病例ID',
  `pig_id` bigint NOT NULL COMMENT '生猪ID',
  `treatment_date` datetime NOT NULL COMMENT '治疗日期',
  `operator_id` bigint NOT NULL COMMENT '操作人ID',
  `treatment_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '治疗类型：MEDICATION-药物治疗，SURGERY-手术，NURSING-护理',
  `drugs_used` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '使用药品（JSON格式）',
  `dosage_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用药详情',
  `treatment_effect` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '治疗效果：EFFECTIVE-有效，INEFFECTIVE-无效，IMPROVED-好转',
  `side_effects` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '副作用',
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_case_id`(`case_id` ASC) USING BTREE,
  INDEX `idx_pig_id`(`pig_id` ASC) USING BTREE,
  INDEX `idx_treatment_date`(`treatment_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '治疗记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_treatment_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名，唯一',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `role` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色：admin/user',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色类型：FARMER-养殖户，VETERINARIAN-兽医，ADMIN-管理员',
  `license_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执业证书号（兽医）',
  `organization` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属机构',
  `specialization` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业领域（兽医）',
  `years_experience` int NULL DEFAULT NULL COMMENT '从业年限',
  `avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除 0：正常 1：删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (2, 'vet001', 'vet123', 'user', '张兽医', NULL, NULL, 'VETERINARIAN', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_user` VALUES (3, 'farmer001', 'farmer123', 'user', '李养殖户', NULL, NULL, 'FARMER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_user` VALUES (2001523723396308993, 'admin', 'admin', 'admin', '超管', NULL, NULL, 'ADMIN', NULL, NULL, NULL, NULL, 'https://static.deepseek.com/user-avatar/G_6cuD8GbD53VwGRwisvCsZ6', '2025-12-21 15:55:44', '2025-12-21 15:55:44', 0);

SET FOREIGN_KEY_CHECKS = 1;
