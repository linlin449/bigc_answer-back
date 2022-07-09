package com.lin.bigc_answer.controller;

import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.RedisUtils;
import com.wf.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "Captcha")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Resource(name = "redisUtils")
    private RedisUtils redisUtil;

    @RequestMapping("/getcaptcha")
    public R captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为5分钟
        redisUtil.setEx("captcha:" + key, verCode, 5, TimeUnit.MINUTES);
        // 将key和base64返回给前端
        Map<String, String> res = new HashMap<>();
        res.put("key", key);
        res.put("image", specCaptcha.toBase64());
        log.info("验证码生成 Key = " + key + " verCode = " + verCode);
        return new R().success("success", res);
    }

    //TODO 此接口用于开发,生产环境应删除该接口
    @Deprecated
    @GetMapping("/value/{key}")
    private R getValue(@PathVariable("key") String key) {
        log.warn("用于开发环境的接口正在被使用,如为生产环境,请尽快删除该接口");
        if (redisUtil.hasKey("captcha:" + key)) {
            return new R().success(redisUtil.get("captcha:" + key));
        }
        return new R().fail("验证码过期");
    }
}
