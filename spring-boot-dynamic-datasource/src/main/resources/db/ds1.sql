/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : ds1

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 06/01/2021 16:07:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_class
-- ----------------------------
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '班级表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES (2517285832, NULL, 1609920396268, 1609920396268);
INSERT INTO `t_class` VALUES (20066653544, NULL, 1609920396538, 1609920396538);
INSERT INTO `t_class` VALUES (20732179025, NULL, 1609920396492, 1609920396492);
INSERT INTO `t_class` VALUES (39523017690, NULL, 1609920396103, 1609920396103);
INSERT INTO `t_class` VALUES (50428862654, NULL, 1609920396340, 1609920396340);
INSERT INTO `t_class` VALUES (51376172442, NULL, 1609920396418, 1609920396418);
INSERT INTO `t_class` VALUES (71197436386, NULL, 1609920396589, 1609920396589);
INSERT INTO `t_class` VALUES (74581584207, NULL, 1609920396034, 1609920396034);
INSERT INTO `t_class` VALUES (92092408102, NULL, 1609920396672, 1609920396672);

SET FOREIGN_KEY_CHECKS = 1;
