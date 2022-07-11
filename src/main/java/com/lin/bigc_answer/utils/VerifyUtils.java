package com.lin.bigc_answer.utils;

/**
 * @author th1nk
 * @date 2022/7/8 下午4:28
 */
public class VerifyUtils {
    /**
     * 判断一个Object是否为数字
     * @param obj 判断的Object
     * @return 是数字返回真, 不是数字则返回假
     */
    public static boolean isObjectNumber(Object obj) {
        try {
            Integer.parseInt(obj.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
