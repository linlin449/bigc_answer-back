package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.mapper.QuestionMapper;
import com.lin.bigc_answer.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Resource
    QuestionMapper questionMapper;

    @Override
    public IPage<Question> getQuestionPage(int currentPage, int pageSize) {
        IPage<Question> iPage = new Page<>(currentPage, pageSize);
        return questionMapper.selectPage(iPage, null);
    }

    @Override
    public IPage<Question> getQuestionPageByChapter(int chapterId, int currentPage, int pageSize) {
        IPage<Question> iPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getChapterId, chapterId);
        return questionMapper.selectPage(iPage, wrapper);
    }
}
