package com.lin.bigc_answer.entity.utils;

import lombok.Data;

/**
 * @author th1nk
 * @date 2022/7/7 下午6:02
 */
@Data
public class StudentDTO {
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 邮箱用来找回密码或申请账号
     */
    private String email;

    /**
     * 手机号用来找回密码或申请账号
     */
    private String phone;

    private Integer role;
}
