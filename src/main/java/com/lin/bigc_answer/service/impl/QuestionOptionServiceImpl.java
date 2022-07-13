package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.question.QuestionOption;
import com.lin.bigc_answer.mapper.QuestionOptionMapper;
import com.lin.bigc_answer.service.QuestionOptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-06
 */
@Service
public class QuestionOptionServiceImpl extends ServiceImpl<QuestionOptionMapper, QuestionOption> implements QuestionOptionService {

    @Resource
    private QuestionOptionMapper questionOptionMapper;


    @Override
    public QuestionOption getByQuestionId(Integer questionId) {
        if (questionId == null) return null;
        LambdaQueryWrapper<QuestionOption> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionOption::getQuestionId, questionId);
        return questionOptionMapper.selectOne(wrapper);
    }

    @Override
    public List<QuestionOption> getByQuestionIdList(List<Integer> questionIds) {
        List<QuestionOption> questionOptions = new LinkedList<>();
        for (Integer questionId : questionIds) {
            if (questionId != null) {
                LambdaQueryWrapper<QuestionOption> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(QuestionOption::getQuestionId, questionId);
                questionOptions.add(questionOptionMapper.selectOne(wrapper));
                continue;
            }
            questionOptions.add(null);
        }
        return questionOptions;
    }

    @Override
    public Boolean deleteByQuestionId(Integer questionId) {
        if (questionId == null) return false;
        LambdaQueryWrapper<QuestionOption> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionOption::getQuestionId, questionId);
        QuestionOption questionOption = questionOptionMapper.selectOne(wrapper);
        if (questionOption == null) return null;
        return questionOptionMapper.delete(wrapper) == 1;
    }
}
