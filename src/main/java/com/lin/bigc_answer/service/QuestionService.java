package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.entity.question.Question;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface QuestionService extends IService<Question> {
    /**
     * 分页获取所有题目
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return IPage<Question>
     */
    IPage<Question> getQuestionPage(int currentPage, int pageSize);

    /**
     * 根据章节ID分页获取所有题目
     * @param chapterId 章节ID
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return IPage<Question>
     */
    IPage<Question> getQuestionPageByChapter(int chapterId, int currentPage, int pageSize);
}
