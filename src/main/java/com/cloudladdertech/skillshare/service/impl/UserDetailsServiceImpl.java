package com.cloudladdertech.skillshare.service.impl;

import com.cloudladdertech.skillshare.config.security.UserDetailsImpl;
import com.cloudladdertech.skillshare.entity.User;
import com.cloudladdertech.skillshare.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security UserDetailsService 实现
 * 用于根据用户名（在此场景中是用户ID）加载用户信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 在我们的JWT策略中，'username' 实际上是用户ID的字符串表示
        Integer userId;
        try {
            userId = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("无效的用户ID格式: " + username);
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在，ID: " + userId);
        }

        // 返回包含用户ID的UserDetails实现
        // 如果需要权限，可以在这里查询并传递给UserDetailsImpl
        return new UserDetailsImpl(user.getUserId());
    }
} 