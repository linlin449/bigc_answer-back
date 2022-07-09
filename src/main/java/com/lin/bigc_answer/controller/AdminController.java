package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.config.shiro.UserToken;
import com.lin.bigc_answer.entity.user.Admin;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.AdminService;
import com.lin.bigc_answer.service.CaptchaService;
import com.lin.bigc_answer.utils.JWTUtil;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.UserRole;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
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
@RequestMapping("/lin.bigc_answer/admin")
public class AdminController {
    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "captchaServiceImpl")
    private CaptchaService captchaService;

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
        UserToken userToken = new UserToken(username, password, UserRole.ADMIN);
        try {
            subject.login(userToken);
            captchaService.deleteVerCode(verKey);
            //登陆成功,下发token
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("token", JWTUtil.createToken(username, UserRole.ADMIN));
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
        if (subject.isPermitted(UserRole.ADMIN.name() + ":" + username)) {
            Admin admin = adminService.queryByUserName(username);
            admin.setPassword("******");
            return new R().success("success", admin);
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }
}

