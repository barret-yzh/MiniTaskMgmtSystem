package com.mgmtsystem.controller;

import com.mgmtsystem.dto.ApiResponse;
import com.mgmtsystem.dto.ProjectRequest;
import com.mgmtsystem.entity.Project;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.service.ProjectService;
import com.mgmtsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Project>> createProject(@RequestBody ProjectRequest projectRequest,
                                                            @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Project project = projectService.createProject(projectRequest, user);
            return ResponseEntity.ok(ApiResponse.success("项目创建成功", project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> getProjects(@RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            List<Project> projects = projectService.getProjectsByOwner(user);
            return ResponseEntity.ok(ApiResponse.success("获取项目列表成功", projects));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> getProject(@PathVariable Long id, @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Project project = projectService.getProjectById(id, user);
            return ResponseEntity.ok(ApiResponse.success("获取项目详情成功", project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> updateProject(@PathVariable Long id, 
                                                            @RequestBody ProjectRequest projectRequest,
                                                            @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Project project = projectService.updateProject(id, projectRequest, user);
            return ResponseEntity.ok(ApiResponse.success("项目更新成功", project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id, @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            projectService.deleteProject(id, user);
            return ResponseEntity.ok(ApiResponse.success("项目删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
} 