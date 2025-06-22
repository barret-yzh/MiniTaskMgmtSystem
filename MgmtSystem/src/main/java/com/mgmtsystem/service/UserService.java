package com.mgmtsystem.service;

import com.mgmtsystem.dto.LoginRequest;
import com.mgmtsystem.dto.RegisterRequest;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User registerUser(RegisterRequest registerRequest) {
        if (userMapper.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (userMapper.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        
        userMapper.insert(user);
        return user;
    }
    
    public User loginUser(LoginRequest loginRequest) {
        User user = userMapper.findByUsername(loginRequest.getUsername());
        
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        return user;
    }
    
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
    
    public User findById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
} 