package com.cloudladdertech.skillshare.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录请求DTO
 */
@Data
@ApiModel(value = "用户登录请求", description = "用户登录时提交的数据")
public class LoginDTO {
    @ApiModelProperty(value = "邮箱", required = true, example = "test@example.com")
    private String email;
    
    @ApiModelProperty(value = "密码", required = true, example = "password123")
    private String password;
} 