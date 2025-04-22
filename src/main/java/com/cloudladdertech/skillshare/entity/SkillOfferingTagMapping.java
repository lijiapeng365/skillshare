package com.cloudladdertech.skillshare.entity;
import lombok.Data;
@Data
public class SkillOfferingTagMapping {
    private Integer mappingId; // 或复合主键 (skillId, tagId)
    private Integer skillId; // 关联 Skill
    private Integer tagId;   // 关联 SkillTag
}