package com.cloudladdertech.skillshare.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 技能分类实体类
 */
@Data
public class SkillCategory {
    private Integer categoryId; // 分类ID，主键
    private String name; // 分类名称
    private String description; // 分类描述
    private Integer skillCount; // 该分类下的技能数量
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}
