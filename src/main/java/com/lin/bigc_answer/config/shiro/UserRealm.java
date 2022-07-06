package com.lin.bigc_answer.config.shiro;

import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.utils.UserRole;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class UserRealm extends AuthorizingRealm {

    @Resource(name = "studentServiceImpl")
    StudentService studentService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UserToken userToken = (UserToken) authenticationToken;
        if (userToken.getUserRole() == UserRole.STUDENT) {
            //学生登陆
            Student student = studentService.queryByUserName(userToken.getUsername());
            if (student != null) {
                return new SimpleAuthenticationInfo(userToken.getUsername(), student.getPassword(), getName());
            }
        }
        //用户不存在
        return null;
    }
}
