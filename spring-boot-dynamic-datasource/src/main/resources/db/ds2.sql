/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : ds2

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 06/01/2021 16:07:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '状态，-1：逻辑删除，0：禁用，1：启用',
  `class_id` bigint(0) NULL DEFAULT NULL COMMENT '班级id',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (25665703687, NULL, NULL, NULL, 1609918555875, 1609918555875);
INSERT INTO `t_user` VALUES (35939112841, NULL, NULL, NULL, 1609918556111, 1609918556111);
INSERT INTO `t_user` VALUES (39753255459, NULL, NULL, NULL, 1609918556337, 1609918556337);
INSERT INTO `t_user` VALUES (49293510738, NULL, NULL, NULL, 1609918556420, 1609918556420);
INSERT INTO `t_user` VALUES (61963821059, NULL, NULL, NULL, 1609918556015, 1609918556015);
INSERT INTO `t_user` VALUES (63265530474, NULL, NULL, NULL, 1609918555958, 1609918555958);
INSERT INTO `t_user` VALUES (65934112489, NULL, NULL, NULL, 1609918556200, 1609918556200);
INSERT INTO `t_user` VALUES (95260419914, NULL, NULL, NULL, 1609918555763, 1609918555763);
INSERT INTO `t_user` VALUES (96898225524, NULL, NULL, NULL, 1609918556060, 1609918556060);

SET FOREIGN_KEY_CHECKS = 1;
