package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 档案类型字典实体类
 */
@Data
@TableName("archive_type")
public class ArchiveType {

    @TableId(type = IdType.AUTO)
    private Long typeId;
    private String typeCode;
    private String typeName;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
