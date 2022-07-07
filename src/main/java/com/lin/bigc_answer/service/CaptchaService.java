package com.lin.bigc_answer.service;

/**
 * @author th1nk
 * @date 2022/7/7 下午3:02
 */
public interface CaptchaService {
    /**
     * 检查验证码
     * @param verKey 验证key
     * @param verCode 验证值
     * @return 正确返回真, 错误返回假, 若验证码已过期则抛出异常
     */
    boolean checkVerCode(String verKey, String verCode);
}
