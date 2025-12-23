/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50744
Source Host           : localhost:3306
Source Database       : hospital_registration

Target Server Type    : MYSQL
Target Server Version : 50744
File Encoding         : 65001

Date: 2025-12-18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 创建数据库
-- ----------------------------
DROP DATABASE IF EXISTS `hospital_registration`;
CREATE DATABASE `hospital_registration` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `hospital_registration`;

-- ----------------------------
-- 1、部门表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- 2、用户信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户 01患者）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `open_id` varchar(100) DEFAULT NULL COMMENT '微信openId',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  `section_id` bigint(20) DEFAULT NULL COMMENT '科室ID（医生用）',
  `outpatien_id` bigint(20) DEFAULT NULL COMMENT '门诊ID（医生用）',
  `rank1` varchar(50) DEFAULT NULL COMMENT '职称',
  `age` int(3) DEFAULT NULL COMMENT '年龄',
  `type` varchar(20) DEFAULT NULL COMMENT '类型',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_section_id` (`section_id`),
  KEY `idx_open_id` (`open_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='岗位信息表';

-- ----------------------------
-- 4、角色信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` char(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` char(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- 6、用户和角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- 7、角色和菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- ----------------------------
-- 8、用户与岗位关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';

-- ----------------------------
-- 9、角色和部门关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';

-- ----------------------------
-- 10、字典类型表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ----------------------------
-- 11、字典数据表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`),
  KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- ----------------------------
-- 12、参数配置表
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

-- ----------------------------
-- 13、系统访问记录
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录';

-- ----------------------------
-- 14、操作日志记录
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_oper_time` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

-- ----------------------------
-- 15、通知公告表
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';

-- ----------------------------
-- 16、科室信息表
-- ----------------------------
DROP TABLE IF EXISTS `p_section`;
CREATE TABLE `p_section` (
  `section_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '科室ID',
  `section_name` varchar(100) NOT NULL COMMENT '科室名称',
  `section_code` varchar(50) DEFAULT NULL COMMENT '科室编码',
  `section_desc` varchar(500) DEFAULT NULL COMMENT '科室描述',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='科室信息表';

-- ----------------------------
-- 17、门诊信息表
-- ----------------------------
DROP TABLE IF EXISTS `p_outpatien`;
CREATE TABLE `p_outpatien` (
  `outpatien_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '门诊ID',
  `section_id` bigint(20) NOT NULL COMMENT '科室ID',
  `outpatien_name` varchar(100) NOT NULL COMMENT '门诊名称',
  `room_name` varchar(100) DEFAULT NULL COMMENT '诊室名称',
  `room_number` varchar(50) DEFAULT NULL COMMENT '诊室编号',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`outpatien_id`),
  KEY `idx_section_id` (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='门诊信息表';

-- ----------------------------
-- 18、医生排班信息表
-- ----------------------------
DROP TABLE IF EXISTS `p_scheduling`;
CREATE TABLE `p_scheduling` (
  `scheduling_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `doctor_id` bigint(20) NOT NULL COMMENT '医生ID（关联sys_user）',
  `section_id` bigint(20) NOT NULL COMMENT '科室ID',
  `outpatien_id` bigint(20) DEFAULT NULL COMMENT '门诊ID',
  `scheduling_date` date NOT NULL COMMENT '排班日期',
  `time_slot` varchar(20) DEFAULT NULL COMMENT '时间段（上午/下午/晚上）',
  `start_time` time DEFAULT NULL COMMENT '开始时间',
  `end_time` time DEFAULT NULL COMMENT '结束时间',
  `total_number` int(11) DEFAULT '20' COMMENT '总号源数',
  `residual_number` int(11) DEFAULT '20' COMMENT '剩余号源数',
  `registration_fee` decimal(10,2) DEFAULT '0.00' COMMENT '挂号费',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用 2已满）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`scheduling_id`),
  KEY `idx_doctor_id` (`doctor_id`),
  KEY `idx_section_id` (`section_id`),
  KEY `idx_scheduling_date` (`scheduling_date`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='医生排班信息表';

-- ----------------------------
-- 19、预约挂号信息表
-- ----------------------------
DROP TABLE IF EXISTS `p_registration`;
CREATE TABLE `p_registration` (
  `registration_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '挂号ID',
  `registration_no` varchar(50) NOT NULL COMMENT '挂号单号',
  `patient_id` bigint(20) NOT NULL COMMENT '患者ID（关联sys_user）',
  `patient_name` varchar(50) NOT NULL COMMENT '患者姓名',
  `patient_phone` varchar(11) DEFAULT NULL COMMENT '患者电话',
  `doctor_id` bigint(20) NOT NULL COMMENT '医生ID（关联sys_user）',
  `doctor_name` varchar(50) DEFAULT NULL COMMENT '医生姓名',
  `section_id` bigint(20) NOT NULL COMMENT '科室ID',
  `section_name` varchar(100) DEFAULT NULL COMMENT '科室名称',
  `outpatien_id` bigint(20) DEFAULT NULL COMMENT '门诊ID',
  `scheduling_id` bigint(20) DEFAULT NULL COMMENT '排班ID',
  `registration_date` date NOT NULL COMMENT '挂号日期',
  `time_slot` varchar(20) DEFAULT NULL COMMENT '时间段',
  `registration_fee` decimal(10,2) DEFAULT '0.00' COMMENT '挂号费',
  `status` char(1) DEFAULT '0' COMMENT '状态（0待就诊 1已就诊 2已取消 3已过期）',
  `pay_status` char(1) DEFAULT '0' COMMENT '支付状态（0未支付 1已支付 2已退款）',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(500) DEFAULT NULL COMMENT '取消原因',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`registration_id`),
  UNIQUE KEY `uk_registration_no` (`registration_no`),
  KEY `idx_patient_id` (`patient_id`),
  KEY `idx_doctor_id` (`doctor_id`),
  KEY `idx_registration_date` (`registration_date`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='预约挂号信息表';

-- ----------------------------
-- 20、充值记录表
-- ----------------------------
DROP TABLE IF EXISTS `p_pay`;
CREATE TABLE `p_pay` (
  `pay_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值ID',
  `pay_no` varchar(50) NOT NULL COMMENT '充值单号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '充值金额',
  `pay_type` char(1) DEFAULT '1' COMMENT '充值类型（1微信 2支付宝 3现金）',
  `pay_status` char(1) DEFAULT '0' COMMENT '充值状态（0待支付 1已支付 2已退款 3已取消）',
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '交易流水号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_reason` varchar(500) DEFAULT NULL COMMENT '退款原因',
  `balance_before` decimal(10,2) DEFAULT '0.00' COMMENT '充值前余额',
  `balance_after` decimal(10,2) DEFAULT '0.00' COMMENT '充值后余额',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pay_id`),
  UNIQUE KEY `uk_pay_no` (`pay_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_pay_time` (`pay_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表';

-- ----------------------------
-- 21、收藏信息表
-- ----------------------------
DROP TABLE IF EXISTS `p_collect`;
CREATE TABLE `p_collect` (
  `collect_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `doctor_id` bigint(20) NOT NULL COMMENT '医生ID',
  `doctor_name` varchar(50) DEFAULT NULL COMMENT '医生姓名',
  `section_id` bigint(20) DEFAULT NULL COMMENT '科室ID',
  `section_name` varchar(100) DEFAULT NULL COMMENT '科室名称',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`collect_id`),
  UNIQUE KEY `uk_user_doctor` (`user_id`,`doctor_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='收藏信息表';

-- ----------------------------
-- 22、评价信息表
-- ----------------------------
DROP TABLE IF EXISTS `p_evaluate`;
CREATE TABLE `p_evaluate` (
  `evaluate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `registration_id` bigint(20) DEFAULT NULL COMMENT '挂号ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `doctor_id` bigint(20) NOT NULL COMMENT '医生ID',
  `doctor_name` varchar(50) DEFAULT NULL COMMENT '医生姓名',
  `section_id` bigint(20) DEFAULT NULL COMMENT '科室ID',
  `section_name` varchar(100) DEFAULT NULL COMMENT '科室名称',
  `rating` int(1) DEFAULT '5' COMMENT '评分（1-5星）',
  `evaluate_content` varchar(1000) DEFAULT NULL COMMENT '评价内容',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1隐藏）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`evaluate_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_doctor_id` (`doctor_id`),
  KEY `idx_registration_id` (`registration_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='评价信息表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '医院总部', 0, '管理员', '15888888888', 'admin@hospital.com', '0', '0', 'admin', SYSDATE(), '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '行政部', 1, '张三', '15888888888', 'admin@hospital.com', '0', '0', 'admin', SYSDATE(), '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '医疗部', 2, '李四', '15888888888', 'admin@hospital.com', '0', '0', 'admin', SYSDATE(), '', NULL);

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '管理员', '00', 'admin@hospital.com', '15888888888', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU.hIfnb8we', '0', '0', '127.0.0.1', SYSDATE(), NULL, 0.00, NULL, NULL, NULL, NULL, NULL, 'admin', SYSDATE(), '', NULL, '管理员');

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', SYSDATE(), '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '医生', 'doctor', 2, '2', 1, 1, '0', '0', 'admin', SYSDATE(), '', NULL, '医生角色');
INSERT INTO `sys_role` VALUES (3, '患者', 'patient', 3, '5', 1, 1, '0', '0', 'admin', SYSDATE(), '', NULL, '患者角色');

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '1', '0', 'M', '0', '0', '', 'system', 'admin', SYSDATE(), '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '业务管理', 0, 2, 'business', NULL, '', '1', '0', 'M', '0', '0', '', 'guide', 'admin', SYSDATE(), '', NULL, '业务管理目录');

-- 系统管理子菜单
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '1', '0', 'C', '0', '0', 'system:user:list', 'user', 'admin', SYSDATE(), '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '1', '0', 'C', '0', '0', 'system:role:list', 'peoples', 'admin', SYSDATE(), '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '1', '0', 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', SYSDATE(), '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '1', '0', 'C', '0', '0', 'system:dept:list', 'tree', 'admin', SYSDATE(), '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '1', '0', 'C', '0', '0', 'system:post:list', 'post', 'admin', SYSDATE(), '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '1', '0', 'C', '0', '0', 'system:dict:list', 'dict', 'admin', SYSDATE(), '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '1', '0', 'C', '0', '0', 'system:config:list', 'edit', 'admin', SYSDATE(), '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '1', '0', 'C', '0', '0', 'system:notice:list', 'message', 'admin', SYSDATE(), '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '1', '0', 'M', '0', '0', '', 'log', 'admin', SYSDATE(), '', NULL, '日志管理菜单');

-- 业务管理子菜单
INSERT INTO `sys_menu` VALUES (200, '科室管理', 2, 1, 'section', 'system/section/index', '', '1', '0', 'C', '0', '0', 'system:section:list', 'education', 'admin', SYSDATE(), '', NULL, '科室管理菜单');
INSERT INTO `sys_menu` VALUES (201, '门诊管理', 2, 2, 'outpatien', 'system/outpatien/index', '', '1', '0', 'C', '0', '0', 'system:outpatien:list', 'build', 'admin', SYSDATE(), '', NULL, '门诊管理菜单');
INSERT INTO `sys_menu` VALUES (202, '医生排班', 2, 3, 'scheduling', 'system/scheduling/index', '', '1', '0', 'C', '0', '0', 'system:scheduling:list', 'date', 'admin', SYSDATE(), '', NULL, '医生排班菜单');
INSERT INTO `sys_menu` VALUES (203, '挂号管理', 2, 4, 'registration', 'system/registration/index', '', '1', '0', 'C', '0', '0', 'system:registration:list', 'form', 'admin', SYSDATE(), '', NULL, '挂号管理菜单');
INSERT INTO `sys_menu` VALUES (204, '充值记录', 2, 5, 'pay', 'system/pay/index', '', '1', '0', 'C', '0', '0', 'system:pay:list', 'money', 'admin', SYSDATE(), '', NULL, '充值记录菜单');
INSERT INTO `sys_menu` VALUES (205, '收藏管理', 2, 6, 'collect', 'system/collect/index', '', '1', '0', 'C', '0', '0', 'system:collect:list', 'star', 'admin', SYSDATE(), '', NULL, '收藏管理菜单');
INSERT INTO `sys_menu` VALUES (206, '评价管理', 2, 7, 'evaluate', 'system/evaluate/index', '', '1', '0', 'C', '0', '0', 'system:evaluate:list', 'skill', 'admin', SYSDATE(), '', NULL, '评价管理菜单');

-- ----------------------------
-- 初始化-字典类型表数据
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', SYSDATE(), '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', SYSDATE(), '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', SYSDATE(), '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '通知类型', 'sys_notice_type', '0', 'admin', SYSDATE(), '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (5, '通知状态', 'sys_notice_status', '0', 'admin', SYSDATE(), '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (6, '挂号状态', 'registration_status', '0', 'admin', SYSDATE(), '', NULL, '挂号状态列表');
INSERT INTO `sys_dict_type` VALUES (7, '支付状态', 'pay_status', '0', 'admin', SYSDATE(), '', NULL, '支付状态列表');
INSERT INTO `sys_dict_type` VALUES (8, '支付类型', 'pay_type', '0', 'admin', SYSDATE(), '', NULL, '支付类型列表');
INSERT INTO `sys_dict_type` VALUES (9, '时间段', 'time_slot', '0', 'admin', SYSDATE(), '', NULL, '时间段列表');

-- ----------------------------
-- 初始化-字典数据表数据
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', SYSDATE(), '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', SYSDATE(), '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', SYSDATE(), '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', SYSDATE(), '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (9, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (10, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (11, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (12, 1, '待就诊', '0', 'registration_status', '', 'info', 'Y', '0', 'admin', SYSDATE(), '', NULL, '待就诊');
INSERT INTO `sys_dict_data` VALUES (13, 2, '已就诊', '1', 'registration_status', '', 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, '已就诊');
INSERT INTO `sys_dict_data` VALUES (14, 3, '已取消', '2', 'registration_status', '', 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '已取消');
INSERT INTO `sys_dict_data` VALUES (15, 4, '已过期', '3', 'registration_status', '', 'warning', 'N', '0', 'admin', SYSDATE(), '', NULL, '已过期');
INSERT INTO `sys_dict_data` VALUES (16, 1, '未支付', '0', 'pay_status', '', 'info', 'Y', '0', 'admin', SYSDATE(), '', NULL, '未支付');
INSERT INTO `sys_dict_data` VALUES (17, 2, '已支付', '1', 'pay_status', '', 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, '已支付');
INSERT INTO `sys_dict_data` VALUES (18, 3, '已退款', '2', 'pay_status', '', 'danger', 'N', '0', 'admin', SYSDATE(), '', NULL, '已退款');
INSERT INTO `sys_dict_data` VALUES (19, 1, '微信支付', '1', 'pay_type', '', 'success', 'Y', '0', 'admin', SYSDATE(), '', NULL, '微信支付');
INSERT INTO `sys_dict_data` VALUES (20, 2, '支付宝', '2', 'pay_type', '', 'primary', 'N', '0', 'admin', SYSDATE(), '', NULL, '支付宝');
INSERT INTO `sys_dict_data` VALUES (21, 3, '现金', '3', 'pay_type', '', 'warning', 'N', '0', 'admin', SYSDATE(), '', NULL, '现金');
INSERT INTO `sys_dict_data` VALUES (22, 1, '上午', '1', 'time_slot', '', 'primary', 'Y', '0', 'admin', SYSDATE(), '', NULL, '上午');
INSERT INTO `sys_dict_data` VALUES (23, 2, '下午', '2', 'time_slot', '', 'success', 'N', '0', 'admin', SYSDATE(), '', NULL, '下午');
INSERT INTO `sys_dict_data` VALUES (24, 3, '晚上', '3', 'time_slot', '', 'warning', 'N', '0', 'admin', SYSDATE(), '', NULL, '晚上');

SET FOREIGN_KEY_CHECKS = 1;



