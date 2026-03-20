package com.teacher.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 统计数据 VO
 */
@Data
public class StatisticsVO {

    /** 年度趋势（折线图） */
    private List<Map<String, Object>> yearTrend;
    /** 类型分布（饼图） */
    private List<Map<String, Object>> typeDistribution;
    /** 教师排名（柱状图） */
    private List<Map<String, Object>> teacherRank;
}
