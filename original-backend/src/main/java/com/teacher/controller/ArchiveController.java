package com.teacher.controller;

import com.teacher.common.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacher.dto.ArchiveDTO;
import com.teacher.service.ArchiveService;
import com.teacher.vo.ArchiveVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 档案接口 - 档案 CRUD
 */
@RestController
@RequestMapping("/archive")
public class ArchiveController {

    private final ArchiveService archiveService;

    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    private String getRole(HttpServletRequest request) {
        return (String) request.getAttribute("role");
    }

    /**
     * 新增档案
     * POST /api/archive/add
     */
    @PostMapping("/add")
    public Result<Long> add(HttpServletRequest request, @Valid @RequestBody ArchiveDTO dto) {
        return archiveService.add(getUserId(request), dto);
    }

    /**
     * 档案列表（分页）
     * GET /api/archive/list?page=1&size=10&year=2023&type=科研成果
     */
    @GetMapping("/list")
    public Result<Page<ArchiveVO>> list(HttpServletRequest request,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) Integer year,
                                        @RequestParam(required = false) String type) {
        return archiveService.list(getUserId(request), getRole(request), page, size, year, type);
    }

    /**
     * 档案详情
     * GET /api/archive/detail/{id}
     */
    @GetMapping("/detail/{id}")
    public Result<ArchiveVO> detail(HttpServletRequest request, @PathVariable Long id) {
        return archiveService.getDetail(id, getUserId(request), getRole(request));
    }

    /**
     * 修改档案
     * PUT /api/archive/update
     */
    @PutMapping("/update")
    public Result<Void> update(HttpServletRequest request, @Valid @RequestBody ArchiveDTO dto) {
        return archiveService.update(getUserId(request), getRole(request), dto);
    }

    /**
     * 删除档案
     * DELETE /api/archive/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(HttpServletRequest request, @PathVariable Long id) {
        return archiveService.delete(getUserId(request), getRole(request), id);
    }

    /**
     * 档案审核（管理员）
     * PUT /api/archive/approve/{id}?status=1
     */
    @PutMapping("/approve/{id}")
    public Result<Void> approve(HttpServletRequest request, @PathVariable Long id, @RequestParam Integer status) {
        if (!"admin".equals(getRole(request))) {
            return Result.error(403, "无权限");
        }
        return archiveService.approve(id, status);
    }
}
