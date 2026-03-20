package com.teacher.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teacher.common.Result;
import com.teacher.dto.ArchiveDTO;
import com.teacher.entity.ArchiveAttachment;
import com.teacher.entity.ArchiveType;
import com.teacher.entity.TeacherArchive;
import com.teacher.entity.User;
import com.teacher.mapper.ArchiveAttachmentMapper;
import com.teacher.mapper.ArchiveTypeMapper;
import com.teacher.mapper.TeacherArchiveMapper;
import com.teacher.mapper.UserMapper;
import com.teacher.vo.ArchiveVO;
import com.teacher.vo.StatisticsVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 档案服务 - 档案 CRUD、统计
 */
@Service
public class ArchiveService {

    private final TeacherArchiveMapper archiveMapper;
    private final UserMapper userMapper;
    private final ArchiveTypeMapper typeMapper;
    private final ArchiveAttachmentMapper attachmentMapper;

    public ArchiveService(TeacherArchiveMapper archiveMapper, UserMapper userMapper,
                          ArchiveTypeMapper typeMapper, ArchiveAttachmentMapper attachmentMapper) {
        this.archiveMapper = archiveMapper;
        this.userMapper = userMapper;
        this.typeMapper = typeMapper;
        this.attachmentMapper = attachmentMapper;
    }

    /**
     * 根据类型名称获取 typeId
     */
    private Long getTypeIdByTypeName(String typeName) {
        ArchiveType t = typeMapper.selectOne(new LambdaQueryWrapper<ArchiveType>().eq(ArchiveType::getTypeName, typeName));
        return t != null ? t.getTypeId() : 1L;
    }

    /**
     * 新增档案
     */
    public Result<Long> add(Long teacherId, ArchiveDTO dto) {
        Long typeId = getTypeIdByTypeName(dto.getType());
        TeacherArchive archive = new TeacherArchive();
        archive.setTeacherId(teacherId);
        archive.setTypeId(typeId);
        archive.setTitle(dto.getTitle());
        archive.setDescription(dto.getDescription());
        archive.setYear(dto.getYear());
        archive.setStatus(0);
        archiveMapper.insert(archive);
        // 若有附件URL，写入 archive_attachment
        if (dto.getFileUrl() != null && !dto.getFileUrl().isEmpty()) {
            ArchiveAttachment att = new ArchiveAttachment();
            att.setArchiveId(archive.getArchiveId());
            att.setFileUrl(dto.getFileUrl());
            att.setFileSize(0L);
            attachmentMapper.insert(att);
        }
        return Result.success(archive.getArchiveId());
    }

    /**
     * 修改档案
     */
    public Result<Void> update(Long userId, String role, ArchiveDTO dto) {
        TeacherArchive archive = archiveMapper.selectById(dto.getArchiveId());
        if (archive == null) return Result.error(404, "档案不存在");
        if ("teacher".equals(role) && !archive.getTeacherId().equals(userId)) {
            return Result.error(403, "无权限修改");
        }
        Long typeId = getTypeIdByTypeName(dto.getType());
        archive.setTypeId(typeId);
        archive.setTitle(dto.getTitle());
        archive.setDescription(dto.getDescription());
        archive.setYear(dto.getYear());
        archiveMapper.updateById(archive);
        return Result.success(null);
    }

    /**
     * 删除档案
     */
    public Result<Void> delete(Long userId, String role, Long archiveId) {
        TeacherArchive archive = archiveMapper.selectById(archiveId);
        if (archive == null) return Result.error(404, "档案不存在");
        if ("teacher".equals(role) && !archive.getTeacherId().equals(userId)) {
            return Result.error(403, "无权限删除");
        }
        archiveMapper.deleteById(archiveId);
        return Result.success(null);
    }

    /**
     * 档案列表（支持按年份、类型筛选，type 传类型名称如"科研成果"）
     */
    public Result<Page<ArchiveVO>> list(Long userId, String role, Integer page, Integer size, Integer year, String type) {
        Page<TeacherArchive> p = new Page<>(page != null ? page : 1, size != null ? size : 10);
        LambdaQueryWrapper<TeacherArchive> wrapper = new LambdaQueryWrapper<>();
        if ("teacher".equals(role)) wrapper.eq(TeacherArchive::getTeacherId, userId);
        if (year != null) wrapper.eq(TeacherArchive::getYear, year);
        if (type != null && !type.isEmpty()) {
            ArchiveType at = typeMapper.selectOne(new LambdaQueryWrapper<ArchiveType>().eq(ArchiveType::getTypeName, type));
            if (at != null) wrapper.eq(TeacherArchive::getTypeId, at.getTypeId());
        }
        wrapper.orderByDesc(TeacherArchive::getCreateTime);

        Page<TeacherArchive> archivePage = archiveMapper.selectPage(p, wrapper);
        Page<ArchiveVO> voPage = new Page<>(archivePage.getCurrent(), archivePage.getSize(), archivePage.getTotal());
        List<ArchiveVO> voList = archivePage.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    /**
     * 档案详情
     */
    public Result<ArchiveVO> getDetail(Long archiveId, Long userId, String role) {
        TeacherArchive archive = archiveMapper.selectById(archiveId);
        if (archive == null) return Result.error(404, "档案不存在");
        if ("teacher".equals(role) && !archive.getTeacherId().equals(userId)) {
            return Result.error(403, "无权限查看");
        }
        return Result.success(toVO(archive));
    }

    /**
     * 档案审核（管理员）
     */
    public Result<Void> approve(Long archiveId, Integer status) {
        TeacherArchive archive = archiveMapper.selectById(archiveId);
        if (archive == null) return Result.error(404, "档案不存在");
        archive.setStatus(status);
        archiveMapper.updateById(archive);
        return Result.success(null);
    }

    /**
     * 统计数据
     */
    public Result<StatisticsVO> statistics(Long userId, String role) {
        StatisticsVO vo = new StatisticsVO();
        Long teacherId = "teacher".equals(role) ? userId : null;
        vo.setYearTrend(archiveMapper.countByYear(teacherId));
        vo.setTypeDistribution(archiveMapper.countByType(teacherId));
        vo.setTeacherRank(archiveMapper.rankByArchiveCount());
        return Result.success(vo);
    }

    private ArchiveVO toVO(TeacherArchive a) {
        ArchiveVO vo = new ArchiveVO();
        vo.setArchiveId(a.getArchiveId());
        vo.setTeacherId(a.getTeacherId());
        User u = userMapper.selectById(a.getTeacherId());
        vo.setTeacherName(u != null ? u.getName() : "");
        ArchiveType at = typeMapper.selectById(a.getTypeId());
        vo.setType(at != null ? at.getTypeName() : "");
        vo.setTitle(a.getTitle());
        vo.setDescription(a.getDescription());
        vo.setYear(a.getYear());
        vo.setStatus(a.getStatus());
        vo.setCreateTime(a.getCreateTime());
        // 取第一个附件作为主附件URL
        ArchiveAttachment att = attachmentMapper.selectOne(
                new LambdaQueryWrapper<ArchiveAttachment>().eq(ArchiveAttachment::getArchiveId, a.getArchiveId()).last("LIMIT 1"));
        vo.setFileUrl(att != null ? att.getFileUrl() : null);
        return vo;
    }
}
