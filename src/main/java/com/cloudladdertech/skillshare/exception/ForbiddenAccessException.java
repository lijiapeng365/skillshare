package com.cloudladdertech.skillshare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 当用户尝试访问他们没有权限的资源时抛出此异常。
 * 通常映射到 HTTP 403 Forbidden。
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenAccessException extends RuntimeException {

    public ForbiddenAccessException(String message) {
        super(message);
    }

    public ForbiddenAccessException(String message, Throwable cause) {
        super(message, cause);
    }
} 