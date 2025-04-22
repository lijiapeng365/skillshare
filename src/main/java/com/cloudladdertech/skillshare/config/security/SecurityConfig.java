package com.cloudladdertech.skillshare.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级别的权限控制
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService; // 我们创建的 UserDetailsServiceImpl

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // 我们创建的 JWT 过滤器

    // 定义哪些 URL 路径应该被公开访问
    private static final String[] PUBLIC_URLS = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // -- 登录和注册接口
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            // -- 可能的其他公共端点 (例如获取技能列表/详情可以对匿名用户开放)
            // 注意：这里允许 GET 请求 /api/v1/skills 和 /api/v1/skills/{id} 对所有人开放
            // 如果获取详情需要根据用户ID判断收藏状态，则在Controller层处理匿名用户情况
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF
            .csrf().disable()
            // 配置认证规则
            .authorizeRequests()
            // 允许对特定 HTTP 方法和路径的公开访问
            .antMatchers(HttpMethod.GET, "/api/v1/skills", "/api/v1/skills/hot", "/api/v1/skills/recommended", "/api/v1/skills/*").permitAll() // 允许匿名用户查看技能列表、热门、推荐和详情
            .antMatchers(PUBLIC_URLS).permitAll() // 允许访问定义的公共 URL
            .antMatchers(HttpMethod.OPTIONS).permitAll() // 允许跨域OPTIONS请求
            .anyRequest().authenticated() // 其他所有请求都需要认证
            .and()
            // 配置会话管理为无状态
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 添加 JWT 过滤器在用户名密码认证过滤器之前
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
            // 可以添加异常处理配置，例如处理未认证和未授权的访问
            // .exceptionHandling()
            // .authenticationEntryPoint(...) 
            // .accessDeniedHandler(...);
    }
} 