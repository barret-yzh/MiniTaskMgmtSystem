package com.mgmtsystem.service;

import com.mgmtsystem.dto.ProjectRequest;
import com.mgmtsystem.entity.Project;
import com.mgmtsystem.entity.User;
import com.mgmtsystem.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectMapper projectMapper;
    
    public Project createProject(ProjectRequest projectRequest, User owner) {
        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        project.setOwnerId(owner.getId());
        
        projectMapper.insert(project);
        return project;
    }
    
    public List<Project> getProjectsByOwner(User owner) {
        return projectMapper.findByOwnerId(owner.getId());
    }
    
    public List<Project> getProjectsByOwnerId(Long ownerId) {
        return projectMapper.findByOwnerId(ownerId);
    }
    
    public Project getProjectById(Long id, User owner) {
        Project project = projectMapper.findByIdAndOwnerId(id, owner.getId());
        if (project == null) {
            throw new RuntimeException("项目不存在或无权访问");
        }
        return project;
    }
    
    public Project getProjectById(Long id, Long ownerId) {
        Project project = projectMapper.findByIdAndOwnerId(id, ownerId);
        if (project == null) {
            throw new RuntimeException("项目不存在或无权访问");
        }
        return project;
    }
    
    public Project updateProject(Long id, ProjectRequest projectRequest, User owner) {
        Project project = getProjectById(id, owner);
        
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        
        projectMapper.updateById(project);
        return project;
    }
    
    public void deleteProject(Long id, User owner) {
        Project project = getProjectById(id, owner);
        projectMapper.deleteById(project.getId());
    }
    
    public Project findById(Long id) {
        Project project = projectMapper.selectById(id);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }
        return project;
    }
} 