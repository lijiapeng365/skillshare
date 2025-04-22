package com.cloudladdertech.skillshare.mapper;

import com.cloudladdertech.skillshare.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据用户 ID 列表批量查询用户
     * @param ids 用户 ID 列表
     * @return 用户实体列表
     */
    List<User> findByIds(@Param("ids") List<Integer> ids);
} 