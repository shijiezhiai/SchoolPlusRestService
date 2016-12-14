/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : schoolplus

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2016-11-22 23:21:11
*/

DROP DATABASE IF EXISTS `schoolplus`;

CREATE DATABASE `schoolplus`;

USE schoolplus;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ad
-- ----------------------------
DROP TABLE IF EXISTS `ad`;
CREATE TABLE `ad` (
  `id` bigint(64) unsigned NOT NULL,
  `pic_id` bigint(64) unsigned NOT NULL,
  `description` text,
  `advertiser` varchar(32) NOT NULL,
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ad_ad_pic` (`pic_id`),
  CONSTRAINT `fk_ad_ad_pic` FOREIGN KEY (`pic_id`) REFERENCES `ad_picture` (`id`),
  CONSTRAINT `fk_ad_pic` FOREIGN KEY (`pic_id`) REFERENCES `ad_picture` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ad
-- ----------------------------

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` smallint(16) unsigned NOT NULL,
  `username` varchar(255) NOT NULL,
  `passwd_md5` char(16) NOT NULL,
  `mobile_phone_number` char(11) NOT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `privilege` smallint(16) unsigned NOT NULL DEFAULT '0' COMMENT 'Bitwise OR of the admin''s privilege',
  `auditor_id` smallint(16) unsigned DEFAULT NULL COMMENT 'Who gave privilege to this admin.',
  `is_super` bit(1) DEFAULT b'0' COMMENT 'Is this admin a super administrator.',
  PRIMARY KEY (`id`),
  KEY `username_idx` (`username`),
  KEY `fk_admin_admin` (`auditor_id`),
  CONSTRAINT `fk_admin_admin` FOREIGN KEY (`auditor_id`) REFERENCES `admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------

-- ----------------------------
-- Table structure for ad_picture
-- ----------------------------
DROP TABLE IF EXISTS `ad_picture`;
CREATE TABLE `ad_picture` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `src` varchar(255) NOT NULL,
  `ad_id` bigint(64) unsigned NOT NULL,
  `height` smallint(16) unsigned DEFAULT NULL,
  `width` smallint(16) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_ad_pic_ad` (`ad_id`),
  CONSTRAINT `fk_ad_pic_ad` FOREIGN KEY (`ad_id`) REFERENCES `ad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ad_picture
-- ----------------------------

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` bigint(64) unsigned NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `type` smallint(5) unsigned NOT NULL COMMENT 'Type of the article: notification(1), teaching material(2), homework(3), activity(4)',
  `author_id` int(32) unsigned NOT NULL,
  `audited` bit(1) DEFAULT b'0' COMMENT 'Has this article been audited yet? Some type can have no this field.',
  `auditor_id` smallint(16) unsigned DEFAULT NULL COMMENT 'Who audited this article.',
  PRIMARY KEY (`id`),
  KEY `fk_article_school_admin` (`auditor_id`),
  KEY `fk_article_teacher` (`author_id`),
  CONSTRAINT `fk_article_school_admin` FOREIGN KEY (`auditor_id`) REFERENCES `school_admin` (`id`),
  CONSTRAINT `fk_article_teacher` FOREIGN KEY (`author_id`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for article_picture
-- ----------------------------
DROP TABLE IF EXISTS `article_picture`;
CREATE TABLE `article_picture` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `src` varchar(255) NOT NULL,
  `article_id` bigint(64) unsigned NOT NULL,
  `height` smallint(16) unsigned DEFAULT NULL,
  `width` smallint(16) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_article_pic_article` (`article_id`),
  CONSTRAINT `fk_article_pic_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article_picture
-- ----------------------------

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `id` int(32) unsigned NOT NULL AUTO_INCREMENT,
  `grade` tinyint(8) unsigned NOT NULL,
  `class` tinyint(8) unsigned NOT NULL,
  `school_id` smallint(16) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `primary_key` (`id`),
  KEY `fk_class_school` (`school_id`),
  CONSTRAINT `fk_class_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of class
-- ----------------------------

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id` tinyint(8) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of company
-- ----------------------------

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(32) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `school_id` smallint(16) unsigned NOT NULL,
  `class_id` int(32) unsigned NOT NULL,
  `teacher_id` int(32) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_course_school` (`school_id`),
  KEY `fk_course_teacher` (`teacher_id`),
  CONSTRAINT `fk_course_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`),
  CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------

-- ----------------------------
-- Table structure for course_student_relation
-- ----------------------------
DROP TABLE IF EXISTS `course_student_relation`;
CREATE TABLE `course_student_relation` (
  `course_id` int(32) unsigned NOT NULL,
  `student_id` bigint(64) unsigned NOT NULL,
  KEY `fk_course_student_relation_student` (`student_id`),
  CONSTRAINT `fk_course_student_relation_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_student_relation
-- ----------------------------

-- ----------------------------
-- Table structure for parent
-- ----------------------------
DROP TABLE IF EXISTS `parent`;
CREATE TABLE `parent` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `passwd_md5` varchar(16) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile_phone_number` char(11) NOT NULL,
  `can_push` bit(1) NOT NULL DEFAULT b'1' COMMENT 'Should push message to his(her) phone or not',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_idx` (`username`),
  UNIQUE KEY `mobile_phone_number_idx` (`mobile_phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of parent
-- ----------------------------

-- ----------------------------
-- Table structure for school
-- ----------------------------
DROP TABLE IF EXISTS `school`;
CREATE TABLE `school` (
  `id` smallint(16) unsigned NOT NULL,
  `name` varchar(32) NOT NULL,
  `city` varchar(30) NOT NULL,
  `district` varchar(10) NOT NULL,
  `address` varchar(100) NOT NULL,
  `auditor_id` smallint(16) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of school
-- ----------------------------

-- ----------------------------
-- Table structure for school_admin
-- ----------------------------
DROP TABLE IF EXISTS `school_admin`;
CREATE TABLE `school_admin` (
  `id` smallint(16) unsigned NOT NULL,
  `username` varchar(255) NOT NULL,
  `password_md5` char(16) NOT NULL,
  `email` varchar(255) NOT NULL,
  `mobile_phone_number` char(11) NOT NULL,
  `school_id` smallint(16) unsigned NOT NULL,
  `privilege` int(32) unsigned NOT NULL DEFAULT '0' COMMENT 'Bitwise OR of his privileges',
  `is_super` bit(1) DEFAULT b'0' COMMENT 'Is this school admin an super school administrator?',
  `audited` tinyint(1) unsigned DEFAULT NULL COMMENT 'This is a super school admin, and he is audited by an admin.',
  `auditor_id` smallint(16) unsigned DEFAULT NULL COMMENT 'This is a normal school admin, and he is privileged by a school admin.',
  PRIMARY KEY (`id`),
  KEY `fk_school_admin_school` (`school_id`),
  KEY `fk_school_admin_school_admin` (`auditor_id`),
  CONSTRAINT `fk_school_admin_admin` FOREIGN KEY (`auditor_id`) REFERENCES `admin` (`id`),
  CONSTRAINT `fk_school_admin_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`),
  CONSTRAINT `fk_school_admin_school_admin` FOREIGN KEY (`auditor_id`) REFERENCES `school_admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of school_admin
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` bigint(64) unsigned NOT NULL COMMENT 'student number',
  `name` varchar(10) NOT NULL,
  `gender` bit(1) DEFAULT NULL COMMENT '0 for male, 1 for female',
  `birthday` date NOT NULL,
  `age` tinyint(8) unsigned NOT NULL,
  `gps_dev_id` char(15) NOT NULL,
  `school_id` smallint(16) unsigned NOT NULL,
  `class_id` int(32) unsigned NOT NULL,
  `parent_id` bigint(64) unsigned NOT NULL,
  PRIMARY KEY (`id`,`school_id`),
  UNIQUE KEY `gps_dev_id_idx` (`gps_dev_id`),
  KEY `parent_id_idx` (`parent_id`),
  KEY `fk_student_class` (`class_id`),
  KEY `fk_student_school` (`school_id`),
  CONSTRAINT `fk_student_class` FOREIGN KEY (`class_id`) REFERENCES `class` (`id`),
  CONSTRAINT `fk_student_parent` FOREIGN KEY (`parent_id`) REFERENCES `parent` (`id`),
  CONSTRAINT `fk_student_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(32) unsigned NOT NULL,
  `name` varchar(16) NOT NULL,
  `type` tinyint(8) unsigned NOT NULL COMMENT 'Which type of entity is the tag associated: video(1), article(2)',
  `num_of_entity` bigint(64) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for tag_article_relation
-- ----------------------------
DROP TABLE IF EXISTS `tag_article_relation`;
CREATE TABLE `tag_article_relation` (
  `tag_id` int(32) unsigned NOT NULL,
  `article_id` bigint(64) unsigned NOT NULL,
  PRIMARY KEY (`tag_id`,`article_id`),
  KEY `fk_tag_article_relation_article` (`article_id`),
  CONSTRAINT `fk_tag_article_relation_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag_article_relation
-- ----------------------------

-- ----------------------------
-- Table structure for tag_video_relation
-- ----------------------------
DROP TABLE IF EXISTS `tag_video_relation`;
CREATE TABLE `tag_video_relation` (
  `tag_id` int(32) unsigned NOT NULL,
  `video_id` bigint(64) unsigned NOT NULL,
  PRIMARY KEY (`tag_id`,`video_id`),
  KEY `fk_tag_video_relation_video` (`video_id`),
  CONSTRAINT `fk_tag_video_relation_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),
  CONSTRAINT `fk_tag_video_relation_video` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tag_video_relation
-- ----------------------------

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` int(32) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `passwd_md5` char(16) NOT NULL,
  `name` varchar(20) NOT NULL,
  `gender` bit(1) DEFAULT NULL COMMENT '0 for male, 1 for female',
  `age` tinyint(8) unsigned DEFAULT NULL,
  `mobile_phone_number` char(11) NOT NULL,
  `phone_number` char(8) DEFAULT NULL,
  `course_id` int(32) unsigned DEFAULT NULL,
  `school_id` smallint(16) unsigned NOT NULL,
  `emp_number` bigint(64) unsigned DEFAULT NULL,
  `class_id` int(32) unsigned DEFAULT NULL,
  `is_head` bit(1) DEFAULT b'0' COMMENT 'Is this teacher a head teacher?',
  `email` varchar(255) DEFAULT NULL,
  `is_audited` bit(1) DEFAULT b'0' COMMENT 'Has this registration been authorized by school admin?',
  `auditor_id` smallint(16) unsigned DEFAULT NULL COMMENT 'Who audited this teacher and give the privilege.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mobile_phone_number_idx` (`mobile_phone_number`),
  KEY `username_idx` (`username`),
  KEY `fk_teacher_class` (`class_id`),
  KEY `fk_teacher_school` (`school_id`),
  KEY `fk_teacher_school_admin` (`auditor_id`),
  CONSTRAINT `fk_teacher_class` FOREIGN KEY (`class_id`) REFERENCES `class` (`id`),
  CONSTRAINT `fk_teacher_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`),
  CONSTRAINT `fk_teacher_school_admin` FOREIGN KEY (`auditor_id`) REFERENCES `school_admin` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------

-- ----------------------------
-- Table structure for textbook
-- ----------------------------
DROP TABLE IF EXISTS `textbook`;
CREATE TABLE `textbook` (
  `isbn` varchar(13) NOT NULL COMMENT 'ISBN',
  `name` varchar(50) NOT NULL,
  `price` double(3,2) unsigned NOT NULL,
  `num_pages` smallint(16) unsigned DEFAULT NULL,
  `press` varchar(255) DEFAULT NULL,
  `cover_id` bigint(64) unsigned DEFAULT NULL,
  `uploader_id` int(32) unsigned DEFAULT NULL,
  PRIMARY KEY (`isbn`),
  KEY `fk_textbook_picture` (`cover_id`),
  KEY `fk_textbook_teacher` (`uploader_id`),
  CONSTRAINT `fk_textbook_picture` FOREIGN KEY (`cover_id`) REFERENCES `ad_picture` (`id`),
  CONSTRAINT `fk_textbook_teacher` FOREIGN KEY (`uploader_id`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of textbook
-- ----------------------------

-- ----------------------------
-- Table structure for textbook_picture
-- ----------------------------
DROP TABLE IF EXISTS `textbook_picture`;
CREATE TABLE `textbook_picture` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `src` varchar(255) NOT NULL,
  `textbook_isbn` varchar(13) NOT NULL,
  `height` smallint(16) unsigned DEFAULT NULL,
  `width` smallint(16) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_textbook_pic_textbook` (`textbook_isbn`),
  CONSTRAINT `fk_textbook_pic_textbook` FOREIGN KEY (`textbook_isbn`) REFERENCES `textbook` (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of textbook_picture
-- ----------------------------

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` bigint(64) unsigned NOT NULL,
  `title` varchar(255) NOT NULL,
  `src` varchar(255) NOT NULL,
  `description` text,
  `uploader_id` int(32) unsigned NOT NULL COMMENT 'Currently only teacher can upload video. So uploader_id is teacher_id.',
  `is_free` bit(1) DEFAULT b'1',
  `audited` bit(1) DEFAULT b'0',
  `auditor_id` smallint(16) unsigned DEFAULT NULL COMMENT 'The auditor id, currently school_admin id.',
  PRIMARY KEY (`id`),
  KEY `fk_video_teacher` (`uploader_id`),
  CONSTRAINT `fk_video_teacher` FOREIGN KEY (`uploader_id`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of video
-- ----------------------------

-- ----------------------------
-- Table structure for video_picture
-- ----------------------------
DROP TABLE IF EXISTS `video_picture`;
CREATE TABLE `video_picture` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `src` varchar(255) NOT NULL,
  `video_id` bigint(64) unsigned NOT NULL,
  `height` smallint(16) unsigned DEFAULT NULL,
  `width` smallint(16) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_video_pic_article` (`video_id`),
  CONSTRAINT `fk_video_pic_article` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of video_picture
-- ----------------------------
