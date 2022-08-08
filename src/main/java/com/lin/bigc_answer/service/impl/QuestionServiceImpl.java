package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.AnswerDetail;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.mapper.QuestionMapper;
import com.lin.bigc_answer.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    private QuestionMapper questionMapper;
    @Resource(name = "answerDetailServiceImpl")
    private AnswerDetailService answerDetailService;
    @Resource(name = "studentServiceImpl")
    private StudentService studentService;
    @Resource(name = "questionOptionServiceImpl")
    private QuestionOptionService questionOptionService;

    @Resource(name = "questionRightAnswerServiceImpl")
    private QuestionRightAnswerService questionRightAnswerService;

    @Resource(name = "studentFalseQuestionServiceImpl")
    private StudentFalseQuestionService studentFalseQuestionService;

    @Override
    public IPage<Question> getQuestionPage(int currentPage, int pageSize) {
        IPage<Question> iPage = new Page<>(currentPage, pageSize);
        return questionMapper.selectPage(iPage, null);
    }

    @Override
    public List<Question> getQuestionListByIdList(List<Integer> idList) {
        List<Question> result = new ArrayList<>();
        for (Integer id : idList) {
            if (id != null) {
                result.add(questionMapper.selectById(id));
                continue;
            }
            result.add(null);
        }
        return result;
    }

    @Override
    public IPage<Question> getQuestionPageByChapter(int chapterId, int currentPage, int pageSize) {
        IPage<Question> iPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getChapterId, chapterId);
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
        if (answerDetailIPage.getTotal() == 0) return getQuestionPage(currentPage, pageSize);
        List<AnswerDetail> records = answerDetailIPage.getRecords();
        List<Integer> questionId = new ArrayList<>();
        for (AnswerDetail record : records) {
            questionId.add(record.getQuestionId());
        }
        return questionMapper.selectPage(new Page<>(currentPage, pageSize), new LambdaQueryWrapper<Question>().notIn(Question::getId, questionId));
    }

    @Override
    public Boolean isQuestionAnswerRight(Integer questionId, String username) {
        Student student = studentService.queryByUserName(username);
        if (student != null) {
            AnswerDetail answerDetail = answerDetailService.getByQuestionIdAndStudentId(questionId, student.getId());
            if (answerDetail == null || answerDetail.getIsRight() == null) return null;
            return answerDetail.getIsRight() == 1;
        }
        return null;
    }

    @Override
    public List<Boolean> isQuestionListRight(List<Integer> questionIds, String username) {
        List<Boolean> result = new ArrayList<>();
        Student student = studentService.queryByUserName(username);
        for (Integer questionId : questionIds) {
            if (questionId != null && student != null) {
                AnswerDetail answerDetail = answerDetailService.getByQuestionIdAndStudentId(questionId, student.getId());
                if (answerDetail == null) {
                    result.add(null);
                } else {
                    result.add(answerDetail.getIsRight() == 1);
                }
                continue;
            }
            result.add(null);
        }
        return result;
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
        questionIPage.setPages(answerDetailIPage.getPages());
        questionIPage.setTotal(answerDetailIPage.getTotal());
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
        questionIPage.setPages(answerDetailIPage.getPages());
        questionIPage.setTotal(answerDetailIPage.getTotal());
        questionIPage.setCurrent(currentPage);
        questionIPage.setSize(pageSize);
        return questionIPage;
    }

    @Override
    public List<Question> getQuestionListByChapterId(Integer chapterId) {
        if (chapterId == null) return null;
        return questionMapper.selectList(new LambdaQueryWrapper<Question>().eq(Question::getChapterId, chapterId));
    }

    @Override
    public Integer addQuestion(Question question) {
        return questionMapper.insert(question) == 1 ? question.getId() : null;
    }

    @Transactional
    @Override
    public Boolean deleteQuestionAndOptionById(Integer questionId) {
        if (questionId == null) return false;
        if (questionMapper.selectById(questionId) == null) return null;
        if (questionRightAnswerService.deleteByQuestionId(questionId) != Boolean.FALSE
                && questionOptionService.deleteByQuestionId(questionId) != Boolean.FALSE
                && studentFalseQuestionService.deleteByQuestionId(questionId) != Boolean.FALSE
                && answerDetailService.deleteByQuestionId(questionId) != Boolean.FALSE) {
            if (questionMapper.deleteById(questionId) == 1) {
                return true;
            }
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }
}
