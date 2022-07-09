package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.question.Chapter;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface ChapterService extends IService<Chapter> {
    /**
     * 通过subject ID 获取章节列表
     * @param subjectId subjectID
     * @return 章节list
     */
    List<Chapter> getListBySubjectId(Integer subjectId);
}
