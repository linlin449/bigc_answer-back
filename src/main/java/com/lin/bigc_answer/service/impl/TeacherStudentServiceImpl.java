package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.Teacher;
import com.lin.bigc_answer.entity.user.TeacherStudent;
import com.lin.bigc_answer.mapper.TeacherStudentMapper;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.service.TeacherService;
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

    @Resource(name = "teacherServiceImpl")
    private TeacherService teacherService;
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
        if (idList.size() > 0) {
            List<Student> students = studentService.listByIds(idList);
            //隐藏密码
            for (Student student : students) {
                student.hidePassword();
            }
            return students;
        }
        return null;
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
        if (idList.size() > 0) {
            //隐藏密码
            List<Student> students = studentService.listByIds(idList);
            for (Student student : students) {
                student.hidePassword();
            }
            studentIPage.setRecords(students);
        }
        studentIPage.setTotal(iPage.getTotal());
        studentIPage.setPages(iPage.getPages());
        return studentIPage;
    }

    @Override
    public List<Teacher> getTeacherListByStudentUsername(String username) {
        Student student = studentService.queryByUserName(username);
        if (student != null) {
            List<TeacherStudent> teacherStudents = teacherStudentMapper.selectList(new LambdaQueryWrapper<TeacherStudent>().eq(TeacherStudent::getStudentId, student.getId()));
            List<Integer> teacherId = new ArrayList<>();
            for (TeacherStudent teacherStudent : teacherStudents) {
                teacherId.add(teacherStudent.getTeacherId());
            }
            if (teacherId.size() > 0) {
                List<Teacher> teachers = teacherService.listByIds(teacherId);
                //隐藏密码
                for (Teacher teacher : teachers) {
                    teacher.hidePassword();
                }
                return teachers;
            }
        }
        return null;
    }

    @Override
    public boolean deleteTeacherStudent(Integer teacherId, Integer studentId) {
        if (teacherId == null || studentId == null) return false;
        LambdaQueryWrapper<TeacherStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherStudent::getStudentId, studentId).eq(TeacherStudent::getTeacherId, teacherId);
        return teacherStudentMapper.delete(wrapper) == 1;
    }

    @Override
    public TeacherStudent getByTeacherIdAndStudentId(Integer teacherId, Integer studentId) {
        if (teacherId == null || studentId == null) return null;
        LambdaQueryWrapper<TeacherStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherStudent::getStudentId, studentId).eq(TeacherStudent::getTeacherId, teacherId);
        return teacherStudentMapper.selectOne(wrapper);
    }

    @Override
    public List<TeacherStudent> getByStudentId(Integer studentId) {
        if (studentId == null) return null;
        LambdaQueryWrapper<TeacherStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherStudent::getStudentId, studentId);
        return teacherStudentMapper.selectList(wrapper);
    }
}
