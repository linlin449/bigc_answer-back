package com.lin.bigc_answer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bigc_answer.entity.user.TeacherStudent;
import com.lin.bigc_answer.entity.utils.StudentDTO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface TeacherStudentMapper extends BaseMapper<TeacherStudent> {

    List<StudentDTO> getStudentListByTeacherId(int teacherId);
}
