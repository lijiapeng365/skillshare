package com.cloudladdertech.skillshare.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用响应结果类
 */
@Data
@ApiModel(value = "通用响应结果", description = "API通用返回结果")
public class Result<T> {
    @ApiModelProperty(value = "状态码", example = "200")
    private Integer code;
    
    @ApiModelProperty(value = "消息", example = "操作成功")
    private String message;
    
    @ApiModelProperty(value = "数据")
    private T data;

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(T data) {
        return success("success", data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 创建成功响应
     */
    public static <T> Result<T> created(T data) {
        Result<T> result = new Result<>();
        result.setCode(201);
        result.setMessage("创建成功");
        result.setData(data);
        return result;
    }
} 