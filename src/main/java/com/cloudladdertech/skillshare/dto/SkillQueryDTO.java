package com.cloudladdertech.skillshare.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "技能查询请求", description = "技能查询请求参数")

public class SkillQueryDTO {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize;
    
    @ApiModelProperty(value = "技能分类ID", example = "1")
    private Integer categoryId;
    
    @ApiModelProperty(value = "用户ID", example = "123")
    private Integer userId;
    
    @ApiModelProperty(value = "学校名称", example = "北京大学")
    private String school;
    
    @ApiModelProperty(value = "搜索关键词", example = "编程")
    private String keyword;
    
    @ApiModelProperty(value = "排序字段", example = "createdAt", notes = "可选值: createdAt (发布时间, 默认), rating (评分), exchangeCount (交换次数), viewCount (浏览量)")
    private String sortBy;
    
    @ApiModelProperty(value = "排序方式", example = "desc", notes = "可选值: asc (升序), desc (降序, 默认)")
    private String sortOrder;

    public int getPageOrDefault() {
        return (page != null && page > 0) ? page : DEFAULT_PAGE;
    }

    public int getPageSizeOrDefault() {
        // Add reasonable limits for page size if desired
        return (pageSize != null && pageSize > 0) ? pageSize : DEFAULT_PAGE_SIZE;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortOrder() {
         return sortOrder;
    }
    
}
