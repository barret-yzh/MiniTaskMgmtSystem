package com.mgmtsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mgmtsystem.entity.Task;
import com.mgmtsystem.enums.TaskPriority;
import com.mgmtsystem.enums.TaskStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    
    @Select("SELECT * FROM task WHERE project_id = #{projectId} AND deleted = 0")
    List<Task> findByProjectId(Long projectId);
    
    @Select("SELECT * FROM task WHERE project_id = #{projectId} AND status = #{status} AND deleted = 0")
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
    
    @Select("SELECT * FROM task WHERE project_id = #{projectId} AND priority = #{priority} AND deleted = 0")
    List<Task> findByProjectIdAndPriority(Long projectId, TaskPriority priority);
    
    @Select("SELECT * FROM task WHERE project_id = #{projectId} AND status = #{status} AND priority = #{priority} AND deleted = 0")
    List<Task> findByProjectIdAndStatusAndPriority(Long projectId, TaskStatus status, TaskPriority priority);
    
    @Select("SELECT * FROM task WHERE id = #{id} AND project_id = #{projectId} AND deleted = 0")
    Task findByIdAndProjectId(Long id, Long projectId);
    
    @Select("SELECT t.* FROM task t INNER JOIN project p ON t.project_id = p.id " +
            "WHERE t.project_id = #{projectId} AND p.owner_id = #{ownerId} AND t.deleted = 0 AND p.deleted = 0")
    List<Task> findByProjectIdAndOwnerId(Long projectId, Long ownerId);
    
    @Select("SELECT t.* FROM task t INNER JOIN project p ON t.project_id = p.id " +
            "WHERE t.project_id = #{projectId} AND p.owner_id = #{ownerId} AND t.status = #{status} " +
            "AND t.deleted = 0 AND p.deleted = 0")
    List<Task> findByProjectIdAndOwnerIdAndStatus(Long projectId, Long ownerId, TaskStatus status);
    
    @Select("SELECT t.* FROM task t INNER JOIN project p ON t.project_id = p.id " +
            "WHERE t.project_id = #{projectId} AND p.owner_id = #{ownerId} AND t.priority = #{priority} " +
            "AND t.deleted = 0 AND p.deleted = 0")
    List<Task> findByProjectIdAndOwnerIdAndPriority(Long projectId, Long ownerId, TaskPriority priority);
    
    @Select("SELECT t.* FROM task t INNER JOIN project p ON t.project_id = p.id " +
            "WHERE t.project_id = #{projectId} AND p.owner_id = #{ownerId} AND t.deleted = 0 AND p.deleted = 0 " +
            "ORDER BY ${sortBy} ${sortDir}")
    List<Task> findByProjectIdAndOwnerIdSorted(Long projectId, Long ownerId, String sortBy, String sortDir);
} 