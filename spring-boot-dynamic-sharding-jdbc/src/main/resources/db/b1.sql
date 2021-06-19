/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : b1

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 19/06/2021 09:32:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_student_0
-- ----------------------------
DROP TABLE IF EXISTS `t_student_0`;
CREATE TABLE `t_student_0`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `user_id` bigint(0) NOT NULL COMMENT 't_user.id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_student_0
-- ----------------------------
INSERT INTO `t_student_0` VALUES (1405865469176446976, 0, '0', 1624019422267, 1624019422267);
INSERT INTO `t_student_0` VALUES (1405865472246677504, 3, '3', 1624019422958, 1624019422958);
INSERT INTO `t_student_0` VALUES (1405865472305397760, 6, '6', 1624019422973, 1624019422973);
INSERT INTO `t_student_0` VALUES (1405865472347340800, 9, '9', 1624019422982, 1624019422982);
INSERT INTO `t_student_0` VALUES (1405865472355729408, 0, '0', 1624019422984, 1624019422984);
INSERT INTO `t_student_0` VALUES (1405865472393478144, 3, '3', 1624019422993, 1624019422993);
INSERT INTO `t_student_0` VALUES (1405865472448004096, 6, '6', 1624019423007, 1624019423007);
INSERT INTO `t_student_0` VALUES (1405865472485752832, 9, '9', 1624019423016, 1624019423016);

-- ----------------------------
-- Table structure for t_student_1
-- ----------------------------
DROP TABLE IF EXISTS `t_student_1`;
CREATE TABLE `t_student_1`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `user_id` bigint(0) NOT NULL COMMENT 't_user.id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_student_1
-- ----------------------------
INSERT INTO `t_student_1` VALUES (1405865471902744576, 1, '1', 1624019422877, 1624019422877);
INSERT INTO `t_student_1` VALUES (1405865472259260416, 4, '4', 1624019422962, 1624019422962);
INSERT INTO `t_student_1` VALUES (1405865472322174976, 7, '7', 1624019422977, 1624019422977);
INSERT INTO `t_student_1` VALUES (1405865472368312320, 1, '1', 1624019422987, 1624019422987);
INSERT INTO `t_student_1` VALUES (1405865472401866752, 4, '4', 1624019422996, 1624019422996);
INSERT INTO `t_student_1` VALUES (1405865472464781312, 7, '7', 1624019423010, 1624019423010);

-- ----------------------------
-- Table structure for t_student_2
-- ----------------------------
DROP TABLE IF EXISTS `t_student_2`;
CREATE TABLE `t_student_2`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `user_id` bigint(0) NOT NULL COMMENT 't_user.id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_student_2
-- ----------------------------
INSERT INTO `t_student_2` VALUES (1405865472053739520, 2, '2', 1624019422912, 1624019422912);
INSERT INTO `t_student_2` VALUES (1405865472271843328, 5, '1405878060242173952', 1624019422965, 1624019422965);
INSERT INTO `t_student_2` VALUES (1405865472338952192, 8, '8', 1624019422980, 1624019422980);
INSERT INTO `t_student_2` VALUES (1405865472380895232, 2, '2', 1624019422990, 1624019422990);
INSERT INTO `t_student_2` VALUES (1405865472414449664, 5, '1405878060242173952', 1624019422999, 1624019422999);
INSERT INTO `t_student_2` VALUES (1405865472477364224, 8, '8', 1624019423013, 1624019423013);

-- ----------------------------
-- Table structure for t_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `user_id` bigint(0) NULL DEFAULT NULL,
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '老师表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_teacher
-- ----------------------------
INSERT INTO `t_teacher` VALUES (1405863748039606272, '1405878062968471552', 1405861487733374976, 1624019011912, 1624019011912);
INSERT INTO `t_teacher` VALUES (1405863748316430336, '1405878062968471552', 1405861487733374976, 1624019011942, 1624019011942);
INSERT INTO `t_teacher` VALUES (1405863748324818944, '0', 1405861487976644608, 1624019011944, 1624019011944);
INSERT INTO `t_teacher` VALUES (1405863748333207552, '1', 1405861487976644608, 1624019011945, 1624019011945);

-- ----------------------------
-- Table structure for t_user_0
-- ----------------------------
DROP TABLE IF EXISTS `t_user_0`;
CREATE TABLE `t_user_0`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_0
-- ----------------------------
INSERT INTO `t_user_0` VALUES (1405861484948357120, '0', 1624018472354, 1624018472354);
INSERT INTO `t_user_0` VALUES (1405861488261857280, '2', 1624018473102, 1624018473102);
INSERT INTO `t_user_0` VALUES (1405861488282828800, '4', 1624018473107, 1624018473107);

-- ----------------------------
-- Table structure for t_user_1
-- ----------------------------
DROP TABLE IF EXISTS `t_user_1`;
CREATE TABLE `t_user_1`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_1
-- ----------------------------
INSERT INTO `t_user_1` VALUES (1405861487733374976, '3', 1624018472977, 1624018472977);
INSERT INTO `t_user_1` VALUES (1405861488194748416, '7', 1624018473086, 1624018473086);
INSERT INTO `t_user_1` VALUES (1405861488236691456, '0', 1624018473096, 1624018473096);
INSERT INTO `t_user_1` VALUES (1405861488320577536, '6', 1624018473117, 1624018473117);

-- ----------------------------
-- Table structure for t_user_2
-- ----------------------------
DROP TABLE IF EXISTS `t_user_2`;
CREATE TABLE `t_user_2`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_2
-- ----------------------------
INSERT INTO `t_user_2` VALUES (1405861487750152192, '4', 1624018472981, 1624018472981);
INSERT INTO `t_user_2` VALUES (1405861488211525632, '8', 1624018473090, 1624018473090);
INSERT INTO `t_user_2` VALUES (1405861488295411712, '5', 1624018473111, 1624018473111);
INSERT INTO `t_user_2` VALUES (1405861488337354752, '8', 1624018473120, 1624018473120);

-- ----------------------------
-- Table structure for t_user_3
-- ----------------------------
DROP TABLE IF EXISTS `t_user_3`;
CREATE TABLE `t_user_3`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_3
-- ----------------------------
INSERT INTO `t_user_3` VALUES (1405861487976644608, '5', 1624018473034, 1624018473034);
INSERT INTO `t_user_3` VALUES (1405861488249274368, '1', 1624018473099, 1624018473099);
INSERT INTO `t_user_3` VALUES (1405861488270245888, '3', 1624018473104, 1624018473104);
INSERT INTO `t_user_3` VALUES (1405861488333160448, '7', 1624018473119, 1624018473119);

-- ----------------------------
-- Table structure for t_user_4
-- ----------------------------
DROP TABLE IF EXISTS `t_user_4`;
CREATE TABLE `t_user_4`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_4
-- ----------------------------
INSERT INTO `t_user_4` VALUES (1405861487573991424, '1', 1624018472939, 1624018472939);
INSERT INTO `t_user_4` VALUES (1405861487720792064, '2', 1624018472973, 1624018472973);
INSERT INTO `t_user_4` VALUES (1405861488182165504, '6', 1624018473083, 1624018473083);
INSERT INTO `t_user_4` VALUES (1405861488224108544, '9', 1624018473093, 1624018473093);
INSERT INTO `t_user_4` VALUES (1405861488349937664, '9', 1624018473123, 1624018473123);

SET FOREIGN_KEY_CHECKS = 1;
