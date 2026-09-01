package com.example.usercenterbackend.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author anchor
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e) {
        log.error("BusinessException: "+e.getMessage(),e);
        return BaseResponse.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse handleException(RuntimeException e) {
        log.error("RuntimeException: ",e);
        return BaseResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(),e.getMessage(),"");
    }
}
