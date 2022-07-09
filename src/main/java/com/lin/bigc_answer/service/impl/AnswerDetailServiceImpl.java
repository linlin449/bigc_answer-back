package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.AnswerDetail;
import com.lin.bigc_answer.mapper.AnswerDetailMapper;
import com.lin.bigc_answer.service.AnswerDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Service
public class AnswerDetailServiceImpl extends ServiceImpl<AnswerDetailMapper, AnswerDetail> implements AnswerDetailService {

    @Resource
    AnswerDetailMapper answerDetailMapper;

    @Override
    public IPage<AnswerDetail> selectPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<AnswerDetail> iPage = new Page<>(currentPage, pageSize);
        return answerDetailMapper.selectPage(iPage, new LambdaQueryWrapper<AnswerDetail>().eq(AnswerDetail::getStudentId, studentId));
    }

    @Override
    public AnswerDetail getByQuestionIdAndStudentId(Integer questionId, Integer studentId) {
        LambdaQueryWrapper<AnswerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnswerDetail::getQuestionId, questionId).eq(AnswerDetail::getStudentId, studentId);
        return answerDetailMapper.selectOne(wrapper);
    }

    @Override
    public IPage<AnswerDetail> getRightPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<AnswerDetail> iPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<AnswerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnswerDetail::getStudentId, studentId).eq(AnswerDetail::getIsRight, true);
        return answerDetailMapper.selectPage(iPage, wrapper);
    }

    @Override
    public IPage<AnswerDetail> getWrongPageByStudentId(Integer studentId, int currentPage, int pageSize) {
        IPage<AnswerDetail> iPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<AnswerDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnswerDetail::getStudentId, studentId).eq(AnswerDetail::getIsRight, false);
        return answerDetailMapper.selectPage(iPage, wrapper);
    }
}
