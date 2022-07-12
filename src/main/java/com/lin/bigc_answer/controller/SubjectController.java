package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.Subject;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.SubjectService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Resource(name = "subjectServiceImpl")
    private SubjectService subjectService;

    /**
     * 根据ID获取课程
     * @param sid 课程ID
     */
    @GetMapping("/{sid}")
    public R getByID(@PathVariable("sid") String sid) {
        Subject subject = subjectService.getById(sid);
        if (subject != null) {
            return new R().success("success", subject);
        }
        return new R().fail("课程不存在");
    }

    /**
     * 获取所有课程
     */
    @GetMapping("/getall")
    public R getAllSubject() {
        List<Subject> subjectList = subjectService.list();
        return new R().success("success", subjectList);
    }

    /**
     * 根据专业ID获取其课程列表
     * @param mid 专业ID
     */
    @GetMapping("/major/{mid}")
    public R getSubjectByMajorId(@PathVariable("mid") String mid) {
        if (!VerifyUtils.isObjectNumber(mid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        List<Subject> subjectList = subjectService.getByMajorId(Integer.valueOf(mid));
        return new R().success("success", subjectList);
    }
}

