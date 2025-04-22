package com.cloudladdertech.skillshare.mapper;
import com.cloudladdertech.skillshare.entity.SkillOfferingTagMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface SkillOfferingTagMappingMapper {
    int insert(SkillOfferingTagMapping mapping);
    List<SkillOfferingTagMapping> findBySkillId(@Param("skillId") Integer skillId);
    int deleteBySkillId(@Param("skillId") Integer skillId);
    // int deleteByTagId(@Param("tagId") Integer tagId); // 如果删除标签时需要级联删除
}