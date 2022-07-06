package com.lin.bigc_answer.utils;

import com.lin.bigc_answer.exception.ErrorCode;
import lombok.Data;

@Data
public class R {
    // 消息码
    private int code;
    // 消息体
    private Object data;
    // 消息提示
    private String msg;

    public R() {
    }

    private R(int code, String msg, Object data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public R success(String msg, Object data, int errorCode) {
        this.code = errorCode;
        this.msg = msg;
        this.data = data;
        return this;
    }


    public R success(String msg, Object data) {
        this.code = ErrorCode.NORMAL_SUCCESS;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public R success(String msg) {
        this.code = ErrorCode.NORMAL_SUCCESS;
        this.msg = msg;
        return this;
    }

    public R fail(String msg, Object data, int errorCode) {
        this.code = errorCode;
        this.msg = msg;
        this.data = data;
        return this;
    }


    public R fail(String msg, Object data) {
        this.code = ErrorCode.NORMAL_ERROR;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public R fail(String msg) {
        this.code = ErrorCode.NORMAL_ERROR;
        this.msg = msg;
        return this;
    }

}