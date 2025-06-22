package com.mgmtsystem.service;

import com.mgmtsystem.dto.TaskRequest;
import com.mgmtsystem.entity.Project;
import com.mgmtsystem.entity.Task;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.enums.TaskPriority;
import com.mgmtsystem.enums.TaskStatus;
import com.mgmtsystem.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ProjectService projectService;

    public Task createTask(TaskRequest taskRequest, Long projectId, User owner) {
        Project project = projectService.getProjectById(projectId, owner);

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setProjectId(project.getId());

        if (taskRequest.getStatus() != null) {
            task.setStatus(taskRequest.getStatus());
        }
        if (taskRequest.getPriority() != null) {
            task.setPriority(taskRequest.getPriority());
        }

        taskMapper.insert(task);
        return task;
    }

    public List<Task> getTasksWithFiltersAndSort(Long projectId, TaskStatus status, TaskPriority priority, 
                                                String sortBy, String sortDir, User owner) {
        projectService.getProjectById(projectId, owner);
        
        return taskMapper.findTasksWithFiltersAndSort(projectId, owner.getId(), status, priority, sortBy, sortDir);
    }

    public List<Task> getTasksByProject(Long projectId, User owner) {
        projectService.getProjectById(projectId, owner);
        return taskMapper.findByProjectIdAndOwnerId(projectId, owner.getId());
    }

    public List<Task> getTasksByProjectAndStatus(Long projectId, TaskStatus status, User owner) {
        projectService.getProjectById(projectId, owner);
        return taskMapper.findByProjectIdAndOwnerIdAndStatus(projectId, owner.getId(), status);
    }

    public List<Task> getTasksByProjectAndPriority(Long projectId, TaskPriority priority, User owner) {
        projectService.getProjectById(projectId, owner);
        return taskMapper.findByProjectIdAndOwnerIdAndPriority(projectId, owner.getId(), priority);
    }

    public List<Task> getTasksByProjectSorted(Long projectId, String sortBy, String sortDir, User owner) {
        projectService.getProjectById(projectId, owner);
        return taskMapper.findByProjectIdAndOwnerIdSorted(projectId, owner.getId(), sortBy, sortDir);
    }

    public Task getTaskById(Long id, Long projectId, User owner) {
        projectService.getProjectById(projectId, owner);
        Task task = taskMapper.findByIdAndProjectId(id, projectId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        return task;
    }

    public Task updateTask(Long id, Long projectId, TaskRequest taskRequest, User owner) {
        Task task = getTaskById(id, projectId, owner);

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());

        if (taskRequest.getStatus() != null) {
            task.setStatus(taskRequest.getStatus());
        }
        if (taskRequest.getPriority() != null) {
            task.setPriority(taskRequest.getPriority());
        }

        taskMapper.updateById(task);
        return task;
    }

    public Task updateTaskStatus(Long id, Long projectId, TaskStatus status, User owner) {
        Task task = getTaskById(id, projectId, owner);
        task.setStatus(status);
        taskMapper.updateById(task);
        return task;
    }

    public Task updateTaskPriority(Long id, Long projectId, TaskPriority priority, User owner) {
        Task task = getTaskById(id, projectId, owner);
        task.setPriority(priority);
        taskMapper.updateById(task);
        return task;
    }

    public void deleteTask(Long id, Long projectId, User owner) {
        Task task = getTaskById(id, projectId, owner);
        taskMapper.deleteById(task.getId());
    }
} 