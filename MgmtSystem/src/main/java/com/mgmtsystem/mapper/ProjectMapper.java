package com.mgmtsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mgmtsystem.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    
    @Select("SELECT * FROM project WHERE owner_id = #{ownerId} AND deleted = 0")
    List<Project> findByOwnerId(Long ownerId);
    
    @Select("SELECT * FROM project WHERE id = #{id} AND owner_id = #{ownerId} AND deleted = 0")
    Project findByIdAndOwnerId(Long id, Long ownerId);
} 