/*
 Navicat MySQL Data Transfer

 Source Server         : local
 Source Server Version : 50616
 Source Host           : localhost
 Source Database       : hc

 Target Server Version : 50616
 File Encoding         : utf-8

 Date: 07/11/2017 14:56:39 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `hive_sync_status`
-- ----------------------------
DROP TABLE IF EXISTS `hive_sync_status`;
CREATE TABLE `hive_sync_status` (
  `tablename` varchar(128) NOT NULL,
  `aliasname` varchar(128) NOT NULL,
  `code` int(11) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tablename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `hive_table`
-- ----------------------------
DROP TABLE IF EXISTS `hive_table`;
CREATE TABLE `hive_table` (
  `table_name_en` varchar(64) NOT NULL,
  `table_name_zh` varchar(64) NOT NULL,
  `columns_zh` text NOT NULL,
  PRIMARY KEY (`table_name_en`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `p_role`
-- ----------------------------
DROP TABLE IF EXISTS `p_role`;
CREATE TABLE `p_role` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT 'Role Name',
  `seq` tinyint(4) NOT NULL COMMENT 'Lvs',
  `description` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT 'Role Description',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `p_role`
-- ----------------------------
BEGIN;
INSERT INTO `p_role` VALUES ('1', 'Administrator', '1', 'Have all permissions'), ('2', 'Devs', '2', 'Own add or delete'), ('3', 'Tourist', '3', 'Only viewer');
COMMIT;

-- ----------------------------
--  Table structure for `process`
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process` (
  `task_id` int(11) NOT NULL,
  `app_id` varchar(128) NOT NULL,
  `log` text NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `resources`
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources` (
  `resource_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT 'Resource Name',
  `url` varchar(255) NOT NULL,
  `parent_id` int(11) NOT NULL,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `resources`
-- ----------------------------
BEGIN;
INSERT INTO `resources` VALUES ('28', 'System Config', '/system', '-1'), ('29', 'User Manager', '/system/user', '28'), ('30', 'Role Manager ', '/system/role', '28'), ('31', 'Resource Manager', '/system/resource', '28'), ('33', 'Notice', '/system/notice', '28'), ('34', 'Config Manager', '/config', '-1'), ('35', 'Hive Table Manager', '/config/hive', '34'), ('36', 'Task Manager', '/tasks', '-1'), ('37', 'Start', '/tasks/executor/public/task', '36'), ('39', 'Stop', '/tasks/kill/public/task/', '36'), ('40', 'Export Data', '/export', '-1'), ('41', 'Advanced Manager', '/export/task/vip', '40'), ('42', 'HBase Schema', '/config/hbase', '34'), ('46', 'User Delete', '/system/user/delete', '28'), ('47', 'User Modify', '/system/user/modify', '28'), ('48', 'Applications', '/appcations', '-1'), ('49', 'Shutdown', '/application/yarn/del', '48'), ('50', 'Metrics', '/metrics', '-1'), ('51', 'HDFS', '/metrics/hdfs/del', '50'), ('52', 'Storage', '/storage', '-1'), ('53', 'Add Plugins', '/storage/plugin/add', '52'), ('54', 'Delete Plugins', '/storage/plugin/delete', '52');
COMMIT;

-- ----------------------------
--  Table structure for `role_resource`
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `role_resource`
-- ----------------------------
BEGIN;
INSERT INTO `role_resource` VALUES ('36', '1', '29'), ('37', '1', '30'), ('38', '1', '31'), ('60', '1', '33'), ('62', '1', '35'), ('63', '1', '37'), ('68', '1', '39'), ('70', '1', '41'), ('71', '1', '42'), ('75', '1', '47'), ('76', '1', '46'), ('77', '1', '49'), ('78', '1', '51'), ('79', '1', '53'), ('80', '1', '54');
COMMIT;

-- ----------------------------
--  Table structure for `rowkey`
-- ----------------------------
DROP TABLE IF EXISTS `rowkey`;
CREATE TABLE `rowkey` (
  `tname` varchar(32) NOT NULL,
  `regular` text NOT NULL,
  `author` varchar(32) NOT NULL,
  `tm` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`tname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `scheduler`
-- ----------------------------
DROP TABLE IF EXISTS `scheduler`;
CREATE TABLE `scheduler` (
  `task_id` int(11) NOT NULL,
  `cron_expression` varchar(32) NOT NULL,
  `task_switch` int(11) NOT NULL DEFAULT '0' COMMENT '0:run,1:cancle',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `storage`
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(64) NOT NULL,
  `port` int(11) NOT NULL,
  `type` varchar(16) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `modify` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tasks`
-- ----------------------------
DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `owner` varchar(64) NOT NULL,
  `status` varchar(32) NOT NULL,
  `log` varchar(255) NOT NULL,
  `result` varchar(32) NOT NULL,
  `fileSize` bigint(11) NOT NULL,
  `start_time` varchar(32) NOT NULL DEFAULT 'CURRENT_TIMESTAMP',
  `end_time` varchar(32) NOT NULL DEFAULT 'CURRENT_TIMESTAMP',
  `download` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_auto` int(11) NOT NULL DEFAULT '0' COMMENT '0:Manual,1:Auto',
  `content` text NOT NULL,
  `parent_id` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `user_role`
-- ----------------------------
BEGIN;
INSERT INTO `user_role` VALUES ('3', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rtxno` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(64) NOT NULL,
  `realname` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `users`
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('1', '1000', 'admin', '123456', 'admin@example.com', 'Administrator');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
