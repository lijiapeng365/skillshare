package com.cloudladdertech.skillshare.mapper;

import com.cloudladdertech.skillshare.entity.SkillTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 技能标签Mapper接口
 */
@Mapper
public interface SkillTagMapper {

    /**
     * 通过ID查询技能标签
     */
    SkillTag findById(@Param("tagId") Integer tagId);

    /**
     * 根据分类ID查询技能标签列表
     */
    List<SkillTag> findByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 查询所有技能标签
     */
    List<SkillTag> findAll();

    /**
     * 插入技能标签
     */
    int insert(SkillTag tag);

    /**
     * 更新技能标签
     */
    int update(SkillTag tag);

    /**
     * 通过ID删除技能标签
     */
    int deleteById(@Param("tagId") Integer tagId);

    /**
     * 根据标签名称查询标签
     * @param name 标签名称
     * @return SkillTag 实体，如果不存在则返回 null
     */
    SkillTag findByName(@Param("name") String name);

    /**
     * 根据 ID 列表批量查询标签
     * @param ids 标签 ID 列表
     * @return SkillTag 实体列表
     */
    List<SkillTag> findByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据技能 ID 查询关联的标签列表。
     * @param skillId 技能 ID
     * @return 关联的 SkillTag 列表
     */
    List<SkillTag> findBySkillId(@Param("skillId") Integer skillId);
}