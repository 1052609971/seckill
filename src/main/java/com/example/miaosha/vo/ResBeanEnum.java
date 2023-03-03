package com.example.miaosha.vo;

import lombok.ToString;

/**服务器处理请求结果枚举类
 * @author longjian
 */

//公共返回对象枚举
@ToString
public enum ResBeanEnum {
    /**
    * @Description:
     * SUCCESS说明服务器处理请求成功
     */
    SUCCESS(200,"SUCCESS"),
    /**
     * @Description:
     * ERROR说明服务器处理请求失败
     */
    ERROR(500,"服务器异常"),
    LOGIN_ERROR(500210,"用户名或密码不正确"),
    MOBILE_ERROR(500211,"手机号格式不正确"),
    BIND_ERROR(200212,"参数校验异常"),
    EMPTY_STOCK(200213,"库存不足"),
    REPEATE_ERROR(200214,"每个用户限购一次，请勿重复购买"),
    SESSION_ERROR(200215,"用户不存在");
    private final Integer code;
    private final String message;

    ResBeanEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
