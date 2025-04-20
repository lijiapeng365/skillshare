package com.cloudladdertech.skillshare.mapper;

import com.cloudladdertech.skillshare.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 通过邮箱查询用户
     */
    User findByEmail(@Param("email") String email);
    
    /**
     * 通过ID查询用户
     */
    User findById(@Param("userId") Integer userId);
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 更新用户
     */
    int update(User user);
} 