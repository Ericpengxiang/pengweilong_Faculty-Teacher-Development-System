package com.teacher.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    /** JWT Token */
    private String token;
    /** 用户ID */
    private Long userId;
    /** 用户名 */
    private String username;
    /** 姓名 */
    private String name;
    /** 角色 */
    private String role;
}
