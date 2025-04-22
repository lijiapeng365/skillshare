package com.cloudladdertech.skillshare.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Favorite (收藏) 数据访问层
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 检查指定用户是否收藏了指定技能
     * @param userId 用户ID
     * @param skillId 技能ID
     * @return 如果存在收藏记录则返回 true, 否则返回 false
     */
    boolean exists(@Param("userId") Integer userId, @Param("skillId") Integer skillId);

    /**
     * 根据技能ID删除所有相关的收藏记录
     * @param skillId 技能ID
     * @return 影响的行数
     */
    int deleteBySkillId(@Param("skillId") Integer skillId);

    /**
     * 根据用户ID删除所有相关的收藏记录 (可选, 可能在删除用户时需要)
     * @param userId 用户ID
     * @return 影响的行数
     */
    // int deleteByUserId(@Param("userId") Integer userId);

    /**
     * 插入一条新的收藏记录 (可选, 如果在 Service 层处理创建)
     * @param userId 用户ID
     * @param skillId 技能ID
     * @return 影响的行数
     */
    // int insert(@Param("userId") Integer userId, @Param("skillId") Integer skillId);
} 