package com.teacher.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@TableName("operation_log")
public class OperationLog {

    /** 日志ID */
    @TableId(type = IdType.AUTO)
    private Long logId;
    /** 操作用户ID */
    private Long userId;
    /** 操作内容 */
    private String operation;
    /** 操作模块 */
    private String module;
    /** 操作时间 */
    private LocalDateTime operationTime;
}
