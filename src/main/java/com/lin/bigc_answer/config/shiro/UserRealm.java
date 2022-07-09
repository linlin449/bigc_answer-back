package com.lin.bigc_answer.config.shiro;

import com.lin.bigc_answer.entity.user.Admin;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.Teacher;
import com.lin.bigc_answer.service.AdminService;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.service.TeacherService;
import com.lin.bigc_answer.service.TeacherStudentService;
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
import java.util.List;

@Slf4j(topic = "Shiro Realm")
public class UserRealm extends AuthorizingRealm {

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;
    @Resource(name = "teacherServiceImpl")
    TeacherService teacherService;
    @Resource(name = "adminServiceImpl")
    AdminService adminService;
    @Resource(name = "teacherStudentServiceImpl")
    TeacherStudentService teacherStudentService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String[] split = primaryPrincipal.split(":");
        if (split.length < 2) throw new RuntimeException("认证信息异常");
        if (split[0].equals(UserRole.STUDENT.name())) {
            //授予指定学生的操作权限
            simpleAuthorizationInfo.addStringPermission(primaryPrincipal);
        } else if (split[0].equals(UserRole.TEACHER.name())) {
            //授予老师的学生拥有的所有权限
            Teacher teacher = teacherService.queryByUserName(split[1]);
            if (teacher != null) {
                simpleAuthorizationInfo.addStringPermission(primaryPrincipal);
                List<Student> studentListByTeacherId = teacherStudentService.getStudentListByTeacherId(teacher.getId());
                for (Student student : studentListByTeacherId) {
                    simpleAuthorizationInfo.addStringPermission(UserRole.STUDENT.name() + ":" + student.getUsername());
                }
            }
        } else if (split[0].equals(UserRole.ADMIN.name())) {
            simpleAuthorizationInfo.addRole(UserRole.ADMIN.name());
            //授予所有教师权限与学生权限
            simpleAuthorizationInfo.addStringPermission(UserRole.STUDENT.name() + ":*");
            simpleAuthorizationInfo.addStringPermission(UserRole.TEACHER.name() + ":*");
        }
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
                log.info("学生 " + userToken.getUserRole().name() + " " + userToken.getUsername() + " 进行登录认证");
                return new SimpleAuthenticationInfo(userToken.getUserRole().name() + ":" + userToken.getUsername(), student.getPassword(), getName());
            }
        } else if (userToken.getUserRole() == UserRole.TEACHER) {
            Teacher teacher = teacherService.queryByUserName(userToken.getUsername());
            if (teacher != null) {
                log.info("老师 " + userToken.getUserRole().name() + " " + userToken.getUsername() + " 进行登录认证");
                return new SimpleAuthenticationInfo(userToken.getUserRole().name() + ":" + userToken.getUsername(), teacher.getPassword(), getName());
            }
        } else if (userToken.getUserRole() == UserRole.ADMIN) {
            Admin admin = adminService.queryByUserName(userToken.getUsername());
            if (admin != null) {
                log.info("管理员 " + userToken.getUserRole().name() + " " + userToken.getUsername() + " 进行登录认证");
                return new SimpleAuthenticationInfo(userToken.getUserRole().name() + ":" + userToken.getUsername(), admin.getPassword(), getName());
            }
        }
        //用户不存在
        return null;
    }
}
