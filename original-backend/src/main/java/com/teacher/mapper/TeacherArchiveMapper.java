package com.teacher.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teacher.entity.TeacherArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 教师档案 Mapper 接口
 */
@Mapper
public interface TeacherArchiveMapper extends BaseMapper<TeacherArchive> {

    /**
     * 按年份统计成果数量
     */
    List<Map<String, Object>> countByYear(@Param("teacherId") Long teacherId);

    /**
     * 按类型统计成果数量
     */
    List<Map<String, Object>> countByType(@Param("teacherId") Long teacherId);

    /**
     * 教师成果排名
     */
    List<Map<String, Object>> rankByArchiveCount();
}
