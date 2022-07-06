package com.lin.bigc_answer.service;

import com.lin.bigc_answer.entity.user.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface StudentService extends IService<Student> {
    /**
     * 通过用户名查找学生
     * @param username 用户名
     * @return 查询成功返回Student实体类, 查询失败返回null
     */
    Student queryByUserName(String username);
}
