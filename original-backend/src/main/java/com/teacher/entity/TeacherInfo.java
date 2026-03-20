package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教师信息扩展实体类
 */
@Data
@TableName("teacher_info")
public class TeacherInfo {

    @TableId(type = IdType.AUTO)
    private Long infoId;
    private Long userId;
    private String employeeNo;
    private String title;
    private String degree;
    private LocalDate entryDate;
    private String researchDirection;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
