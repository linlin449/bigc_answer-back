package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.StudentFalseQuestion;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface StudentFalseQuestionService extends IService<StudentFalseQuestion> {
    /**
     * 根据学生id分页获取学生错题
     * @param studentId 学生ID
     * @param currentPage 页码
     * @param pageSize 页大小
     * @return IPage<StudentFalseQuestion>
     */
    IPage<StudentFalseQuestion> selectPageByStudentId(Integer studentId, int currentPage, int pageSize);

    /**
     * 根据学生ID分页获取其收藏的题目
     * @param studentId 学生ID
     * @param currentPage 页码
     * @param pageSize 页大小
     * @return IPage<StudentFalseQuestion>
     */
    IPage<StudentFalseQuestion> selectLikePageByStudentId(Integer studentId, int currentPage, int pageSize);
}
