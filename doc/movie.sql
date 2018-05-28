/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 100110
 Source Host           : localhost
 Source Database       : movie

 Target Server Type    : MySQL
 Target Server Version : 100110
 File Encoding         : utf-8

 Date: 05/28/2018 17:13:12 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` varchar(32) NOT NULL,
  `subjectId` varchar(32) DEFAULT NULL,
  `userId` varchar(32) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `submitDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `userId` (`userId`),
  KEY `subjectId` (`subjectId`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`subjectId`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `leaderboard`
-- ----------------------------
DROP TABLE IF EXISTS `leaderboard`;
CREATE TABLE `leaderboard` (
  `id` varchar(8) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE,
  CONSTRAINT `leaderboard_ibfk_1` FOREIGN KEY (`id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `playing`
-- ----------------------------
DROP TABLE IF EXISTS `playing`;
CREATE TABLE `playing` (
  `id` varchar(8) NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE,
  CONSTRAINT `playing_id` FOREIGN KEY (`id`) REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `subject`
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject` (
  `id` varchar(8) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `originalTitle` varchar(255) DEFAULT NULL,
  `ratingCount` int(11) DEFAULT NULL,
  `totalRating` double DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `directors` varchar(255) DEFAULT NULL,
  `casts` varchar(255) DEFAULT NULL,
  `writers` varchar(255) DEFAULT NULL,
  `pubDate` datetime DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `languages` varchar(255) DEFAULT NULL,
  `durations` varchar(255) DEFAULT NULL,
  `genres` varchar(255) DEFAULT NULL,
  `countries` varchar(255) DEFAULT NULL,
  `summary` text,
  `commentCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `title_casts_search` (`title`,`casts`) USING BTREE,
  KEY `title` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(32) NOT NULL,
  `userName` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `isAdmin` tinyint(1) DEFAULT '0',
  `enable` tinyint(1) DEFAULT '1',
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
