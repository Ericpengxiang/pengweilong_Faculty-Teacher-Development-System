package com.teacher.service;

import com.teacher.common.Result;
import com.teacher.config.FileConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * 文件上传服务
 */
@Service
public class FileService {

    private final FileConfig fileConfig;

    public FileService(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    /**
     * 上传附件
     */
    public Result<String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error(400, "文件不能为空");
        }
        try {
            String dir = fileConfig.getUploadPath();
            File uploadDir = new File(dir);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String ext = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + (ext != null ? "." + ext : "");
            Path path = Path.of(uploadDir.getAbsolutePath(), filename);
            Files.copy(file.getInputStream(), path);
            // 返回访问路径
            return Result.success("/uploads/" + filename);
        } catch (IOException e) {
            return Result.error(500, "上传失败：" + e.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return null;
        int i = filename.lastIndexOf('.');
        return i > 0 ? filename.substring(i + 1) : null;
    }
}
