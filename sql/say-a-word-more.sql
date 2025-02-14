# 创建数据库
CREATE DATABASE IF NOT EXISTS dduo_yiyan_db;

# 选择数据库
USE dduo_yiyan_db;

-- ----------------------------
-- 访问日志表
-- ----------------------------
DROP TABLE IF EXISTS `t_visit_log`;
CREATE TABLE IF NOT EXISTS `t_visit_log`
(
    `id`          int                                                          NOT NULL AUTO_INCREMENT COMMENT 'id',
    `ip_address`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问ip',
    `ip_source`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问地址',
    `os`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
    `create_time` datetime                                                     NOT NULL COMMENT '访问时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1671
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 异常日志表
-- ----------------------------
DROP TABLE IF EXISTS `t_exception_log`;
CREATE TABLE IF NOT EXISTS `t_exception_log`
(
    `id`             int                                                           NOT NULL AUTO_INCREMENT COMMENT '异常id',
    `module`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '异常模块',
    `uri`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '异常uri',
    `name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '异常名称',
    `description`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作描述',
    `error_method`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '异常方法',
    `message`        longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '异常信息',
    `params`         longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '请求参数',
    `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '请求方式',
    `ip_address`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '操作ip',
    `ip_source`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '操作地址',
    `os`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '操作系统',
    `browser`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '浏览器',
    `create_time`    datetime                                                      NOT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 248
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 句子表
-- ----------------------------
DROP TABLE IF EXISTS `t_sentences`;
CREATE TABLE IF NOT EXISTS `t_sentences`
(
    `id`          int                                                       NOT NULL AUTO_INCREMENT COMMENT '句子id',
    `content`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '句子内容',
    `create_time` datetime                                                  NOT NULL COMMENT '创建时间',
    `from`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提供者',
    `hot`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '热度',
    `other1`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拓展1',
    `other2`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拓展2',
    `other3`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拓展3',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- 标签表
-- ----------------------------
DROP TABLE IF EXISTS `t_tags`;
CREATE TABLE IF NOT EXISTS `t_tags`
(
    `id`          int                                                           NOT NULL AUTO_INCREMENT COMMENT '标签id',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
    `from`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提供者',
    `hot`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '热度',
    `other1`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拓展1',
    `other2`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拓展2',
    `other3`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '拓展3',
    `create_time` datetime                                                      NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`name`) USING BTREE COMMENT '标签名称唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
--  句子表和标签表的中间表
-- ----------------------------
DROP TABLE IF EXISTS `t_sentence_tag`;
CREATE TABLE IF NOT EXISTS `t_sentence_tag`
(
    `id` int      NOT NULL COMMENT 'id',
    `sentence_id` int      NOT NULL COMMENT '句子id',
    `tag_id`      int      NOT NULL COMMENT '标签id',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    FOREIGN KEY (`sentence_id`) REFERENCES `t_sentences` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `t_tags` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;


