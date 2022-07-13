package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.question.Question;

import java.util.List;

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
     * 判断学生某题是否答对
     * @param questionId 题目id
     * @param username 学生username
     * @return 已作答返回真, 未作答返回假,其他原因返回null
     */
    Boolean isQuestionAnswerRight(Integer questionId, String username);

    /**
     * 查询多个学生多个题目是否答对
     * @param username 学生username
     * @return Boolean集合, 若答对则为真, 答错为假, 题目不存在或其他原因则为null
     */
    List<Boolean> isQuestionListRight(List<Integer> questionIds, String username);

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

    /**
     * 根据章节ID获取题目列表
     * @param chapterId 章节ID
     * @return Question List,若章节不存在则返回null
     */
    List<Question> getQuestionListByChapterId(Integer chapterId);

    /**
     * 添加问题
     * @param question 问题实体类
     * @return 增加成功则返回主键ID, 失败返回null
     */
    Integer addQuestion(Question question);

    /**
     * 根据问题ID删除该问题及其选项和正确答案
     * @param questionId 问题ID
     * @return 删除成功返回真, 失败返回假,问题不存在返回null
     */
    Boolean deleteQuestionAndOptionById(Integer questionId);
}
