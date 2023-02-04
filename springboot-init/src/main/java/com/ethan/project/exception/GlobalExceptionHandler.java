package com.ethan.project.exception;

import com.ethan.project.common.BaseResponse;
import com.ethan.project.common.ErrorCode;
import com.ethan.project.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description:全局异常处理类
 * @Auther: http://www.0808.icu
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessExcption.class)
    public BaseResponse<?> businessExceptionHandler(BusinessExcption excption){
        //打印错误信息日志
        log.error("businessException: "+excption.getMessage(),excption);
        return ResultUtils.error(excption.getCode(),excption.getMessage());
    }
    public BaseResponse<?> runtimeExcptionHandler(RuntimeException exception){
        //打印错误信息日志
        log.error("runtimeExcprion: "+exception);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,exception.getMessage());
    }
}
