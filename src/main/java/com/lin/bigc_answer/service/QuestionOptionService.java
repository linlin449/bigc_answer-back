package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.question.QuestionOption;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-06
 */
public interface QuestionOptionService extends IService<QuestionOption> {
    /**
     * 根据问题ID列表获取问题选项列表
     * @param questionIds 问题ID列表
     * @return 问题选项列表
     */
    List<QuestionOption> getByQuestionIds(List<Integer> questionIds);

    /**
     * 根据问题ID删除该问题的选项
     * @param questionId 问题ID
     * @return 删除成功返回真, 删除失败返回假,选项不存在返回null
     */
    Boolean deleteByQuestionId(Integer questionId);
}
