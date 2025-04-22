package com.cloudladdertech.skillshare.mapper;

import com.cloudladdertech.skillshare.entity.Skill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.math.BigDecimal; // 引入 BigDecimal

@Mapper
public interface SkillMapper {

    /**
     * 分页查询技能列表
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
     * 计算符合条件的技能总数
     */
    long countTotal(
            @Param("categoryId") Integer categoryId,
            @Param("keyword") String keyword
    );

    /**
     * 插入技能记录
     * (需要在XML中使用 useGeneratedKeys="true" keyProperty="skillId" 来回填ID)
     */
    int insert(Skill skill);

    /**
     * 通过ID查询技能
     */
    Skill findById(@Param("skillId") Integer skillId);

    /**
     * 更新技能记录
     */
    int update(Skill skill);

    /**
     * 通过ID删除技能
     */
    int deleteById(@Param("skillId") Integer skillId);

    /**
     * 查询热门技能 (例如按浏览量、评分等排序)
     */
    List<Skill> findHot(@Param("limit") Integer limit);

    /**
     * 增加技能浏览次数 (用于 getSkillById)
     */
    void incrementViewCount(@Param("skillId") Integer skillId);

    /**
     * 根据ID列表批量查询技能 (用于优化N+1)
     */
    List<Skill> findByIds(@Param("ids") List<Integer> ids);
}