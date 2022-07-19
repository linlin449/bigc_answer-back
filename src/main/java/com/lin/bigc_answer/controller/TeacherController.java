package com.lin.bigc_answer.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.config.shiro.UserToken;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.Teacher;
import com.lin.bigc_answer.entity.user.TeacherStudent;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.CaptchaService;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.service.TeacherService;
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
@RequestMapping("/teacher")
public class TeacherController {
    @Resource(name = "teacherServiceImpl")
    private TeacherService teacherService;

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;
    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

    @Resource(name = "teacherStudentServiceImpl")
    private TeacherStudentService teacherStudentService;

    @PostMapping("/login")
    //老师登陆
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
        UserToken userToken = new UserToken(username, password, UserRole.TEACHER);
        captchaService.deleteVerCode(verKey);
        try {
            subject.login(userToken);
            //登陆成功,下发token
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("token", JWTUtil.createToken(username, UserRole.TEACHER));
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

    /**
     * 根据用户名获取老师信息
     * @param username 老师username
     */
    @GetMapping("/info/{username}")
    public R getTeacherInfo(@PathVariable("username") String username) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.TEACHER.name() + ":" + username)) {
            Teacher teacher = teacherService.queryByUserName(username);
            teacher.hidePassword();
            return new R().success("success", teacher);
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 分页获取老师的学生列表,默认页大小为10
     * @param username 老师username
     * @param page 页码
     */
    @GetMapping("/students/{username}/list/{page}")
    public R getStudentList(@PathVariable("username") String username, @PathVariable("page") String page) {
        if (!VerifyUtils.isObjectNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.TEACHER.name() + ":" + username)) {
            Teacher teacher = teacherService.queryByUserName(username);
            if (teacher != null) {
                IPage<Student> studentIPage = teacherStudentService.getStudentPageByTeacherId(teacher.getId(), Integer.parseInt(page), 10);
                return new R().success("success", studentIPage);
            }
            return new R().fail("老师不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 将某学生从老师的学生列表中删除
     * @param sid 学生ID
     * @param username 老师username
     */
    @GetMapping("/{username}/deletestudent/{sid}")
    public R deleteStudentFromTeacherStudent(@PathVariable("sid") String sid, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(sid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.TEACHER.name() + ":" + username)) {
            Student student = studentService.getById(sid);
            Teacher teacher = teacherService.queryByUserName(username);
            if (student == null || teacher == null) return new R().fail("信息有误,无法删除");
            if (subject.isPermitted(UserRole.STUDENT.name() + ":" + student.getUsername())) {
                if (teacherStudentService.deleteTeacherStudent(teacher.getId(), student.getId())) {
                    return new R().success("删除成功");
                }
                return new R().fail("删除失败");
            }
            return new R().fail("Ta不是你的学生,无法删除");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 将某学生加入老师的学生列表
     * @param sid 学生ID
     * @param username 老师username
     */
    @GetMapping("/{username}/addstudent/id/{sid}")
    public R addStudentToTeacherStudentById(@PathVariable("sid") String sid, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(sid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.TEACHER.name() + ":" + username)) {
            Student student = studentService.getById(sid);
            Teacher teacher = teacherService.queryByUserName(username);
            if (student == null || teacher == null) return new R().fail("信息有误,无法添加");
            if (teacherStudentService.getByTeacherIdAndStudentId(teacher.getId(), student.getId()) == null) {
                TeacherStudent teacherStudent = new TeacherStudent();
                teacherStudent.setTeacherId(teacher.getId());
                teacherStudent.setStudentId(student.getId());
                if (teacherStudentService.save(teacherStudent)) {
                    return new R().success("添加成功");
                }
                return new R().fail("添加失败");
            }
            return new R().fail("信息已存在,请勿重复添加");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 将某学生加入老师的学生列表
     * @param sUsername 学生username
     * @param tUsername 老师username
     */
    @GetMapping("/{tUsername}/addstudent/username/{sUsername}")
    public R addStudentToTeacherStudentByUsername(@PathVariable("sUsername") String sUsername, @PathVariable("tUsername") String tUsername) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.TEACHER.name() + ":" + tUsername)) {
            Student student = studentService.queryByUserName(sUsername);
            Teacher teacher = teacherService.queryByUserName(tUsername);
            if (student == null || teacher == null) return new R().fail("信息有误,无法添加");
            if (teacherStudentService.getByTeacherIdAndStudentId(teacher.getId(), student.getId()) == null) {
                TeacherStudent teacherStudent = new TeacherStudent();
                teacherStudent.setTeacherId(teacher.getId());
                teacherStudent.setStudentId(student.getId());
                if (teacherStudentService.save(teacherStudent)) {
                    return new R().success("添加成功");
                }
                return new R().fail("添加失败");
            }
            return new R().fail("信息已存在,请勿重复添加");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 获取老师列表
     */
    @RequiresRoles("ADMIN")
    @GetMapping("/list")
    public R getTeacherList() {
        List<Teacher> teacherList = teacherService.list();
        for (Teacher teacher : teacherList) {
            teacher.hidePassword();
        }
        return new R().success("success", teacherList);
    }

    /**
     * 修改老师个人信息
     * @param teacher 老师实体类
     */
    @RequiresRoles("ADMIN")
    @PutMapping("/update")
    public R updateTeacher(@RequestBody Teacher teacher) {
        if (teacher.getId() == null) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        teacher.setPassword(null);
        teacher.setRole(null);
        if (teacherService.updateById(teacher)) {
            return new R().success("修改成功");
        }
        return new R().fail("修改失败");
    }

    /**
     * 通过老师ID删除老师
     * @param tid 老师ID
     */
    @RequiresRoles("ADMIN")
    @GetMapping("/delete/{tid}")
    public R deleteTeacher(@PathVariable("tid") String tid) {
        if (!VerifyUtils.isObjectNumber(tid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        if (teacherService.getById(tid) != null) {
            IPage<Student> studentIPage = teacherStudentService.getStudentPageByTeacherId(Integer.valueOf(tid), 1, 1);
            if (studentIPage.getTotal() == 0) {
                if (teacherService.removeById(tid)) {
                    return new R().success("删除成功");
                }
                return new R().fail("删除失败");
            }
            return new R().fail("该老师拥有学生,请先删除师生关系");
        }
        return new R().fail("老师不存在,无法删除");
    }
}

