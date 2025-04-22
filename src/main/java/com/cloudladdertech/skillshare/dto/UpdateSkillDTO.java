package com.cloudladdertech.skillshare.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(value = "更新技能请求", description = "更新技能请求参数")
public class UpdateSkillDTO {
    @ApiModelProperty(value = "技能分类ID", example = "1")
    private Integer categoryId;

    @ApiModelProperty(value = "技能标题", example = "Java编程基础教学", notes = "2-100字符")
    private String title;

    @ApiModelProperty(value = "技能详细描述", example = "从零开始学习Java编程，包括基础语法、面向对象等内容")
    private String description;

    @ApiModelProperty(value = "教学方法描述", example = "线上一对一教学，每周两次，每次1小时")
    private String teachingMethod;

    @ApiModelProperty(value = "交换期望描述", example = "希望能交换Python或前端开发相关技能")
    private String exchangeExpectation;

    @ApiModelProperty(value = "是否激活状态", example = "true")
    private Boolean isActive;

    @ApiModelProperty(value = "标签名称列表", example = "[\"Java\", \"编程\", \"后端\"]", notes = "会覆盖原有标签")
    private List<String> tags;

    @ApiModelProperty(value = "技能封面图URL", example = "https://example.com/images/cover.jpg")
    private String coverImageUrl;

    @ApiModelProperty(value = "技能详情图片URL列表", example = "[\"https://example.com/images/detail1.jpg\", \"https://example.com/images/detail2.jpg\"]", notes = "会覆盖原有图片")
    private List<String> images;
    
}