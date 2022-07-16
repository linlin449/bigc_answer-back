package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.config.shiro.UserToken;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.Teacher;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.CaptchaService;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.service.TeacherStudentService;
import com.lin.bigc_answer.utils.JWTUtil;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.UserRole;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;
    @Resource(name = "teacherStudentServiceImpl")
    private TeacherStudentService teacherStudentService;

    @PostMapping("/login")
    //学生登陆
    public R userLogin(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String verKey = params.get("verkey");
        String verCode = params.get("vercode");
        if (username == null || password == null || verKey == null || verCode == null) {
            return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        }
        try {
            if (!captchaService.checkVerCode(verKey, verCode)) {
                captchaService.deleteVerCode(verKey);
                return new R().fail("验证码错误", null);
            }
        } catch (RuntimeException e) {
            return new R().fail("验证码已过期", null);
        }
        Subject subject = SecurityUtils.getSubject();
        UserToken userToken = new UserToken(username, password, UserRole.STUDENT);
        captchaService.deleteVerCode(verKey);
        try {
            subject.login(userToken);
            //登陆成功,下发token
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("token", JWTUtil.createToken(username, UserRole.STUDENT));
            map.put("expire", JWTUtil.getExpireTime());
            return new R().success("登陆成功", map);
        } catch (UnknownAccountException e) {
            //用户不存在
            return new R().fail("用户名或密码错误");
        } catch (IncorrectCredentialsException e) {
            //密码错误
            return new R().fail("用户名或密码错误");
        }
    }

    @GetMapping("/info/{username}")
    public R getStudentInfo(@PathVariable("username") String username) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            List<Teacher> teacherList = teacherStudentService.getTeacherListByStudentUsername(username);
            student.hidePassword();
            Map<String, Object> map = new HashMap<>();
            map.put("student", student);
            map.put("teacherList", teacherList);
            return new R().success("success", map);
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    @PostMapping("/register")
    public R userRegister(@RequestBody Student student) {
        if (student.getUsername() == null || student.getPassword() == null ||
                student.getEmail() == null || student.getName() == null) {
            return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        }
        try {
            studentService.save(student);
        } catch (Exception e) {
            return new R().fail("服务器错误");
        }
        return new R().success("注册成功");
    }

    /**
     * 分页获取全部学生列表,默认页大小为10
     * @param pid 页码
     */
    @RequiresRoles("ADMIN")
    @GetMapping("/list/{pid}")
    public R getStudentPage(@PathVariable("pid") String pid) {
        if (!VerifyUtils.isObjectNumber(pid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        return new R().success("success", studentService.getStudentPage(Integer.parseInt(pid), 10));
    }

    @RequiresRoles("ADMIN")
    @GetMapping("/delete/{username}")
    public R deleteStudent(@PathVariable("username") String username) {
        Student student = studentService.queryByUserName(username);
        if (student != null) {
            if (studentService.deleteStudentInfo(student.getId())) {
                return new R().success("删除成功");
            }
            return new R().fail("删除失败");
        }
        return new R().fail("学生不存在,无法删除");
    }
}

