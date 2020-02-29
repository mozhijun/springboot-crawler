/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : crawler

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 29/02/2020 10:38:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel`  (
  `channel_id` bigint(11) NOT NULL COMMENT '频道id',
  `channel_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '频道名称',
  `sort_num` int(255) NOT NULL COMMENT '频道排序num',
  PRIMARY KEY (`channel_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `comment_id` bigint(20) NOT NULL COMMENT '评论',
  `comment_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评论内容',
  `uid` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `passtime` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '过审时间',
  `content_id` bigint(20) NULL DEFAULT NULL COMMENT '内容ID',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `content_id`(`content_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  CONSTRAINT `t_comment_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `t_content` (`content_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `t_comment_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `t_user` (`uid`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_content
-- ----------------------------
DROP TABLE IF EXISTS `t_content`;
CREATE TABLE `t_content`  (
  `content_id` bigint(20) NOT NULL COMMENT '内容id',
  `channel_id` bigint(20) NULL DEFAULT NULL COMMENT '频道编号',
  `forum_id` bigint(11) NULL DEFAULT NULL COMMENT '圈子编号',
  `uid` bigint(255) NULL DEFAULT NULL COMMENT '会员id',
  `status` int(255) NULL DEFAULT NULL COMMENT '状态 4-过审',
  `comment_count` int(11) NULL DEFAULT NULL COMMENT '评论数量',
  `bookmark_count` int(255) NULL DEFAULT NULL COMMENT '收藏数量',
  `content_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '标题或正文',
  `like_count` int(255) NULL DEFAULT NULL COMMENT '点赞数量',
  `hate_count` int(255) NULL DEFAULT NULL COMMENT '讨厌数量',
  `share_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分享url',
  `share_count` int(255) NULL DEFAULT NULL COMMENT '分享数量',
  `passtime` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布时间',
  `content_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容类型 text/image/gif/video',
  `source_id` bigint(20) NULL DEFAULT NULL COMMENT '来源编号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '数据创建时间',
  PRIMARY KEY (`content_id`) USING BTREE,
  INDEX `source_id`(`source_id`) USING BTREE,
  INDEX `channel_id`(`channel_id`) USING BTREE,
  INDEX `forum_id`(`forum_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  CONSTRAINT `t_content_ibfk_1` FOREIGN KEY (`channel_id`) REFERENCES `t_channel` (`channel_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_content_ibfk_2` FOREIGN KEY (`forum_id`) REFERENCES `t_forum` (`forum_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_content_ibfk_3` FOREIGN KEY (`uid`) REFERENCES `t_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `t_content_ibfk_4` FOREIGN KEY (`source_id`) REFERENCES `t_source` (`source_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `id` int(120) NOT NULL AUTO_INCREMENT COMMENT '访问用户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '访问用户姓名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '访问用户的登录密码',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '访问用户微信id',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '访问用户微信昵称',
  `header` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'https://www.xmlvhy.com/images/user.png' COMMENT '访问用户头像url',
  `login_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户登录名',
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户签名',
  `sex` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '1 男 2 女 0 保密',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_forum
-- ----------------------------
DROP TABLE IF EXISTS `t_forum`;
CREATE TABLE `t_forum`  (
  `forum_id` bigint(20) NOT NULL COMMENT '圈子',
  `post_count` int(255) NULL DEFAULT NULL COMMENT '发帖数量',
  `logo` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Logo URL',
  `forum_sort` int(255) NULL DEFAULT NULL COMMENT '频道顺序',
  `forum_status` int(255) NULL DEFAULT NULL COMMENT '状态',
  `info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '圈子简介',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '圈子名称',
  `user_count` int(255) NULL DEFAULT NULL COMMENT '参与人数',
  PRIMARY KEY (`forum_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_image
-- ----------------------------
DROP TABLE IF EXISTS `t_image`;
CREATE TABLE `t_image`  (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片',
  `big_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '经过压缩后的大图(600P)',
  `watermarker_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '带水印大图',
  `raw_height` int(11) NULL DEFAULT NULL COMMENT '原图宽度',
  `raw_width` int(11) NULL DEFAULT NULL COMMENT '原图高度',
  `thumb_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图',
  `content_id` bigint(20) NOT NULL COMMENT '内容编号',
  PRIMARY KEY (`image_id`) USING BTREE,
  INDEX `t_image_ibfk_1`(`content_id`) USING BTREE,
  CONSTRAINT `t_image_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `t_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1126 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_source
-- ----------------------------
DROP TABLE IF EXISTS `t_source`;
CREATE TABLE `t_source`  (
  `source_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `channel_id` int(11) NOT NULL,
  `response_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime(0) NOT NULL,
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`source_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7618 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `uid` bigint(20) NOT NULL,
  `header` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像Url',
  `is_vip` int(255) NULL DEFAULT NULL COMMENT '是否是vip 0-否 1-是',
  `is_v` int(255) NULL DEFAULT NULL COMMENT '??',
  `room_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '房间url',
  `room_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ' 房间名',
  `room_role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '房间角色',
  `room_icon` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '房间logo',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_video
-- ----------------------------
DROP TABLE IF EXISTS `t_video`;
CREATE TABLE `t_video`  (
  `video_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `video_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '在线播放URL',
  `download_url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下载地址',
  `width` int(11) NULL DEFAULT NULL COMMENT '宽度',
  `height` int(11) NULL DEFAULT NULL COMMENT '高度',
  `playfcount` int(255) NULL DEFAULT NULL COMMENT '??',
  `duration` int(255) NULL DEFAULT NULL COMMENT '长度（秒）',
  `playcount` int(255) NOT NULL COMMENT '播放量',
  `thumb` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图（等比）',
  `thumb_small` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图（正方）',
  `content_id` bigint(20) NULL DEFAULT NULL COMMENT '内容编号',
  PRIMARY KEY (`video_id`) USING BTREE,
  INDEX `t_video_ibfk_1`(`content_id`) USING BTREE,
  CONSTRAINT `t_video_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `t_content` (`content_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2000 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
