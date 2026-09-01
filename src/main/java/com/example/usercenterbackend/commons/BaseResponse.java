package com.example.usercenterbackend.commons;

import java.io.Serial;
import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 2681796856560691299L;

    public int code;
    public T data;
    public String message;
    public String description;

    public BaseResponse(int code, T data, String message,String desc) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = desc;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "","");
    }

    public BaseResponse(ErrorCode errorCode,String desc) {
        this(errorCode.getCode(),null,errorCode.getMessage(),desc);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "ok","");
    }

    public static BaseResponse error(ErrorCode errorCode,String desc) {
        return new BaseResponse<>(errorCode,desc);
    }

    public static BaseResponse error(int code,String message,String desc) {
        return new BaseResponse<>(code,null,message,desc);
    }
}
