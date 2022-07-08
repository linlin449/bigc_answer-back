package com.lin.bigc_answer;

import com.lin.bigc_answer.entity.user.Admin;
import com.lin.bigc_answer.mapper.TeacherStudentMapper;
import com.lin.bigc_answer.service.AdminService;
import com.lin.bigc_answer.service.TeacherStudentService;
import com.lin.bigc_answer.utils.JWTUtil;
import com.lin.bigc_answer.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BigcAnswerApplicationTests {
    @Resource
    private AdminService adminService;
    @Resource
    TeacherStudentService teacherStudentService;
    @Resource
    TeacherStudentMapper teacherStudentMapper;

    @Test
    public void admin_queryByUserName() {
        Admin admin = adminService.queryByUserName("10001");
        System.out.println(admin);
    }

    @Test
    public void jtwTest() {
        String token = JWTUtil.createToken("th1nk", UserRole.STUDENT);
        System.out.println("token===>" + token);
        System.out.println("UserName===>" + JWTUtil.getUserName(token));
        System.out.println("UserRole===>" + JWTUtil.getUserRole(token));
    }
}
