package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.question.QuestionRightAnswer;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-06
 */
public interface QuestionRightAnswerService extends IService<QuestionRightAnswer> {
    /**
     * 根据问题ID删除该问题的正确答案
     * @param questionId 问题ID
     * @return 删除成功返回真, 删除失败返回假, 答案不存在返回null
     */
    Boolean deleteByQuestionId(Integer questionId);
}
