package com.mgmtsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("project")
public class Project {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    @NotBlank(message = "项目名称不能为空")
    private String name;

    @TableField(value = "description")
    @NotBlank(message = "项目描述不能为空")
    private String description;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "owner_id")
    private Long ownerId;

    @TableLogic
    private Integer deleted;
} 