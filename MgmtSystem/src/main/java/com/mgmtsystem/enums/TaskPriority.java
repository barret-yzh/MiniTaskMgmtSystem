package com.mgmtsystem.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskPriority {
    LOW("低"),
    MEDIUM("中"),
    HIGH("高");

    private final String priority;

    TaskPriority(String priority) {
        this.priority = priority;
    }

    @JsonValue //控制枚举序列化时输出什么值
    public String getPriority() {
        return priority;
    }

    @JsonCreator //控制枚举反序列化时如何解析输入值
    public static TaskPriority fromString(String value) {
        for (TaskPriority priority : TaskPriority.values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown TaskPriority: " + value);
    }
} 