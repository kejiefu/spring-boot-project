DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `status` tinyint DEFAULT NULL COMMENT '状态，-1：逻辑删除，0：禁用，1：启用',
  `class_id` bigint DEFAULT NULL COMMENT '班级id',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE `t_class` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '班级名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班级表';