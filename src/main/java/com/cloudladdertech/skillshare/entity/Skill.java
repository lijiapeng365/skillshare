package com.cloudladdertech.skillshare.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal; // 价格使用 BigDecimal

@Data
public class Skill {
    private Integer skillId; // 主键
    private Integer userId; // 发布者用户ID (外键关联 User)
    private Integer categoryId; // 所属分类ID (外键关联 SkillCategory)
    private String title; // 技能标题
    private String description; // 详细描述
    private String teachingMethod; // 教学方法 (来自 SkillDetailVO)
    private String exchangeExpectation; // 交换期望 (来自 SkillDetailVO)
    // private BigDecimal price; // 价格 (如果需要收费) - 移除，与数据库表结构对齐
    private String coverImageUrl; // 封面图片URL
    // 注意: images 列表可能更适合放在单独的 SkillImage 表中
    private Double rating; // 评分
    private Integer exchangeCount; // 交换次数
    private Integer viewCount; // 浏览次数
    private Boolean isActive; // 是否激活/上架
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
}