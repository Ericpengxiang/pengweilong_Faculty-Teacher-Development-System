package com.teacher.dto;

import lombok.Data;

/**
 * 用户信息更新 DTO
 */
@Data
public class UserUpdateDTO {

    private String name;
    private Integer gender;
    private String phone;
    private String email;
}
