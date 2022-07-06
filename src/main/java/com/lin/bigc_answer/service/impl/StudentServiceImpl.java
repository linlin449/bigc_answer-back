package com.lin.bigc_answer.service.impl;

import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.mapper.StudentMapper;
import com.lin.bigc_answer.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
