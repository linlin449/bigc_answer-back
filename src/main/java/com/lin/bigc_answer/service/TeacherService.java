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
}
