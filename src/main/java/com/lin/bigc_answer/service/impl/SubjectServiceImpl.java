package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.question.Subject;
import com.lin.bigc_answer.mapper.SubjectMapper;
import com.lin.bigc_answer.service.SubjectService;
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
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public List<Subject> getByMajorId(Integer majorId) {
        return subjectMapper.selectList(new LambdaQueryWrapper<Subject>().eq(Subject::getMajorId, majorId));
    }
}
