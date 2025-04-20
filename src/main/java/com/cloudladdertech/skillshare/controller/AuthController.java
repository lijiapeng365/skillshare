package com.cloudladdertech.skillshare.controller;

import com.cloudladdertech.skillshare.dto.LoginDTO;
import com.cloudladdertech.skillshare.dto.RegisterDTO;
import com.cloudladdertech.skillshare.service.UserService;
import com.cloudladdertech.skillshare.vo.LoginVO;
import com.cloudladdertech.skillshare.vo.Result;
import com.cloudladdertech.skillshare.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "用户认证接口", description = "包括用户注册、登录和获取用户信息等接口")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户注册接口，注册成功后返回用户基本信息")
    public Result<UserVO> register(@RequestBody RegisterDTO registerDTO) {
        UserVO userVO = userService.register(registerDTO);
        return Result.created(userVO);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录接口，登录成功后返回token和用户信息")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/me")
    @ApiOperation(value = "获取用户信息", notes = "根据用户ID获取用户详细信息")
    @ApiImplicitParam(name = "user_id", value = "用户ID", required = true, dataTypeClass = Integer.class, paramType = "query", example = "123")
    public Result<UserVO> getUserInfo(@RequestParam("user_id") Integer userId) {
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }
} 