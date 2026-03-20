package com.teacher.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teacher.common.Result;
import com.teacher.dto.LoginDTO;
import com.teacher.dto.RegisterDTO;
import com.teacher.dto.UserUpdateDTO;
import com.teacher.entity.OperationLog;
import com.teacher.entity.User;
import com.teacher.mapper.OperationLogMapper;
import com.teacher.mapper.UserMapper;
import com.teacher.vo.LoginVO;
import com.teacher.vo.UserVO;
import com.teacher.config.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务 - 登录、注册、个人信息管理
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final OperationLogMapper logMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserMapper userMapper, OperationLogMapper logMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.logMapper = logMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户登录
     */
    public Result<LoginVO> login(LoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            return Result.error(400, "用户名或密码错误");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.error(400, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());
        LoginVO vo = new LoginVO(token, user.getUserId(), user.getUsername(), user.getName(), user.getRole());

        // 记录登录日志
        OperationLog log = new OperationLog();
        log.setUserId(user.getUserId());
        log.setOperation("用户登录");
        logMapper.insert(log);

        return Result.success(vo);
    }

    /**
     * 用户注册
     */
    public Result<LoginVO> register(RegisterDTO dto) {
        if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())) != null) {
            return Result.error(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole() != null ? dto.getRole() : "teacher");
        userMapper.insert(user);

        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());
        LoginVO vo = new LoginVO(token, user.getUserId(), user.getUsername(), user.getName(), user.getRole());
        return Result.success("注册成功", vo);
    }

    /**
     * 获取当前用户信息
     */
    public Result<UserVO> getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        UserVO vo = new UserVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setName(user.getName());
        vo.setGender(user.getGender());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setRole(user.getRole());
        return Result.success(vo);
    }

    /**
     * 更新用户信息
     */
    public Result<Void> updateUser(Long userId, UserUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        userMapper.updateById(user);

        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setOperation("修改个人信息");
        logMapper.insert(log);

        return Result.success(null);
    }
}
