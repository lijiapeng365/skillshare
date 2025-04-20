package com.cloudladdertech.skillshare.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录响应类
 */
@Data
@ApiModel(value = "登录响应", description = "登录成功后返回的数据")
public class LoginVO {
    @ApiModelProperty(value = "JWT令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @ApiModelProperty(value = "令牌有效期(秒)", example = "3600")
    private Integer expiresIn;
    
    @ApiModelProperty(value = "用户信息")
    private UserVO userInfo;
} 