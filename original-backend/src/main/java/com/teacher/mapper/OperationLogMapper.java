package com.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teacher.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper 接口
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
