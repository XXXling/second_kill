/*
 Navicat Premium Data Transfer

 Source Server         : mysql_local
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : db_second_kill

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 21/05/2020 14:26:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编号',
  `stock` bigint(20) NULL DEFAULT NULL COMMENT '库存',
  `purchase_time` date NULL DEFAULT NULL COMMENT '采购时间',
  `is_active` int(11) NULL DEFAULT 1 COMMENT '是否有效（1=是；0=否）',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (6, 'Java编程思想', 'book10010', 1000, '2019-05-18', 1, '2019-05-18 21:11:23', NULL);
INSERT INTO `item` VALUES (7, 'Spring实战第四版', 'book10011', 2000, '2019-05-18', 1, '2019-05-18 21:11:23', NULL);
INSERT INTO `item` VALUES (8, '深入分析JavaWeb', 'book10012', 2000, '2019-05-18', 1, '2019-05-18 21:11:23', NULL);

-- ----------------------------
-- Table structure for item_kill
-- ----------------------------
DROP TABLE IF EXISTS `item_kill`;
CREATE TABLE `item_kill`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `total` int(11) NULL DEFAULT NULL COMMENT '可被秒杀的总数',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '秒杀结束时间',
  `is_active` tinyint(11) NULL DEFAULT 1 COMMENT '是否有效（1=是；0=否）',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '待秒杀商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_kill
-- ----------------------------
INSERT INTO `item_kill` VALUES (1, 6, 58, '2020-05-01 21:59:07', '2020-05-31 21:59:11', 1, '2019-05-20 21:59:41');
INSERT INTO `item_kill` VALUES (2, 7, 0, '2020-05-01 21:59:19', '2020-05-31 21:59:11', 1, '2019-05-20 21:59:41');
INSERT INTO `item_kill` VALUES (3, 8, 97, '2020-05-01 18:58:26', '2020-05-31 21:59:07', 0, '2019-05-20 21:59:41');

-- ----------------------------
-- Table structure for item_kill_success
-- ----------------------------
DROP TABLE IF EXISTS `item_kill_success`;
CREATE TABLE `item_kill_success`  (
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀成功生成的订单编号',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `kill_id` int(11) NULL DEFAULT NULL COMMENT '秒杀id',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `status` tinyint(4) NULL DEFAULT -1 COMMENT '秒杀结果: -1无效  0成功(未付款)  1已付款  2已取消',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀成功订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_kill_success
-- ----------------------------
INSERT INTO `item_kill_success` VALUES ('460613129710350336', 6, 1, '10011', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133069987840', 6, 1, '10015', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133183234048', 6, 1, '10016', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133183234049', 6, 1, '10017', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133212594176', 6, 1, '10018', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133363589120', 6, 1, '10010', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133363589121', 6, 1, '10014', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133506195456', 6, 1, '10013', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133527166976', 6, 1, '10019', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460613133720104960', 6, 1, '10012', 0, '2020-05-20 22:33:14');
INSERT INTO `item_kill_success` VALUES ('460800020866936832', 6, 1, '10', -1, '2020-05-21 10:55:52');

-- ----------------------------
-- Table structure for random_code
-- ----------------------------
DROP TABLE IF EXISTS `random_code`;
CREATE TABLE `random_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of random_code
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `is_active` tinyint(11) NULL DEFAULT 1 COMMENT '是否有效(1=是；0=否)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1015 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10, 'debug', '80bab46abb7b1c4013f9971b8bec3868', '15627280601', '826125286@qq.com', 1, NULL, NULL);
INSERT INTO `user` VALUES (1010, 'a', 'a', 'a', '826125286@qq.com', 1, NULL, NULL);
INSERT INTO `user` VALUES (1011, 'b', 'a', 'a', '826125286@qq.com', 1, NULL, NULL);
INSERT INTO `user` VALUES (1012, 'd', 'd', 'd', '826125286@qq.com', 1, NULL, NULL);
INSERT INTO `user` VALUES (1013, 'c', 'c', 'c', '826125286@qq.com', 1, NULL, NULL);
INSERT INTO `user` VALUES (1014, 'e', 'e', 'e', '826125286@qq.com', 1, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
