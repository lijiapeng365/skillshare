package com.cloudladdertech.skillshare.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息展示类
 */
@Data
@ApiModel(value = "用户信息", description = "用户基本信息")
public class UserVO {
    @ApiModelProperty(value = "用户ID", example = "123")
    private Integer userId;
    
    @ApiModelProperty(value = "用户名", example = "testuser")
    private String username;
    
    @ApiModelProperty(value = "邮箱", example = "test@example.com")
    private String email;
    
    @ApiModelProperty(value = "学校", example = "北京大学")
    private String school;
    
    @ApiModelProperty(value = "专业", example = "计算机科学")
    private String major;
    
    @ApiModelProperty(value = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
    
    @ApiModelProperty(value = "创建时间", example = "2023-01-01T00:00:00")
    private LocalDateTime createdAt;
} 