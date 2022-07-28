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
    @Deprecated
    IPage<StudentFalseQuestion> selectLikePageByStudentId(Integer studentId, int currentPage, int pageSize);

    /**
     * 根据学生用户名获取收藏题目数量
     * @param username 学生username
     * @return 学生不存在返回null, 否则返回收藏题目数量
     */
    Integer getCountByUsername(String username);

    /**
     * 根据问题ID和学生id删除收藏题目
     * @param questionId 问题ID
     * @param studentId 学生Id
     * @return 删除成功返回真, 失败返回假, 收藏不存在返回null
     */
    Boolean deleteByQuestionIdAndUsername(Integer questionId, Integer studentId);

    /**
     * 根据题目ID和学生id获取收藏题目实体类
     * @param questionId 问题ID
     * @return 获取成功返回实体类, 失败返回null
     */
    StudentFalseQuestion getByQuestionIdAndUserId(Integer questionId, Integer studentId);
}
