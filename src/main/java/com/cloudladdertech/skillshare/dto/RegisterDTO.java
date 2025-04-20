package com.cloudladdertech.skillshare.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户注册请求DTO
 */
@Data
@ApiModel(value = "用户注册请求", description = "用户注册时提交的数据")
public class RegisterDTO {
    @ApiModelProperty(value = "用户名", required = true, example = "testuser")
    private String username;
    
    @ApiModelProperty(value = "邮箱", required = true, example = "test@example.com")
    private String email;
    
    @ApiModelProperty(value = "密码", required = true, example = "password123")
    private String password;
    
    @ApiModelProperty(value = "学校", example = "北京大学")
    private String school;
    
    @ApiModelProperty(value = "专业", example = "计算机科学")
    private String major;
} 