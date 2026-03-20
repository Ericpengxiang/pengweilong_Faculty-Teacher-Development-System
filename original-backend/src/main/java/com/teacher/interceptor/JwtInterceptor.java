package com.teacher.interceptor;

import com.teacher.config.JwtUtil;
import com.teacher.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * JWT 认证拦截器 - 验证请求中的 Token
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public JwtInterceptor(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS 请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || token.isEmpty()) {
            writeError(response, 401, "未登录或Token已过期");
            return false;
        }

        if (!jwtUtil.validateToken(token)) {
            writeError(response, 401, "Token无效或已过期");
            return false;
        }

        var claims = jwtUtil.parseToken(token);
        request.setAttribute("userId", Long.parseLong(claims.getSubject()));
        request.setAttribute("role", claims.get("role", String.class));
        return true;
    }

    private void writeError(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<?> result = Result.error(code, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
