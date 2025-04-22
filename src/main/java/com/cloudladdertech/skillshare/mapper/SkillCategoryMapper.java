package com.cloudladdertech.skillshare.mapper;

import com.cloudladdertech.skillshare.entity.SkillCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 技能分类Mapper接口
 */
@Mapper
public interface SkillCategoryMapper {

    /**
     * 通过ID查询技能分类
     */
    SkillCategory findById(@Param("categoryId") Integer categoryId);

    /**
     * 查询所有技能分类
     */
    List<SkillCategory> findAll();

    /**
     * 插入技能分类
     */
    int insert(SkillCategory category);

    /**
     * 更新技能分类
     */
    int update(SkillCategory category);

    /**
     * 通过ID删除技能分类
     */
    int deleteById(@Param("categoryId") Integer categoryId);

    /**
     * 根据分类 ID 列表批量查询分类
     * @param ids 分类 ID 列表
     * @return 分类实体列表
     */
    List<SkillCategory> findByIds(@Param("ids") List<Integer> ids);
}