package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.AnswerDetail;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.mapper.QuestionMapper;
import com.lin.bigc_answer.service.AnswerDetailService;
import com.lin.bigc_answer.service.QuestionService;
import com.lin.bigc_answer.service.StudentService;
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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Resource
    QuestionMapper questionMapper;
    @Resource(name = "answerDetailServiceImpl")
    AnswerDetailService answerDetailService;
    @Resource(name = "studentServiceImpl")
    StudentService studentService;

    @Override
    public IPage<Question> getQuestionPage(int currentPage, int pageSize) {
        IPage<Question> iPage = new Page<>(currentPage, pageSize);
        return questionMapper.selectPage(iPage, null);
    }

    @Override
    public IPage<Question> getQuestionPageBySubject(int subjectId, int currentPage, int pageSize) {
        IPage<Question> iPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getSubjectId, subjectId);
        return questionMapper.selectPage(iPage, wrapper);
    }

    @Override
    public IPage<Question> getAnsweredPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<AnswerDetail> answerDetailIPage = answerDetailService.selectPageByStudentId(studentId, currentPage, pageSize);
        List<AnswerDetail> records = answerDetailIPage.getRecords();
        List<Integer> questionId = new ArrayList<>();
        for (AnswerDetail record : records) {
            questionId.add(record.getQuestionId());
        }
        IPage<Question> questionIPage = new Page<>();
        if (questionId.size() > 0) {
            questionIPage.setRecords(questionMapper.selectBatchIds(questionId));
        }
        questionIPage.setPages(answerDetailIPage.getPages());
        questionIPage.setTotal(answerDetailIPage.getTotal());
        questionIPage.setCurrent(currentPage);
        questionIPage.setSize(pageSize);
        return questionIPage;
    }

    @Override
    public IPage<Question> getUnansweredPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<AnswerDetail> answerDetailIPage = answerDetailService.selectPageByStudentId(studentId, currentPage, pageSize);
        List<AnswerDetail> records = answerDetailIPage.getRecords();
        List<Integer> questionId = new ArrayList<>();
        for (AnswerDetail record : records) {
            questionId.add(record.getQuestionId());
        }
        IPage<Question> questionIPage = new Page<>();
        if (questionId.size() > 0) {
            questionIPage.setRecords(questionMapper.selectList(new LambdaQueryWrapper<Question>().notIn(Question::getId, questionId)));
        }
        questionIPage.setPages(answerDetailIPage.getPages());
        questionIPage.setTotal(answerDetailIPage.getTotal());
        questionIPage.setCurrent(currentPage);
        questionIPage.setSize(pageSize);
        return questionIPage;
    }

    @Override
    public boolean isQuestionAnswered(Integer questionId, String username) {
        Student student = studentService.queryByUserName(username);
        if (student != null) {
            AnswerDetail answerDetail = answerDetailService.getByQuestionIdAndStudentId(questionId, student.getId());
            return answerDetail != null;
        }
        return false;
    }

    @Override
    public IPage<Question> getWrongPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<AnswerDetail> answerDetailIPage = answerDetailService.getWrongPageByStudentId(studentId, currentPage, pageSize);
        List<AnswerDetail> answerDetailList = answerDetailIPage.getRecords();
        List<Integer> questionId = new ArrayList<>();
        for (AnswerDetail answerDetail : answerDetailList) {
            questionId.add(answerDetail.getQuestionId());
        }
        IPage<Question> questionIPage = new Page<>();
        if (questionId.size() > 0) {
            questionIPage.setRecords(questionMapper.selectBatchIds(questionId));
        }
        questionIPage.setPages(questionIPage.getPages());
        questionIPage.setTotal(questionIPage.getTotal());
        questionIPage.setCurrent(currentPage);
        questionIPage.setSize(pageSize);
        return questionIPage;
    }

    @Override
    public IPage<Question> getRightPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<AnswerDetail> answerDetailIPage = answerDetailService.getRightPageByStudentId(studentId, currentPage, pageSize);
        List<AnswerDetail> answerDetailList = answerDetailIPage.getRecords();
        List<Integer> questionId = new ArrayList<>();
        for (AnswerDetail answerDetail : answerDetailList) {
            questionId.add(answerDetail.getQuestionId());
        }
        IPage<Question> questionIPage = new Page<>();
        if (questionId.size() > 0) {
            questionIPage.setRecords(questionMapper.selectBatchIds(questionId));
        }
        questionIPage.setPages(questionIPage.getPages());
        questionIPage.setTotal(questionIPage.getTotal());
        questionIPage.setCurrent(currentPage);
        questionIPage.setSize(pageSize);
        return questionIPage;
    }

    @Override
    public List<Question> getQuestionListByChapterId(Integer chapterId) {
        if (chapterId == null) return null;
        return questionMapper.selectList(new LambdaQueryWrapper<Question>().eq(Question::getChapterId, chapterId));
    }
}
