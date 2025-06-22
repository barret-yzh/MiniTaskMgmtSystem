package com.mgmtsystem.dto;

import com.mgmtsystem.enums.TaskPriority;
import com.mgmtsystem.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
}