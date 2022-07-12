package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.question.Subject;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface SubjectService extends IService<Subject> {
    /**
     * 根据专业ID获取课程列表
     * @param majorId 专业ID
     */
    List<Subject> getByMajorId(Integer majorId);
}
