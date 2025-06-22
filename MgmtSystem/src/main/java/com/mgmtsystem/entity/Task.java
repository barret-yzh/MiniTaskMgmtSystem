package com.mgmtsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.mgmtsystem.enums.TaskPriority;
import com.mgmtsystem.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("task")
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "title")
    @NotBlank(message = "任务标题不能为空")
    private String title;

    @TableField(value = "description")
    @NotBlank(message = "任务描述不能为空")
    private String description;

    @TableField(value = "status")
    private TaskStatus status = TaskStatus.TODO;

    @TableField(value = "priority")
    private TaskPriority priority = TaskPriority.MEDIUM;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "project_id")
    private Long projectId;

    @TableLogic
    private Integer deleted;
} 