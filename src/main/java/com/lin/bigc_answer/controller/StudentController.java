package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.config.shiro.UserToken;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.impl.CaptchaServiceImpl;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.UserRole;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource(name = "CaptchaServiceImpl")
    private CaptchaServiceImpl captchaService;

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
        //TODO 获取验证码并进行验证
        try {
            if (!captchaService.checkVerCode(verKey, verCode)) {
                return new R().fail("验证码错误", null);
            }
        } catch (RuntimeException e) {
            return new R().fail("验证码已过期", null);
        }
        Subject subject = SecurityUtils.getSubject();
        UserToken userToken = new UserToken(username, password, UserRole.STUDENT);
        try {
            subject.login(userToken);
            return new R().success("登陆成功");
        } catch (UnknownAccountException e) {
            //用户不存在
            return new R().fail("用户名或密码错误");
        } catch (IncorrectCredentialsException e) {
            //密码错误
            return new R().fail("用户名或密码错误");
        }
    }
}

