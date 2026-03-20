package com.teacher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置类 - 存储 JWT 密钥和过期时间
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /** JWT 密钥 */
    private String secret;
    /** 过期时间（毫秒） */
    private long expiration;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
