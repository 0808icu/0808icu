package com.ethan.project.common;

/**
 * @Description:通用返回工具类
 * @Auther: http://www.0808.icu
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param code
     * @param message
     * @return
     */
    public static BaseResponse error(int code,String message){
        return new BaseResponse<>(code,null,message);
    }

    /**
     * 失败
     * @param errorCode
     * @param massage
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String massage){
        return new BaseResponse<>(errorCode.getCode(),null,massage);

    }
}
