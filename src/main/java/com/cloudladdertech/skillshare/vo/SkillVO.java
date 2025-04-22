package com.cloudladdertech.skillshare.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 技能信息视图对象
 */
@Data
@ApiModel(value = "技能信息", description = "技能详细信息")
public class SkillVO {
    @ApiModelProperty(value = "技能ID")
    private Integer skillId;
    
    @ApiModelProperty(value = "发布者ID")
    private Integer userId;
    
    @ApiModelProperty(value = "发布者简要信息")
    private UserInfo user;
    
    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;
    
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    
    @ApiModelProperty(value = "技能标题")
    private String title;
    
    @ApiModelProperty(value = "技能简短描述")
    private String description;
    
    @ApiModelProperty(value = "评分")
    private Double rating;
    
    @ApiModelProperty(value = "交换次数")
    private Integer exchangeCount;
    
    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;
    
    @ApiModelProperty(value = "是否激活")
    private Boolean isActive;
    
    @ApiModelProperty(value = "创建时间", example = "2023-01-01T12:00:00Z")
    private String createdAt;
    
    @ApiModelProperty(value = "技能封面图URL")
    private String coverImageUrl;
    
    /**
     * 发布者简要信息
     */
    @Data
    @ApiModel(value = "用户简要信息", description = "用户基本信息")
    public static class UserInfo {
        @ApiModelProperty(value = "发布者用户名")
        private String username;
        
        @ApiModelProperty(value = "发布者头像URL")
        private String avatarUrl;
        
        @ApiModelProperty(value = "发布者学校")
        private String school;
    }
    
    /**
     * 分页信息
     */
    @Data
    @ApiModel(value = "分页信息", description = "分页相关参数")
    public static class Pagination {
        @ApiModelProperty(value = "当前页码")
        private Integer currentPage;
        
        @ApiModelProperty(value = "每页数量")
        private Integer pageSize;
        
        @ApiModelProperty(value = "总条目数")
        private Long totalItems;
        
        @ApiModelProperty(value = "总页数")
        private Integer totalPages;
    }
}

