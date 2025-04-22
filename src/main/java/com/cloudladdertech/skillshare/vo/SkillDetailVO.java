package com.cloudladdertech.skillshare.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 技能详细信息视图对象
 */
@Data
@ApiModel(value = "技能详细信息", description = "技能的详细信息，包含发布者信息、分类、标签等")
public class SkillDetailVO {
    @ApiModelProperty(value = "技能ID", example = "1")
    private Integer skillId;
    
    @ApiModelProperty(value = "发布者ID", example = "123")
    private Integer userId;
    
    @ApiModelProperty(value = "发布者详细信息")
    private UserVO user;
    
    @ApiModelProperty(value = "分类ID", example = "5")
    private Integer categoryId;
    
    @ApiModelProperty(value = "分类名称", example = "编程技能")
    private String categoryName;
    
    @ApiModelProperty(value = "技能标题", example = "Java编程基础教学")
    private String title;
    
    @ApiModelProperty(value = "技能详细描述", example = "从零开始学习Java编程，包括基础语法、面向对象等内容")
    private String description;
    
    @ApiModelProperty(value = "教学方法", example = "线上一对一教学，每周两次，每次1小时")
    private String teachingMethod;
    
    @ApiModelProperty(value = "交换期望", example = "希望能交换Python或前端开发相关技能")
    private String exchangeExpectation;
    
    @ApiModelProperty(value = "评分", example = "4.8")
    private Double rating;
    
    @ApiModelProperty(value = "交换次数", example = "12")
    private Integer exchangeCount;
    
    @ApiModelProperty(value = "浏览次数", example = "256")
    private Integer viewCount;
    
    @ApiModelProperty(value = "是否激活", example = "true")
    private Boolean isActive;
    
    @ApiModelProperty(value = "创建时间", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAt;
    
    @ApiModelProperty(value = "更新时间", example = "2023-01-10T00:00:00")
    private LocalDateTime updatedAt;
    
    @ApiModelProperty(value = "技能标签列表")
    private List<TagVO> tags;
    
    @ApiModelProperty(value = "技能相关图片URL列表", example = "[\"https://example.com/images/detail1.jpg\"]")
    private List<String> images;
    
    @ApiModelProperty(value = "当前用户是否已收藏", example = "false", notes = "需要用户认证")
    private Boolean isFavorited;
    
    @Data
    @ApiModel(value = "标签信息", description = "技能标签信息")
    public static class TagVO {
        @ApiModelProperty(value = "标签ID", example = "1")
        private Integer tagId;
        
        @ApiModelProperty(value = "标签名称", example = "Java")
        private String name;
    }
}

