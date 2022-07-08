package com.lin.bigc_answer.utils;

/**
 * @author th1nk
 * @date 2022/7/8 下午4:28
 */
public class VerifyUtils {
    /**
     * 判断一个字符串是否为数字
     * @param str 判断的字符串
     * @return 是数字返回真, 不是数字则返回假
     */
    public static boolean isStrNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
