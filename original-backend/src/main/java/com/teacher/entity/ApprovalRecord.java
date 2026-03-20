package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审核记录实体类
 */
@Data
@TableName("approval_record")
public class ApprovalRecord {

    @TableId(type = IdType.AUTO)
    private Long recordId;
    private Long archiveId;
    private Long approverId;
    private Integer status;
    private String comment;
    private LocalDateTime approvalTime;
}
