package com.teacher.controller;

import com.teacher.common.Result;
import com.teacher.dto.LoginDTO;
import com.teacher.dto.RegisterDTO;
import com.teacher.service.UserService;
import com.teacher.vo.LoginVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口 - 登录、注册
 */
@RestController
@RequestMapping
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     * POST /api/login
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return userService.login(dto);
    }

    /**
     * 用户注册
     * POST /api/register
     */
    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
}
