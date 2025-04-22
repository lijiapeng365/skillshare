package com.cloudladdertech.skillshare.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 技能标签实体类
 */
@Data
public class SkillTag {
    private Integer tagId; // 标签ID，主键
    private String name; // 标签名称
    private Integer categoryId; // 所属分类ID，外键关联 SkillCategory
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}