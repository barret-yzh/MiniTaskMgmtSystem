package com.mgmtsystem.controller;

import com.mgmtsystem.dto.ApiResponse;
import com.mgmtsystem.dto.TaskRequest;
import com.mgmtsystem.entity.Task;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.enums.TaskPriority;
import com.mgmtsystem.enums.TaskStatus;
import com.mgmtsystem.service.TaskService;
import com.mgmtsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{projectId}/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTask(@PathVariable Long projectId,
                                                      @RequestBody TaskRequest taskRequest,
                                                      @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Task task = taskService.createTask(taskRequest, projectId, user);
            return ResponseEntity.ok(ApiResponse.success("任务创建成功", task));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getTasks(@PathVariable Long projectId,
                                                          @RequestParam(required = false) TaskStatus status,
                                                          @RequestParam(required = false) TaskPriority priority,
                                                          @RequestParam(required = false, defaultValue = "created_at") String sortBy,
                                                          @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                                          @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            List<Task> tasks;
            
            if (status != null && priority != null) {
                tasks = taskService.getTasksByProject(projectId, user);
                tasks = tasks.stream()
                        .filter(task -> task.getStatus() == status && task.getPriority() == priority)
                        .toList();
            } else if (status != null) {
                tasks = taskService.getTasksByProjectAndStatus(projectId, status, user);
            } else if (priority != null) {
                tasks = taskService.getTasksByProjectAndPriority(projectId, priority, user);
            } else {
                tasks = taskService.getTasksByProjectSorted(projectId, sortBy, sortDir, user);
            }
            
            return ResponseEntity.ok(ApiResponse.success("获取任务列表成功", tasks));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> getTask(@PathVariable Long projectId,
                                                   @PathVariable Long id,
                                                   @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Task task = taskService.getTaskById(id, projectId, user);
            return ResponseEntity.ok(ApiResponse.success("获取任务详情成功", task));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> updateTask(@PathVariable Long projectId,
                                                      @PathVariable Long id,
                                                      @RequestBody TaskRequest taskRequest,
                                                      @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Task task = taskService.updateTask(id, projectId, taskRequest, user);
            return ResponseEntity.ok(ApiResponse.success("任务更新成功", task));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Task>> updateTaskStatus(@PathVariable Long projectId,
                                                            @PathVariable Long id,
                                                            @RequestParam TaskStatus status,
                                                            @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Task task = taskService.updateTaskStatus(id, projectId, status, user);
            return ResponseEntity.ok(ApiResponse.success("任务状态更新成功", task));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/priority")
    public ResponseEntity<ApiResponse<Task>> updateTaskPriority(@PathVariable Long projectId,
                                                              @PathVariable Long id,
                                                              @RequestParam TaskPriority priority,
                                                              @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            Task task = taskService.updateTaskPriority(id, projectId, priority, user);
            return ResponseEntity.ok(ApiResponse.success("任务优先级更新成功", task));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long projectId,
                                                      @PathVariable Long id,
                                                      @RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            taskService.deleteTask(id, projectId, user);
            return ResponseEntity.ok(ApiResponse.success("任务删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
} 