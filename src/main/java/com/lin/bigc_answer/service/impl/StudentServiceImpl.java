package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.AnswerDetail;
import com.lin.bigc_answer.entity.StudentFalseQuestion;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.entity.user.TeacherStudent;
import com.lin.bigc_answer.mapper.StudentMapper;
import com.lin.bigc_answer.service.AnswerDetailService;
import com.lin.bigc_answer.service.StudentFalseQuestionService;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.service.TeacherStudentService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Resource(name = "answerDetailServiceImpl")
    private AnswerDetailService answerDetailService;

    @Resource(name = "studentFalseQuestionServiceImpl")
    private StudentFalseQuestionService studentFalseQuestionService;

    @Resource(name = "teacherStudentServiceImpl")
    @Lazy
    private TeacherStudentService teacherStudentService;

    @Override
    public Student queryByUserName(String username) {
        if (username == null || username.equals("")) return null;
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getUsername, username);
        return studentMapper.selectOne(wrapper);
    }

    @Override
    public IPage<Student> getStudentPage(int currentPage, int pageSize) {
        IPage<Student> iPage = new Page<>(currentPage, pageSize);
        return studentMapper.selectPage(iPage, null);
    }

    @Override
    @Transactional
    public boolean deleteStudentInfo(Integer studentId) {
        if (studentId == null) return false;
        if (answerDetailService.selectPageByStudentId(studentId, 1, 1).getTotal() > 0) {
            if (!answerDetailService.remove(new LambdaQueryWrapper<AnswerDetail>().eq(AnswerDetail::getStudentId, studentId))) {
                return false;
            }
        }
        if (studentFalseQuestionService.selectPageByStudentId(studentId, 1, 1).getTotal() > 0) {
            if (!studentFalseQuestionService.remove(new LambdaQueryWrapper<StudentFalseQuestion>().eq(StudentFalseQuestion::getStudentId, studentId))) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        if (teacherStudentService.getByStudentId(studentId).size() > 0) {
            if (!teacherStudentService.remove(new LambdaQueryWrapper<TeacherStudent>().eq(TeacherStudent::getStudentId, studentId))) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        if (studentMapper.deleteById(studentId) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public Boolean changePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.equals("")) return false;
        Student student = queryByUserName(username);
        if (student == null) return null;
        student.setPassword(newPassword);
        return studentMapper.updateById(student) == 1;
    }
}
