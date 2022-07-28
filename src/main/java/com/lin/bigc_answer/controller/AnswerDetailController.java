package com.lin.bigc_answer.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.entity.AnswerDetail;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.AnswerDetailService;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.UserRole;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/answerdetail")
public class AnswerDetailController {
    @Resource(name = "answerDetailServiceImpl")
    private AnswerDetailService answerDetailService;

    @Resource(name = "studentServiceImpl")
    private StudentService studentService;

    /**
     * 根据题目ID和学生username获取其答题详细
     * @param qid 题目ID
     * @param username 学生username
     */
    @GetMapping("/question/{qid}/username/{username}")
    public R getByQuestionId(@PathVariable("qid") String qid, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(qid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                AnswerDetail answerDetail = answerDetailService.getByQuestionIdAndStudentId(Integer.valueOf(qid), student.getId());
                if (answerDetail != null) {
                    return new R().success("success", answerDetail);
                }
                return new R().fail("答题记录不存在");
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 分页获取学生的错题详细,页大小默认为10
     * @param username 学生username
     * @param page 页码
     */
    @GetMapping("/wronglist/{page}/username/{username}")
    public R getWrongQuestion(@PathVariable("username") String username, @PathVariable("page") String page) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                IPage<AnswerDetail> answerDetailIPage = answerDetailService.getWrongPageByStudentId(student.getId(), Integer.parseInt(page), 10);
                return new R().success("success", answerDetailIPage);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 分页获取学生的正确题目答题详细,页大小默认为10
     * @param username 学生username
     * @param page 页码
     */
    @GetMapping("/rightlist/{page}/username/{username}")
    public R getRightQuestion(@PathVariable("username") String username, @PathVariable("page") String page) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                IPage<AnswerDetail> answerDetailIPage = answerDetailService.getRightPageByStudentId(student.getId(), Integer.parseInt(page), 10);
                return new R().success("success", answerDetailIPage);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }
}

