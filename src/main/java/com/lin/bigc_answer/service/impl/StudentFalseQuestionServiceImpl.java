package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.StudentFalseQuestion;
import com.lin.bigc_answer.mapper.StudentFalseQuestionMapper;
import com.lin.bigc_answer.service.StudentFalseQuestionService;
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
    StudentFalseQuestionMapper studentFalseQuestionMapper;

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
}
