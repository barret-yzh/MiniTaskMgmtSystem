package com.mgmtsystem.enums;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testTaskStatusValues() {
        // Test all enum values exist
        assertEquals(3, TaskStatus.values().length);
        
        assertEquals("待办", TaskStatus.TODO.getStatus());
        assertEquals("进行中", TaskStatus.IN_PROGRESS.getStatus());
        assertEquals("已完成", TaskStatus.COMPLETED.getStatus());
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        // Test JSON serialization returns display name
        String todoJson = objectMapper.writeValueAsString(TaskStatus.TODO);
        assertEquals("\"待办\"", todoJson);
        
        String inProgressJson = objectMapper.writeValueAsString(TaskStatus.IN_PROGRESS);
        assertEquals("\"进行中\"", inProgressJson);
        
        String completedJson = objectMapper.writeValueAsString(TaskStatus.COMPLETED);
        assertEquals("\"已完成\"", completedJson);
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        // Test JSON deserialization from enum name
        TaskStatus todo = objectMapper.readValue("\"TODO\"", TaskStatus.class);
        assertEquals(TaskStatus.TODO, todo);
        
        TaskStatus inProgress = objectMapper.readValue("\"IN_PROGRESS\"", TaskStatus.class);
        assertEquals(TaskStatus.IN_PROGRESS, inProgress);
        
        TaskStatus completed = objectMapper.readValue("\"COMPLETED\"", TaskStatus.class);
        assertEquals(TaskStatus.COMPLETED, completed);
    }

    @Test
    void testFromName() {
        // Test creating enum from string name
        assertEquals(TaskStatus.TODO, TaskStatus.fromString("todo"));
        assertEquals(TaskStatus.TODO, TaskStatus.fromString("TODO"));
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromString("in_progress"));
        assertEquals(TaskStatus.COMPLETED, TaskStatus.fromString("completed"));
    }

    @Test
    void testFromNameInvalid() {
        // Test invalid enum name throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            TaskStatus.fromString("INVALID");
        });
    }
} 