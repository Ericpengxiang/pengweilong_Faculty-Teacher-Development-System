package com.teacher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 档案新增/修改 DTO
 */
@Data
public class ArchiveDTO {

    private Long archiveId;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "类型不能为空")
    private String type;

    private String description;
    private String fileUrl;

    @NotNull(message = "年份不能为空")
    private Integer year;
}
