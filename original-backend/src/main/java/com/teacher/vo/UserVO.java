package com.teacher.vo;

import lombok.Data;

/**
 * 用户信息 VO（不包含密码）
 */
@Data
public class UserVO {

    private Long userId;
    private String username;
    private String name;
    private Integer gender;
    private String phone;
    private String email;
    private String role;
}
