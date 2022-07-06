package com.lin.bigc_answer.mapper;

import com.lin.bigc_answer.entity.question.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

}
