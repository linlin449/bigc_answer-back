package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.user.Teacher;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface TeacherService extends IService<Teacher> {
    /**
     * 通过用户名查找老师
     * @param username 用户名
     * @return 查询成功返回Teacher实体类, 查询失败返回null
     */
    Teacher queryByUserName(String username);

    /**
     * 修改老师密码
     * @param username 老师用户名
     * @param newPassword 新密码
     * @return 修改成功返回真, 失败返回假, 老师不存在返回null
     */
    Boolean changePassword(String username, String newPassword);
}
