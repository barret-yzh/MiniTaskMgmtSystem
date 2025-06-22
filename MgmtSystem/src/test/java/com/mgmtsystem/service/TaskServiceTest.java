package com.mgmtsystem.service;

import com.mgmtsystem.dto.TaskRequest;
import com.mgmtsystem.entity.Project;
import com.mgmtsystem.entity.Task;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.enums.TaskPriority;
import com.mgmtsystem.enums.TaskStatus;
import com.mgmtsystem.mapper.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Project project;
    private Task task;
    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        project = new Project();
        project.setId(1L);
        project.setName("测试项目");
        project.setDescription("这是一个测试项目");
        project.setOwnerId(1L);

        task = new Task();
        task.setId(1L);
        task.setTitle("测试任务");
        task.setDescription("这是一个测试任务");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setProjectId(1L);

        taskRequest = new TaskRequest();
        taskRequest.setTitle("测试任务");
        taskRequest.setDescription("这是一个测试任务");
        taskRequest.setStatus(TaskStatus.TODO);
        taskRequest.setPriority(TaskPriority.MEDIUM);
    }

    @Test
    void testCreateTask_Success() {
        // Given
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.insert(any(Task.class))).thenReturn(1);

        // When
        Task result = taskService.createTask(taskRequest, 1L, user);

        // Then
        assertNotNull(result);
        assertEquals("测试任务", result.getTitle());
        assertEquals("这是一个测试任务", result.getDescription());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(TaskPriority.MEDIUM, result.getPriority());
        assertEquals(1L, result.getProjectId());
        verify(taskMapper).insert(any(Task.class));
    }

    @Test
    void testCreateTask_WithDefaultValues() {
        // Given
        TaskRequest requestWithoutStatusAndPriority = new TaskRequest();
        requestWithoutStatusAndPriority.setTitle("测试任务");
        requestWithoutStatusAndPriority.setDescription("这是一个测试任务");
        
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.insert(any(Task.class))).thenReturn(1);

        // When
        Task result = taskService.createTask(requestWithoutStatusAndPriority, 1L, user);

        // Then
        assertNotNull(result);
        assertEquals("测试任务", result.getTitle());
        assertEquals("这是一个测试任务", result.getDescription());
        verify(taskMapper).insert(any(Task.class));
    }

    @Test
    void testGetTasksWithFiltersAndSort_Success() {
        // Given
        List<Task> tasks = Arrays.asList(task);
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findTasksWithFiltersAndSort(1L, 1L, TaskStatus.TODO, TaskPriority.MEDIUM, "created_at", "desc"))
                .thenReturn(tasks);

        // When
        List<Task> result = taskService.getTasksWithFiltersAndSort(1L, TaskStatus.TODO, TaskPriority.MEDIUM, "created_at", "desc", user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试任务", result.get(0).getTitle());
        verify(taskMapper).findTasksWithFiltersAndSort(1L, 1L, TaskStatus.TODO, TaskPriority.MEDIUM, "created_at", "desc");
    }

    @Test
    void testGetTasksByProject_Success() {
        // Given
        List<Task> tasks = Arrays.asList(task);
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByProjectIdAndOwnerId(1L, 1L)).thenReturn(tasks);

        // When
        List<Task> result = taskService.getTasksByProject(1L, user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试任务", result.get(0).getTitle());
        verify(taskMapper).findByProjectIdAndOwnerId(1L, 1L);
    }

    @Test
    void testGetTaskById_Success() {
        // Given
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByIdAndProjectId(1L, 1L)).thenReturn(task);

        // When
        Task result = taskService.getTaskById(1L, 1L, user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试任务", result.getTitle());
        verify(taskMapper).findByIdAndProjectId(1L, 1L);
    }

    @Test
    void testGetTaskById_TaskNotFound() {
        // Given
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByIdAndProjectId(1L, 1L)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> taskService.getTaskById(1L, 1L, user)
        );
        assertEquals("任务不存在", exception.getMessage());
    }

    @Test
    void testUpdateTask_Success() {
        // Given
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByIdAndProjectId(1L, 1L)).thenReturn(task);
        when(taskMapper.updateById(any(Task.class))).thenReturn(1);

        // When
        Task result = taskService.updateTask(1L, 1L, taskRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("测试任务", result.getTitle());
        assertEquals("这是一个测试任务", result.getDescription());
        verify(taskMapper).updateById(any(Task.class));
    }

    @Test
    void testUpdateTaskStatus_Success() {
        // Given
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByIdAndProjectId(1L, 1L)).thenReturn(task);
        when(taskMapper.updateById(any(Task.class))).thenReturn(1);

        // When
        Task result = taskService.updateTaskStatus(1L, 1L, TaskStatus.IN_PROGRESS, user);

        // Then
        assertNotNull(result);
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        verify(taskMapper).updateById(any(Task.class));
    }

    @Test
    void testUpdateTaskPriority_Success() {
        // Given
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByIdAndProjectId(1L, 1L)).thenReturn(task);
        when(taskMapper.updateById(any(Task.class))).thenReturn(1);

        // When
        Task result = taskService.updateTaskPriority(1L, 1L, TaskPriority.HIGH, user);

        // Then
        assertNotNull(result);
        assertEquals(TaskPriority.HIGH, result.getPriority());
        verify(taskMapper).updateById(any(Task.class));
    }

    @Test
    void testDeleteTask_Success() {
        // Given
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByIdAndProjectId(1L, 1L)).thenReturn(task);
        when(taskMapper.deleteById(1L)).thenReturn(1);

        // When
        assertDoesNotThrow(() -> taskService.deleteTask(1L, 1L, user));

        // Then
        verify(taskMapper).deleteById(1L);
    }

    @Test
    void testGetTasksByProjectAndStatus_Success() {
        // Given
        List<Task> tasks = Arrays.asList(task);
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByProjectIdAndOwnerIdAndStatus(1L, 1L, TaskStatus.TODO)).thenReturn(tasks);

        // When
        List<Task> result = taskService.getTasksByProjectAndStatus(1L, TaskStatus.TODO, user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TaskStatus.TODO, result.get(0).getStatus());
        verify(taskMapper).findByProjectIdAndOwnerIdAndStatus(1L, 1L, TaskStatus.TODO);
    }

    @Test
    void testGetTasksByProjectAndPriority_Success() {
        // Given
        List<Task> tasks = Arrays.asList(task);
        when(projectService.getProjectById(1L, user)).thenReturn(project);
        when(taskMapper.findByProjectIdAndOwnerIdAndPriority(1L, 1L, TaskPriority.HIGH)).thenReturn(tasks);

        // When
        List<Task> result = taskService.getTasksByProjectAndPriority(1L, TaskPriority.HIGH, user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(taskMapper).findByProjectIdAndOwnerIdAndPriority(1L, 1L, TaskPriority.HIGH);
    }
} 