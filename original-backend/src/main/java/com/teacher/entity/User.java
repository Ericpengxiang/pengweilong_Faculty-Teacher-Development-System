package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User {

    /** 用户ID */
    @TableId(type = IdType.AUTO)
    private Long userId;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 性别：0-未知 1-男 2-女 */
    private Integer gender;
    /** 手机号 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 角色：teacher-教师 admin-管理员 */
    private String role;
    /** 所属部门ID */
    private Long deptId;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}
