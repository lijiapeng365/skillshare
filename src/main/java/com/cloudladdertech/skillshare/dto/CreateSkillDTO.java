package com.cloudladdertech.skillshare.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;


@Data
@ApiModel(value = "创建技能请求", description = "创建技能请求参数")
public class CreateSkillDTO {
    @ApiModelProperty(value = "技能分类ID", required = true, example = "1")
    private Integer categoryId;

    @ApiModelProperty(value = "技能标题", required = true, example = "Java编程基础教学", notes = "2-100字符")
    private String title;

    @ApiModelProperty(value = "技能详细描述", required = true, example = "从零开始学习Java编程，包括基础语法、面向对象等内容")
    private String description;

    @ApiModelProperty(value = "教学方法描述", example = "线上一对一教学，每周两次，每次1小时")
    private String teachingMethod;

    @ApiModelProperty(value = "交换期望描述", example = "希望能交换Python或前端开发相关技能")
    private String exchangeExpectation;

    @ApiModelProperty(value = "标签名称列表", example = "[\"Java\", \"编程\", \"后端\"]")
    private List<String> tags;

    @ApiModelProperty(value = "技能封面图URL", example = "https://example.com/images/cover.jpg")
    private String coverImageUrl;

    @ApiModelProperty(value = "技能详情图片URL列表", example = "[\"https://example.com/images/detail1.jpg\", \"https://example.com/images/detail2.jpg\"]")
    private List<String> images;

    @ApiModelProperty(value = "是否立即激活技能", example = "true", notes = "如果不传，默认为 true")
    private Boolean isActive;
}
