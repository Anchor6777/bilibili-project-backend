package com.example.usercenterbackend.commons;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PARAMS_ERROR(40001,"请求参数错误"),
    NOT_LOGIN(40100,"未登录"),
    NO_AUTH(40101,"无权限"),
    INTERNAL_SERVER_ERROR(50000,"服务器内部错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
