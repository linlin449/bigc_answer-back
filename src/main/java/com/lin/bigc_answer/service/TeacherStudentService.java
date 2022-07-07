package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.Teacher;
import com.lin.bigc_answer.entity.user.TeacherStudent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface TeacherStudentService extends IService<TeacherStudent> {
    /**
     * 获取指定老师的学生列表
     * @param teacherId 老师ID
     * @return 学生List
     */
    List<Student> getStudentListByTeacherId(Integer teacherId);

    /**
     * 获取指定老师的学生列表,分页展示
     * @param teacherId 老师ID
     * @param currentPage 当前页数
     * @param pageSize 每页大小
     * @return IPage<Student>
     */
    IPage<Student> getStudentPageByTeacherId(Integer teacherId, int currentPage, int pageSize);
}
