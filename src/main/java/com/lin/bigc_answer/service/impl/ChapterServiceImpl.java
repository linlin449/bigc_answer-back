package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.question.Chapter;
import com.lin.bigc_answer.mapper.ChapterMapper;
import com.lin.bigc_answer.service.ChapterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    @Override
    public List<Chapter> getListBySubjectId(Integer subjectId) {
        if (subjectId == null) return null;
        return chapterMapper.selectList(new LambdaQueryWrapper<Chapter>().eq(Chapter::getSubjectId, subjectId));
    }
}
