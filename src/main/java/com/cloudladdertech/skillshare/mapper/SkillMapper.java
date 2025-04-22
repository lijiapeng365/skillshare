package com.cloudladdertech.skillshare.mapper;

import com.cloudladdertech.skillshare.entity.Skill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Skill 数据访问层
 */
@Mapper
public interface SkillMapper {

    /**
     * 根据条件分页查询技能列表
     * @param categoryId 分类ID (可选)
     * @param keyword 关键词 (可选, 搜索标题或描述)
     * @param sortBy 排序字段 (可选, 如 'created_at', 'rating')
     * @param sortOrder 排序顺序 (可选, 'ASC' 或 'DESC')
     * @param limit 返回数量
     * @param offset 偏移量
     * @return 技能实体列表
     */
    List<Skill> findPaged(
            @Param("categoryId") Integer categoryId,
            @Param("keyword") String keyword,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 根据条件统计技能总数
     * @param categoryId 分类ID (可选)
     * @param keyword 关键词 (可选)
     * @return 技能总数
     */
    long countTotal(
            @Param("categoryId") Integer categoryId,
            @Param("keyword") String keyword
    );

    /**
     * 插入一个新的技能记录
     * @param skill 技能实体 (需要包含 userId, categoryId, title, description 等)
     * @return 影响的行数
     */
    int insert(Skill skill);

    /**
     * 根据ID查询技能
     * @param id 技能ID
     * @return 技能实体，如果不存在则返回 null
     */
    Skill findById(@Param("id") Integer id);

    /**
     * 更新一个已有的技能记录
     * @param skill 技能实体 (必须包含 skillId)
     * @return 影响的行数
     */
    int update(Skill skill);

    /**
     * 根据ID删除技能
     * @param id 技能ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 查询热门技能 (示例: 按浏览量或交换次数降序)
     * @param limit 返回数量
     * @return 热门技能列表
     */
    List<Skill> findHot(@Param("limit") int limit);

    // 可能需要的其他方法:
    // List<Skill> findByUserId(@Param("userId") Integer userId);
    // int incrementViewCount(@Param("id") Integer id);
    // int incrementExchangeCount(@Param("id") Integer id);
    // List<Skill> findByIds(@Param("ids") List<Integer> ids);
} 