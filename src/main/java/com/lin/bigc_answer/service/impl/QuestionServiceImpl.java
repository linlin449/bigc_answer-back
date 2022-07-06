package com.lin.bigc_answer.service.impl;

import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.mapper.QuestionMapper;
import com.lin.bigc_answer.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
