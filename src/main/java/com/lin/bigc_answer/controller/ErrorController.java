package com.lin.bigc_answer.controller;

import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author th1nk
 * @date 2022/7/7 下午1:44
 * 用于自定义Shiro的未登录时302重定向和未授权时的401返回内容
 */
@RestController
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping("/401")
    public R ret401() {
        return new R().fail("未授权", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    @RequestMapping("/10000")
    public R ret10000() {
        return new R().fail("未登录", null, ErrorCode.NOTLOGIN_ERROR);
    }

    @RequestMapping("/list")
    public R errorList() {
        Field[] fields = ErrorCode.class.getDeclaredFields();
        Map<Integer, String> map = new HashMap<>();
        for (Field field : fields) {
            try {
                String name = field.getName();
                map.put((Integer) field.get(name), name);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return new R().success("success", map);
    }
}
