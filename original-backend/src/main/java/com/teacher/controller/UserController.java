package com.teacher.controller;

import com.teacher.common.Result;
import com.teacher.dto.UserUpdateDTO;
import com.teacher.service.UserService;
import com.teacher.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口 - 个人信息管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前用户信息
     * GET /api/user/info
     */
    @GetMapping("/info")
    public Result<UserVO> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getCurrentUser(userId);
    }

    /**
     * 修改个人信息
     * PUT /api/user/update
     */
    @PutMapping("/update")
    public Result<Void> update(HttpServletRequest request, @RequestBody UserUpdateDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.updateUser(userId, dto);
    }
}
