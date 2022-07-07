package com.lin.bigc_answer.exception;

public interface ErrorCode {
    //成功
    int NORMAL_SUCCESS = 200;

    //未授权错误
    int UNAUTHORIZED_ERROR = 401;

    //服务器内部错误
    int INTERNAL_SERVER_ERROR = 500;

    //未登录错误
    int NOTLOGIN_ERROR = 10000;

    //常规错误
    int NORMAL_ERROR = 10010;

    //参数错误
    int PARAMETER_ERROR = 10020;
}
