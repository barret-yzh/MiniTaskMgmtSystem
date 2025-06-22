package com.mgmtsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mgmtsystem.entity.Task;
import com.mgmtsystem.enums.TaskPriority;
import com.mgmtsystem.enums.TaskStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    
    /**
     * 任务查询，支持筛选和排序（只支持按优先级或创建时间排序）
     */
    @Select("<script>" +
            "SELECT t.* FROM task t INNER JOIN project p ON t.project_id = p.id " +
            "WHERE t.project_id = #{projectId} AND p.owner_id = #{ownerId} " +
            "AND t.deleted = 0 AND p.deleted = 0 " +
            "<if test='status != null'> AND t.status = #{status} </if>" +
            "<if test='priority != null'> AND t.priority = #{priority} </if>" +
            "ORDER BY " +
            "<choose>" +
            "<when test='sortBy == \"priority\"'>" +
            "<choose>" +
            "<when test='sortDir == \"asc\"'>" +
            "CASE t.priority WHEN 'LOW' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'HIGH' THEN 3 END ASC" +
            "</when>" +
            "<otherwise>" +
            "CASE t.priority WHEN 'HIGH' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'LOW' THEN 3 END ASC" +
            "</otherwise>" +
            "</choose>" +
            "</when>" +
            "<otherwise>" +
            "t.created_at ${sortDir}" +
            "</otherwise>" +
            "</choose>" +
            "</script>")
    List<Task> findTasksWithFiltersAndSort(@Param("projectId") Long projectId, 
                                          @Param("ownerId") Long ownerId,
                                          @Param("status") TaskStatus status, 
                                          @Param("priority") TaskPriority priority,
                                          @Param("sortBy") String sortBy, 
                                          @Param("sortDir") String sortDir);
} 