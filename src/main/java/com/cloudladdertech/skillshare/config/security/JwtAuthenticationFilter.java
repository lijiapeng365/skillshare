package com.cloudladdertech.skillshare.config.security;

import com.cloudladdertech.skillshare.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器，在每个请求中验证JWT Token
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService; // 我们创建的 UserDetailsServiceImpl

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HEADER_AUTHORIZATION);
        String token = null;
        Integer userId = null;

        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            token = header.substring(TOKEN_PREFIX.length());
            try {
                userId = jwtUtil.getUserIdFromToken(token);
            } catch (ExpiredJwtException e) {
                log.warn("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("JWT token is unsupported: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("JWT token is malformed: {}", e.getMessage());
            } catch (SignatureException e) {
                log.warn("JWT signature validation failed: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("JWT claims string is empty: {}", e.getMessage());
            }
        }

        // 如果成功解析出 userId 并且当前没有认证信息
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 根据 userId 加载 UserDetails (username 在此是 userId 的字符串形式)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(String.valueOf(userId));

            // 验证 token 是否仍然有效（例如再次检查过期时间，虽然 getUserIdFromToken 已包含此检查）
            if (!jwtUtil.isTokenExpired(token)) { // 理论上 userId != null 已经隐含了未过期，但双重检查更安全
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 设置认证详情
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将认证信息设置到 Spring Security 上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("User '{}' authenticated successfully.", userId);
            }
        }

        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
} 