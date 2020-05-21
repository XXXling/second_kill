package com.auv.kill.api.enums;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/11 12:56
 */
public enum StatusCode {

    Success(0,"成功"),
    Fail(-1,"失败"),
    InvalidParams(201,"非法的参数!"),
    UserNotLogin(202,"用户没登录"),

    ;

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
