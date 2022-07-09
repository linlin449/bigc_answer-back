package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.question.Question;

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
     * @param subjectId 章节ID
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return IPage<Question>
     */
    IPage<Question> getQuestionPageBySubject(int subjectId, int currentPage, int pageSize);

    /**
     * 获取学生答过的题目
     * @param studentId 学生id
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return IPage<Question>
     */
    IPage<Question> getAnsweredPageByStudentId(Integer studentId, int currentPage, int pageSize);

    /**
     * 获取学生没有答过的题目
     * @param studentId 学生id
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return IPage<Question>
     */
    IPage<Question> getUnansweredPageByStudentId(Integer studentId, int currentPage, int pageSize);

    /**
     * 判断学生是否答过某题
     * @param questionId 题目id
     * @param username 学生username
     * @return 已作答返回真, 未作答或其他原因返回假
     */
    boolean isQuestionAnswered(Integer questionId, String username);

    /**
     * 获取某学生的错题,分页展示
     * @param studentId 学生id
     * @param currentPage 页码
     * @param pageSize 页大小
     * @return IPage<Question>
     */
    IPage<Question> getWrongPageByStudentId(Integer studentId, int currentPage, int pageSize);

    /**
     * 获取某学生答对的题目,分页展示
     * @param studentId 学生id
     * @param currentPage 页码
     * @param pageSize 页大小
     * @return IPage<Question>
     */
    IPage<Question> getRightPageByStudentId(Integer studentId, int currentPage, int pageSize);
}
