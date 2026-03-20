package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门/院系实体类
 */
@Data
@TableName("department")
public class Department {

    @TableId(type = IdType.AUTO)
    private Long deptId;
    private String deptName;
    private Long parentId;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
