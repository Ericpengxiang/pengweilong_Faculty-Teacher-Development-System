package com.teacher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传配置
 */
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    /** 文件上传根路径 */
    private String uploadPath = "./uploads";

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
