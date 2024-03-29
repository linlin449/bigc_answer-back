package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.Teacher;
import com.lin.bigc_answer.entity.user.TeacherStudent;

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
     * 密码字段已隐藏
     * @param teacherId 老师ID
     * @return 学生List, 没有学生返回null
     */
    List<Student> getStudentListByTeacherId(Integer teacherId);

    /**
     * 获取指定老师的学生列表,分页展示
     * 密码字段已隐藏
     * @param teacherId 老师ID
     * @param currentPage 当前页数
     * @param pageSize 每页大小
     * @return IPage<Student>
     */
    IPage<Student> getStudentPageByTeacherId(Integer teacherId, int currentPage, int pageSize);

    /**
     * 获取学生的老师列表
     * 密码字段已隐藏
     * @param username 学生username
     * @return Teacher List,失败返回null
     */
    List<Teacher> getTeacherListByStudentUsername(String username);

    /**
     * 删除老师与学生建立的关系
     * @param teacherId 老师ID
     * @param studentId 学生ID
     * @return 删除成功返回真, 失败返回假
     */
    boolean deleteTeacherStudent(Integer teacherId, Integer studentId);

    /**
     * 通过老师ID和学生ID获取TeacherStudent实体类
     * @param teacherId 老师ID
     * @param studentId 学生ID
     * @return TeacherStudent实体类, 若获取失败返回null
     */
    TeacherStudent getByTeacherIdAndStudentId(Integer teacherId, Integer studentId);

    /**
     * 通过学生ID获取 TeacherStudent 列表
     * @param studentId 学生ID
     * @return TeacherStudent List
     */
    List<TeacherStudent> getByStudentId(Integer studentId);
}
