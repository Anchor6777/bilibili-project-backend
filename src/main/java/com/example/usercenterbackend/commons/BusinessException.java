package com.example.usercenterbackend.commons;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final int code;
    private final String description;
    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = "";
    }

    public BusinessException(ErrorCode errorCode, String desc) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = desc;
    }

}
