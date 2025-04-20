package com.cloudladdertech.skillshare.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private Integer userId;
    private String username;
    private String email;
    private String passwordHash;
    private String school;
    private String major;
    private String avatarUrl;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 