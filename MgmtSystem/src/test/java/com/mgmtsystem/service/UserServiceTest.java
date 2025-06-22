package com.mgmtsystem.service;

import com.mgmtsystem.dto.LoginRequest;
import com.mgmtsystem.dto.RegisterRequest;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    void testRegisterUser_Success() {
        // Given
        when(userMapper.existsByUsername("testuser")).thenReturn(false);
        when(userMapper.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // When
        User result = userService.registerUser(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("password123", result.getPassword());
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        // Given
        when(userMapper.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.registerUser(registerRequest)
        );
        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    void testRegisterUser_EmailExists() {
        // Given
        when(userMapper.existsByUsername("testuser")).thenReturn(false);
        when(userMapper.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.registerUser(registerRequest)
        );
        assertEquals("邮箱已存在", exception.getMessage());
    }

    @Test
    void testLoginUser_Success() {
        // Given
        when(userMapper.findByUsername("testuser")).thenReturn(user);

        // When
        User result = userService.loginUser(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testLoginUser_UserNotFound() {
        // Given
        when(userMapper.findByUsername("testuser")).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.loginUser(loginRequest)
        );
        assertEquals("用户名不存在", exception.getMessage());
    }

    @Test
    void testLoginUser_WrongPassword() {
        // Given
        User userWithDifferentPassword = new User();
        userWithDifferentPassword.setId(1L);
        userWithDifferentPassword.setUsername("testuser");
        userWithDifferentPassword.setEmail("test@example.com");
        userWithDifferentPassword.setPassword("wrongpassword");
        
        when(userMapper.findByUsername("testuser")).thenReturn(userWithDifferentPassword);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.loginUser(loginRequest)
        );
        assertEquals("密码错误", exception.getMessage());
    }

    @Test
    void testFindByUsername_Success() {
        // Given
        when(userMapper.findByUsername("testuser")).thenReturn(user);

        // When
        User result = userService.findByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        // Given
        when(userMapper.findByUsername("testuser")).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.findByUsername("testuser")
        );
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testFindById_Success() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(user);

        // When
        User result = userService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testFindById_UserNotFound() {
        // Given
        when(userMapper.selectById(1L)).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.findById(1L)
        );
        assertEquals("用户不存在", exception.getMessage());
    }
} 