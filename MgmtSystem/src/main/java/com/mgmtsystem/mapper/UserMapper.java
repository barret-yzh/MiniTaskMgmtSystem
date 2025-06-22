package com.mgmtsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mgmtsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE user_name = #{username} AND deleted = 0")
    User findByUsername(String username);
    
    @Select("SELECT COUNT(*) > 0 FROM user WHERE user_name = #{username} AND deleted = 0")
    boolean existsByUsername(String username);
    
    @Select("SELECT COUNT(*) > 0 FROM user WHERE email = #{email} AND deleted = 0")
    boolean existsByEmail(String email);
} 