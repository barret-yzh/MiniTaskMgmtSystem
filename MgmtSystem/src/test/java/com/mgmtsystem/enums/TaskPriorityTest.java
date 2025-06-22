package com.mgmtsystem.enums;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskPriorityTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testTaskPriorityValues() {
        // Test all enum values exist
        assertEquals(3, TaskPriority.values().length);
        
        assertEquals("低", TaskPriority.LOW.getPriority());
        assertEquals("中", TaskPriority.MEDIUM.getPriority());
        assertEquals("高", TaskPriority.HIGH.getPriority());
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        // Test JSON serialization returns display name
        String lowJson = objectMapper.writeValueAsString(TaskPriority.LOW);
        assertEquals("\"低\"", lowJson);
        
        String mediumJson = objectMapper.writeValueAsString(TaskPriority.MEDIUM);
        assertEquals("\"中\"", mediumJson);
        
        String highJson = objectMapper.writeValueAsString(TaskPriority.HIGH);
        assertEquals("\"高\"", highJson);
    }

    @Test
    void testJsonDeserialization() throws JsonProcessingException {
        // Test JSON deserialization from enum name
        TaskPriority low = objectMapper.readValue("\"LOW\"", TaskPriority.class);
        assertEquals(TaskPriority.LOW, low);
        
        TaskPriority medium = objectMapper.readValue("\"MEDIUM\"", TaskPriority.class);
        assertEquals(TaskPriority.MEDIUM, medium);
        
        TaskPriority high = objectMapper.readValue("\"HIGH\"", TaskPriority.class);
        assertEquals(TaskPriority.HIGH, high);
    }

    @Test
    void testFromName() {
        // Test creating enum from string name
        assertEquals(TaskPriority.LOW, TaskPriority.fromString("low"));
        assertEquals(TaskPriority.LOW, TaskPriority.fromString("LOW"));
        assertEquals(TaskPriority.MEDIUM, TaskPriority.fromString("medium"));
        assertEquals(TaskPriority.HIGH, TaskPriority.fromString("high"));
    }

    @Test
    void testFromNameInvalid() {
        // Test invalid enum name throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            TaskPriority.fromString("INVALID");
        });
    }
} 