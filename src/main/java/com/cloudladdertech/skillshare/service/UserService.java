package com.cloudladdertech.skillshare.service;

import com.cloudladdertech.skillshare.dto.LoginDTO;
import com.cloudladdertech.skillshare.dto.RegisterDTO;
import com.cloudladdertech.skillshare.entity.User;
import com.cloudladdertech.skillshare.vo.LoginVO;
import com.cloudladdertech.skillshare.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    UserVO register(RegisterDTO registerDTO);
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(Integer userId);
} 