package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.question.QuestionRightAnswer;
import com.lin.bigc_answer.mapper.QuestionRightAnswerMapper;
import com.lin.bigc_answer.service.QuestionRightAnswerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-06
 */
@Service
public class QuestionRightAnswerServiceImpl extends ServiceImpl<QuestionRightAnswerMapper, QuestionRightAnswer> implements QuestionRightAnswerService {

    @Resource
    private QuestionRightAnswerMapper questionRightAnswerMapper;

    @Override
    public Boolean deleteByQuestionId(Integer questionId) {
        if (questionId == null) return false;
        LambdaQueryWrapper<QuestionRightAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionRightAnswer::getQuestionId, questionId);
        QuestionRightAnswer questionRightAnswer = questionRightAnswerMapper.selectOne(wrapper);
        if (questionRightAnswer == null) return null;
        return questionRightAnswerMapper.delete(wrapper) == 1;
    }

    @Override
    public QuestionRightAnswer getByQuestionId(Integer questionId) {
        if (questionId == null) return null;
        LambdaQueryWrapper<QuestionRightAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionRightAnswer::getQuestionId, questionId);
        return questionRightAnswerMapper.selectOne(wrapper);
    }
}
