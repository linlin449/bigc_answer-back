package com.lin.bigc_answer.exception;

import com.lin.bigc_answer.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//作为SpringMVC的异常处理器
@Slf4j(topic = "异常处理器")
@RestControllerAdvice
public class ProjectExceptionAdvice {
    //拦截所有异常信息
    @ExceptionHandler
    public R doException(Exception e) {
        e.printStackTrace();
        log.error("服务器内部异常");
        return new R().fail("服务器异常!", null, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}