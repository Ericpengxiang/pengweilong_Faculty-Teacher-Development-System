package com.teacher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 注册请求 DTO
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private Integer gender;
    private String phone;
    private String email;

    /** 角色，默认为教师 */
    private String role = "teacher";
}
