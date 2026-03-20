package com.teacher.controller;

import com.teacher.common.Result;
import com.teacher.service.ArchiveService;
import com.teacher.vo.StatisticsVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计接口 - 数据统计与图表
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final ArchiveService archiveService;

    public StatisticsController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    /**
     * 获取统计数据（年度趋势、类型分布、教师排名）
     * GET /api/statistics
     */
    @GetMapping
    public Result<StatisticsVO> statistics(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        return archiveService.statistics(userId, role);
    }
}
