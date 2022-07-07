package com.lin.bigc_answer.config.shiro;

import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.utils.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

@Slf4j(topic = "Shiro Realm")
public class UserRealm extends AuthorizingRealm {

    @Resource(name = "studentServiceImpl")
    StudentService studentService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermission(primaryPrincipal + ":*");
        log.info("身份授权 " + primaryPrincipal);
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UserToken userToken = (UserToken) authenticationToken;
        if (userToken.getUserRole() == UserRole.STUDENT) {
            //学生登陆
            Student student = studentService.queryByUserName(userToken.getUsername());
            if (student != null) {
                log.info(userToken.getUserRole().name() + " " + userToken.getUsername() + " 进行登录认证");
                return new SimpleAuthenticationInfo(userToken.getUserRole().name() + ":" + userToken.getUsername(), student.getPassword(), getName());
            }
        }
        //用户不存在
        return null;
    }
}
