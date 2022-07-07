package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.TeacherStudent;
import com.lin.bigc_answer.entity.utils.StudentDTO;
import com.lin.bigc_answer.mapper.TeacherStudentMapper;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.service.TeacherStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Service
public class TeacherStudentServiceImpl extends ServiceImpl<TeacherStudentMapper, TeacherStudent> implements TeacherStudentService {

    @Resource
    private TeacherStudentMapper teacherStudentMapper;

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;

    @Override
    public List<Student> getStudentListByTeacherId(Integer teacherId) {
        /*
         * SELECT t.student_id as id, s.name, s.username, s.email, s.phone, s.role
         * FROM teacher_student as t
         *          LEFT JOIN student s on s.id = t.student_id
         * WHERE t.teacher_id = ?;
         */

        if (teacherId == null) return null;
        LambdaQueryWrapper<TeacherStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherStudent::getTeacherId, teacherId);
        List<TeacherStudent> teacherStudents = teacherStudentMapper.selectList(wrapper);
        List<Integer> idList = new ArrayList<>();
        for (TeacherStudent teacherStudent : teacherStudents) {
            idList.add(teacherStudent.getStudentId());
        }
        return studentService.listByIds(idList);
    }

    @Override
    public IPage<Student> getStudentPageByTeacherId(Integer teacherId, int currentPage, int pageSize) {
        if (teacherId == null) return null;
        IPage<TeacherStudent> iPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<TeacherStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherStudent::getTeacherId, teacherId);
        IPage<TeacherStudent> teacherStudentIPage = teacherStudentMapper.selectPage(iPage, wrapper);
        List<TeacherStudent> teacherStudents = teacherStudentIPage.getRecords();
        List<Integer> idList = new ArrayList<>();
        for (TeacherStudent teacherStudent : teacherStudents) {
            idList.add(teacherStudent.getStudentId());
        }
        IPage<Student> studentIPage = new Page<>(currentPage, pageSize);
        studentIPage.setRecords(studentService.listByIds(idList));
        studentIPage.setTotal(iPage.getTotal());
        studentIPage.setPages(iPage.getPages());
        return studentIPage;
    }
}
