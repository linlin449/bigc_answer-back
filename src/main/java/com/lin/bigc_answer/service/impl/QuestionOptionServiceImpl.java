package com.lin.bigc_answer.service.impl;

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
    public List<QuestionOption> getByQuestionIds(List<Integer> questionIds) {
        List<QuestionOption> questionOptions = new LinkedList<>();
        for (Integer questionId : questionIds) {
            if (questionId != null) {
                questionOptions.add(questionOptionMapper.selectById(questionId));
                continue;
            }
            questionOptions.add(null);
        }
        return questionOptions;
    }
}
