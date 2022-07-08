package com.lin.bigc_answer.service.impl;

import com.lin.bigc_answer.service.CaptchaService;
import com.lin.bigc_answer.utils.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author th1nk
 * @date 2022/7/7 下午3:06
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean checkVerCode(String verKey, String verCode) throws RuntimeException {
        if (redisUtils.hasKey("captcha:" + verKey)) {
            return redisUtils.get("captcha:" + verKey).equals(verCode);
        }
        throw new RuntimeException("验证码已过期");
    }

    @Override
    public void deleteVerCode(String verKey) {
        if (redisUtils.hasKey("captcha:" + verKey)) {
            redisUtils.delete("captcha:" + verKey);
        }
    }
}
