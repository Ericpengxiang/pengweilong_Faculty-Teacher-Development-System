package com.teacher.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 档案信息 VO
 */
@Data
public class ArchiveVO {

    private Long archiveId;
    private Long teacherId;
    private String teacherName;
    private String title;
    private String type;
    private String description;
    private String fileUrl;
    private Integer year;
    private Integer status;
    private LocalDateTime createTime;
}
