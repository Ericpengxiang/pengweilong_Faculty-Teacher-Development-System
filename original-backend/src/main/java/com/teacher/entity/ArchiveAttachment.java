package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 档案附件实体类
 */
@Data
@TableName("archive_attachment")
public class ArchiveAttachment {

    @TableId(type = IdType.AUTO)
    private Long attachmentId;
    private Long archiveId;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
