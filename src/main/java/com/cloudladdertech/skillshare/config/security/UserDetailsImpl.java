package com.cloudladdertech.skillshare.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections; // Import Collections

/**
 * Spring Security UserDetails 实现
 */
public class UserDetailsImpl implements UserDetails {

    private final Integer userId;
    // 可以根据需要添加其他用户信息，如 username, roles 等
    // private final String username;
    // private final Collection<? extends GrantedAuthority> authorities;

    // 简化构造函数，只包含 userId
    public UserDetailsImpl(Integer userId) {
        this.userId = userId;
        // this.username = username; // 如果需要用户名
        // this.authorities = authorities; // 如果需要权限
    }

    // --- UserDetails 接口实现 ---

    public Integer getId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 暂时返回空权限列表，如果需要基于角色的访问控制，需要实现权限加载逻辑
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // 返回 null，因为我们使用 JWT 进行身份验证，密码哈希在此处不直接使用
        return null;
    }

    @Override
    public String getUsername() {
        // 返回用户ID的字符串形式作为用户名，或者如果添加了username字段则返回它
        return String.valueOf(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 账户未过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 账户未锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 凭证未过期
    }

    @Override
    public boolean isEnabled() {
        return true; // 账户已启用
    }
} 