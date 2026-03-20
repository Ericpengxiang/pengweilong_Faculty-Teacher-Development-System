package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教师成长档案实体类
 */
@Data
@TableName("teacher_archive")
public class TeacherArchive {

    @TableId(type = IdType.AUTO)
    private Long archiveId;
    private Long teacherId;
    /** 档案类型ID（关联 archive_type） */
    private Long typeId;
    private String title;
    private String description;
    private Integer year;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
