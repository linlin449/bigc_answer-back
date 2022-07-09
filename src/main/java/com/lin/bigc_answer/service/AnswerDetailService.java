package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.AnswerDetail;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface AnswerDetailService extends IService<AnswerDetail> {
    /**
     * 根据学生ID获取答题详细,分页展示
     * @param studentId 学生id
     * @param currentPage 页码
     * @param pageSize 页大小
     * @return IPage<AnswerDetail>
     */
    IPage<AnswerDetail> selectPageByStudentId(Integer studentId, int currentPage, int pageSize);

    /**
     * 根据问题ID和学生ID获取AnswerDetail
     * @param questionId 问题ID
     * @param studentId 学生ID
     * @return AnswerDetail实体类
     */
    AnswerDetail getByQuestionIdAndStudentId(Integer questionId, Integer studentId);

    /**
     * 分页获取学生答对的AnswerDetail
     * @param studentId 学生ID
     * @param currentPage 页码
     * @param pageSize 页大小
     * @return IPage<AnswerDetail>
     */
    IPage<AnswerDetail> getRightPageByStudentId(Integer studentId, int currentPage, int pageSize);

    /**
     * 分页获取学生答错的AnswerDetail
     * @param studentId 学生ID
     * @param currentPage 页码
     * @param pageSize 页大小
     * @return IPage<AnswerDetail>
     */
    IPage<AnswerDetail> getWrongPageByStudentId(Integer studentId, int currentPage, int pageSize);
}
