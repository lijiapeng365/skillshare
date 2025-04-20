package com.cloudladdertech.skillshare.service.impl;

import com.cloudladdertech.skillshare.dto.LoginDTO;
import com.cloudladdertech.skillshare.dto.RegisterDTO;
import com.cloudladdertech.skillshare.entity.User;
import com.cloudladdertech.skillshare.mapper.UserMapper;
import com.cloudladdertech.skillshare.service.UserService;
import com.cloudladdertech.skillshare.util.JwtUtil;
import com.cloudladdertech.skillshare.vo.LoginVO;
import com.cloudladdertech.skillshare.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Value("${jwt.expiration}")
    private Integer expiration;

    @Override
    public UserVO register(RegisterDTO registerDTO) {
        // 检查邮箱是否已注册
        User existUser = userMapper.findByEmail(registerDTO.getEmail());
        if (existUser != null) {
            throw new RuntimeException("该邮箱已注册");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        // 密码加密
        String passwordHash = DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPasswordHash(passwordHash);
        user.setSchool(registerDTO.getSchool());
        user.setMajor(registerDTO.getMajor());
        
        // 保存用户
        userMapper.insert(user);
        
        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return userVO;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 根据邮箱查询用户
        User user = userMapper.findByEmail(loginDTO.getEmail());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        String passwordHash = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!Objects.equals(passwordHash, user.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }
        
        // 生成token
        String token = jwtUtil.generateToken(user.getUserId());
        
        // 创建登录响应
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setExpiresIn(expiration);
        
        // 设置用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        loginVO.setUserInfo(userVO);
        
        return loginVO;
    }

    @Override
    public UserVO getUserInfo(Integer userId) {
        // 根据ID查询用户
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return userVO;
    }
} 