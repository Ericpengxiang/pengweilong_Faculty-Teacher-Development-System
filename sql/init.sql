-- ============================================
-- 高校教师成长档案管理系统 - 数据库初始化脚本
-- MySQL 8.0 - 共10张表
-- ============================================

CREATE DATABASE IF NOT EXISTS teacher_archive DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE teacher_archive;

-- 按外键依赖顺序删除表
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `notification`;
DROP TABLE IF EXISTS `operation_log`;
DROP TABLE IF EXISTS `approval_record`;
DROP TABLE IF EXISTS `archive_attachment`;
DROP TABLE IF EXISTS `teacher_archive`;
DROP TABLE IF EXISTS `teacher_info`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `archive_type`;
DROP TABLE IF EXISTS `department`;
DROP TABLE IF EXISTS `system_config`;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 1. 部门表 (department)
-- ============================================
CREATE TABLE `department` (
    `dept_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    `dept_name` VARCHAR(100) NOT NULL COMMENT '部门名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '上级部门ID，0为顶级',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`dept_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门/院系表';

-- ============================================
-- 2. 用户表 (user)
-- ============================================
CREATE TABLE `user` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知 1-男 2-女',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `role` VARCHAR(20) NOT NULL DEFAULT 'teacher' COMMENT '角色：teacher-教师 admin-管理员',
    `dept_id` BIGINT DEFAULT NULL COMMENT '所属部门ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `department` (`dept_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 3. 教师信息扩展表 (teacher_info)
-- ============================================
CREATE TABLE `teacher_info` (
    `info_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '信息ID',
    `user_id` BIGINT NOT NULL COMMENT '教师用户ID',
    `employee_no` VARCHAR(50) DEFAULT NULL COMMENT '工号',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `degree` VARCHAR(20) DEFAULT NULL COMMENT '学历：本科/硕士/博士',
    `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
    `research_direction` VARCHAR(200) DEFAULT NULL COMMENT '研究方向',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`info_id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_employee_no` (`employee_no`),
    CONSTRAINT `fk_teacher_info_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师信息扩展表';

-- ============================================
-- 4. 档案类型字典表 (archive_type)
-- ============================================
CREATE TABLE `archive_type` (
    `type_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '类型ID',
    `type_code` VARCHAR(50) NOT NULL COMMENT '类型编码',
    `type_name` VARCHAR(100) NOT NULL COMMENT '类型名称',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`type_id`),
    UNIQUE KEY `uk_type_code` (`type_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='档案类型字典表';

-- ============================================
-- 5. 教师成长档案表 (teacher_archive)
-- ============================================
CREATE TABLE `teacher_archive` (
    `archive_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '档案ID',
    `teacher_id` BIGINT NOT NULL COMMENT '教师ID（关联user表）',
    `type_id` BIGINT NOT NULL COMMENT '档案类型ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `year` INT NOT NULL COMMENT '年份',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核 1-已通过 2-已拒绝',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`archive_id`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_type_id` (`type_id`),
    KEY `idx_year` (`year`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_archive_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_archive_type` FOREIGN KEY (`type_id`) REFERENCES `archive_type` (`type_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师成长档案表';

-- ============================================
-- 6. 档案附件表 (archive_attachment)
-- ============================================
CREATE TABLE `archive_attachment` (
    `attachment_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '附件ID',
    `archive_id` BIGINT NOT NULL COMMENT '档案ID',
    `file_name` VARCHAR(200) DEFAULT NULL COMMENT '原始文件名',
    `file_url` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    `file_size` BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`attachment_id`),
    KEY `idx_archive_id` (`archive_id`),
    CONSTRAINT `fk_attachment_archive` FOREIGN KEY (`archive_id`) REFERENCES `teacher_archive` (`archive_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='档案附件表';

-- ============================================
-- 7. 审核记录表 (approval_record)
-- ============================================
CREATE TABLE `approval_record` (
    `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `archive_id` BIGINT NOT NULL COMMENT '档案ID',
    `approver_id` BIGINT NOT NULL COMMENT '审核人ID',
    `status` TINYINT NOT NULL COMMENT '审核结果：1-通过 2-拒绝',
    `comment` VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    `approval_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
    PRIMARY KEY (`record_id`),
    KEY `idx_archive_id` (`archive_id`),
    KEY `idx_approver_id` (`approver_id`),
    CONSTRAINT `fk_approval_archive` FOREIGN KEY (`archive_id`) REFERENCES `teacher_archive` (`archive_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_approval_user` FOREIGN KEY (`approver_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审核记录表';

-- ============================================
-- 8. 操作日志表 (operation_log)
-- ============================================
CREATE TABLE `operation_log` (
    `log_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT NOT NULL COMMENT '操作用户ID',
    `operation` VARCHAR(200) NOT NULL COMMENT '操作内容',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
    `operation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`log_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation_time` (`operation_time`),
    CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统操作日志表';

-- ============================================
-- 9. 系统配置表 (system_config)
-- ============================================
CREATE TABLE `system_config` (
    `config_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT DEFAULT NULL COMMENT '配置值',
    `config_desc` VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`config_id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ============================================
-- 10. 通知消息表 (notification)
-- ============================================
CREATE TABLE `notification` (
    `notify_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT DEFAULT NULL COMMENT '通知内容',
    `type` VARCHAR(20) DEFAULT 'system' COMMENT '类型：system-系统 archive-档案 audit-审核',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读 1-已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`notify_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_notify_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

-- ============================================
-- 初始化字典数据
-- ============================================
INSERT INTO `department` (`dept_name`, `parent_id`, `sort_order`) VALUES
('计算机学院', 0, 1),
('数学学院', 0, 2),
('物理学院', 0, 3);

INSERT INTO `archive_type` (`type_code`, `type_name`, `sort_order`) VALUES
('research', '科研成果', 1),
('teaching', '教学成果', 2),
('award', '获奖情况', 3),
('training', '培训记录', 4),
('promotion', '职称晋升', 5);

INSERT INTO `system_config` (`config_key`, `config_value`, `config_desc`) VALUES
('system_name', '高校教师成长档案管理系统', '系统名称'),
('archive_auto_approve', '0', '档案是否自动审核通过：0-否 1-是');

-- 测试数据由应用启动时自动初始化（DataInit）
