package com.teacher.config;

import com.teacher.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置 - 跨域、拦截器、静态资源
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final FileConfig fileConfig;

    public WebConfig(JwtInterceptor jwtInterceptor, FileConfig fileConfig) {
        this.jwtInterceptor = jwtInterceptor;
        this.fileConfig = fileConfig;
    }

    /**
     * 配置跨域 - 允许微信小程序访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 配置 JWT 拦截器 - 排除登录、注册接口
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register");
    }

    /**
     * 静态资源 - 上传文件访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = new java.io.File(fileConfig.getUploadPath()).getAbsolutePath();
        if (!path.endsWith("/")) path += "/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
    }
}
