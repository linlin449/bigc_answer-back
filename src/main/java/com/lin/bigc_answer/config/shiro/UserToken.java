package com.lin.bigc_answer.config.shiro;

import com.lin.bigc_answer.utils.UserRole;
import org.apache.shiro.authc.UsernamePasswordToken;

/*
重写UsernamePasswordToken,用于增加参数(登陆角色),便于登陆时的授权
 */
public class UserToken extends UsernamePasswordToken {
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    //登陆身份
    private UserRole userRole;

    public UserToken() {
        super();
    }

    /**
     * 以特定身份创建UsernamePasswordToken
     * @param username 用户名
     * @param password 密码
     * @param userRole 登陆角色
     */
    public UserToken(String username, String password, UserRole userRole) {
        super(username, password);
        this.userRole = userRole;
    }
}
