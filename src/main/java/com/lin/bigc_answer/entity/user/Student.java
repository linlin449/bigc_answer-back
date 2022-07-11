package com.lin.bigc_answer.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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

    public void hidePassword() {
        this.password = "******";
    }
}
