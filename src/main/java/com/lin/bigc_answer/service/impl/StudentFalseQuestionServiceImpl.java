package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.StudentFalseQuestion;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.mapper.StudentFalseQuestionMapper;
import com.lin.bigc_answer.service.StudentFalseQuestionService;
import com.lin.bigc_answer.service.StudentService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
public class StudentFalseQuestionServiceImpl extends ServiceImpl<StudentFalseQuestionMapper, StudentFalseQuestion> implements StudentFalseQuestionService {

    @Resource
    private StudentFalseQuestionMapper studentFalseQuestionMapper;

    @Resource(name = "studentServiceImpl")
    @Lazy
    private StudentService studentService;

    @Override
    public IPage<StudentFalseQuestion> selectPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<StudentFalseQuestion> iPage = new Page<>(currentPage, pageSize);
        return studentFalseQuestionMapper.selectPage(iPage, new LambdaQueryWrapper<StudentFalseQuestion>().eq(StudentFalseQuestion::getStudentId, studentId));
    }

    @Override
    public IPage<StudentFalseQuestion> selectLikePageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<StudentFalseQuestion> iPage = new Page<>(currentPage, pageSize);
        return studentFalseQuestionMapper.selectPage(iPage, new LambdaQueryWrapper<StudentFalseQuestion>().eq(StudentFalseQuestion::getStudentId, studentId).eq(StudentFalseQuestion::getIsLike, true));
    }

    @Override
    public Integer getCountByUsername(String username) {
        Student student = studentService.queryByUserName(username);
        if (student == null) return null;
        return studentFalseQuestionMapper.selectCount(new LambdaQueryWrapper<StudentFalseQuestion>().eq(StudentFalseQuestion::getStudentId, student.getId()));
    }

    @Override
    public Boolean deleteByQuestionIdAndUsername(Integer questionId, Integer studentId) {
        if (questionId == null || studentId == null) return null;
        LambdaQueryWrapper<StudentFalseQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentFalseQuestion::getQuestionId, questionId);
        wrapper.eq(StudentFalseQuestion::getStudentId, studentId);
        StudentFalseQuestion studentFalseQuestion = studentFalseQuestionMapper.selectOne(wrapper);
        if (studentFalseQuestion == null) return null;
        return studentFalseQuestionMapper.delete(wrapper) == 1;
    }

    @Override
    public StudentFalseQuestion getByQuestionIdAndUserId(Integer questionId, Integer studentId) {
        if (questionId == null || studentId == null) return null;
        LambdaQueryWrapper<StudentFalseQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentFalseQuestion::getQuestionId, questionId);
        wrapper.eq(StudentFalseQuestion::getStudentId, studentId);
        return studentFalseQuestionMapper.selectOne(wrapper);
    }
}
