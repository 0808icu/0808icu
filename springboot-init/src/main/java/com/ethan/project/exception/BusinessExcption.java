package com.ethan.project.exception;

import com.ethan.project.common.ErrorCode;

/**
 * @Description:自定义异常类
 * @Auther: http://www.0808.icu
 */
public class BusinessExcption extends RuntimeException{
    private final int code;

    public BusinessExcption(int code,String massage){
        super(massage);
        this.code=code;
    }

    public BusinessExcption(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
    }

    public BusinessExcption(ErrorCode errorCode,String massage){
        super(massage);
        this.code=errorCode.getCode();
    }

    public BusinessExcption(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
