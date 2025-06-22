package com.mgmtsystem.service;

import com.mgmtsystem.dto.ProjectRequest;
import com.mgmtsystem.entity.Project;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.mapper.ProjectMapper;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private User user;
    private Project project;
    private ProjectRequest projectRequest;

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

        projectRequest = new ProjectRequest();
        projectRequest.setName("测试项目");
        projectRequest.setDescription("这是一个测试项目");
    }

    @Test
    void testCreateProject_Success() {
        // Given
        when(projectMapper.insert(any(Project.class))).thenReturn(1);

        // When
        Project result = projectService.createProject(projectRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("测试项目", result.getName());
        assertEquals("这是一个测试项目", result.getDescription());
        assertEquals(1L, result.getOwnerId());
        verify(projectMapper).insert(any(Project.class));
    }

    @Test
    void testGetProjectsByOwner_Success() {
        // Given
        List<Project> projects = Arrays.asList(project);
        when(projectMapper.findByOwnerId(1L)).thenReturn(projects);

        // When
        List<Project> result = projectService.getProjectsByOwner(user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试项目", result.get(0).getName());
        verify(projectMapper).findByOwnerId(1L);
    }

    @Test
    void testGetProjectById_Success() {
        // Given
        when(projectMapper.findByIdAndOwnerId(1L, 1L)).thenReturn(project);

        // When
        Project result = projectService.getProjectById(1L, user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试项目", result.getName());
        verify(projectMapper).findByIdAndOwnerId(1L, 1L);
    }

    @Test
    void testGetProjectById_ProjectNotFound() {
        // Given
        when(projectMapper.findByIdAndOwnerId(1L, 1L)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> projectService.getProjectById(1L, user)
        );
        assertEquals("项目不存在或无权限访问", exception.getMessage());
    }

    @Test
    void testUpdateProject_Success() {
        // Given
        when(projectMapper.findByIdAndOwnerId(1L, 1L)).thenReturn(project);
        when(projectMapper.updateById(any(Project.class))).thenReturn(1);

        // When
        Project result = projectService.updateProject(1L, projectRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("测试项目", result.getName());
        assertEquals("这是一个测试项目", result.getDescription());
        verify(projectMapper).updateById(any(Project.class));
    }

    @Test
    void testUpdateProject_ProjectNotFound() {
        // Given
        when(projectMapper.findByIdAndOwnerId(1L, 1L)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> projectService.updateProject(1L, projectRequest, user)
        );
        assertEquals("项目不存在或无权限访问", exception.getMessage());
    }

    @Test
    void testDeleteProject_Success() {
        // Given
        when(projectMapper.findByIdAndOwnerId(1L, 1L)).thenReturn(project);
        when(projectMapper.deleteById(1L)).thenReturn(1);

        // When
        assertDoesNotThrow(() -> projectService.deleteProject(1L, user));

        // Then
        verify(projectMapper).deleteById(1L);
    }

    @Test
    void testDeleteProject_ProjectNotFound() {
        // Given
        when(projectMapper.findByIdAndOwnerId(1L, 1L)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> projectService.deleteProject(1L, user)
        );
        assertEquals("项目不存在或无权限访问", exception.getMessage());
    }
} 