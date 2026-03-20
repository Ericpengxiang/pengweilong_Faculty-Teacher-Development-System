package com.teacher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 高校教师成长档案管理系统 - 启动类
 */
@SpringBootApplication
@MapperScan("com.teacher.mapper")
public class TeacherArchiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherArchiveApplication.class, args);
    }
}
