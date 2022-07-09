package com.lin.bigc_answer.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.QuestionService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource(name = "questionServiceImpl")
    QuestionService questionService;
    @Resource(name = "studentServiceImpl")
    StudentService studentService;


    /**
     * 根据页码获取题目,页大小默认为10
     * @param page 页码
     */
    @GetMapping("/list/{page}")
    public R getQuestionByPage(@PathVariable("page") String page) {
        if (!VerifyUtils.isStrNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        IPage<Question> questionPage = questionService.getQuestionPage(Integer.parseInt(page), 10);
        return new R().success("success", questionPage);
    }

    /**
     * 根据id获取题目
     * @param id 题目ID
     */
    @GetMapping("/{id}")
    public R getQuestionPage(@PathVariable("id") String id) {
        if (!VerifyUtils.isStrNumber(id)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Question question = questionService.getById(id);
        if (question != null) {
            return new R().success("success", question);
        }
        return new R().fail("题目不存在");
    }

    /**
     * 根据subject和章节获取题目,页大小默认为10
     * @param page 页码
     * @param sid Subject ID
     */
    @GetMapping("/list/{page}/subject/{sid}")
    private R getQuestionPageByChapter(@PathVariable("page") String page, @PathVariable("sid") String sid) {
        if (!VerifyUtils.isStrNumber(page) || !VerifyUtils.isStrNumber(sid))
            return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        IPage<Question> questionIPage = questionService.getQuestionPageBySubject(Integer.parseInt(sid), Integer.parseInt(page), 10);
        return new R().success("success", questionIPage);
    }

    /**
     * 分页获取学生做过的题目,页大小默认为10
     * @param page 页码
     * @param username 学生username
     */
    @GetMapping("/list/{page}/answered/{username}")
    private R getAnsweredQuestionPage(@PathVariable("page") String page, @PathVariable("username") String username) {
        if (!VerifyUtils.isStrNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        //权限检验
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                return new R().success("success", questionService.getAnsweredPageByStudentId(student.getId(), Integer.parseInt(page), 10));
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 分页获取学生为做过的题目,页大小默认为10
     * @param page 页码
     * @param username 学生username
     */
    @GetMapping("/list/{page}/unanswered/{username}")
    private R getUnansweredQuestionPage(@PathVariable("page") String page, @PathVariable("username") String username) {
        if (!VerifyUtils.isStrNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        //权限检验
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                return new R().success("success", questionService.getUnansweredPageByStudentId(student.getId(), Integer.parseInt(page), 10));
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 检查学生某一题的答题状态(未作答 or 已作答)
     * @param qid 问题ID
     * @param username 学生username
     */
    @GetMapping("/check/{qid}/username/{username}")
    private R getStudentQuestionStatus(@PathVariable("qid") String qid, @PathVariable("username") String username) {
        if (!VerifyUtils.isStrNumber(qid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        //权限检验
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionID", qid);
                map.put("username", username);
                map.put("answered", questionService.isQuestionAnswered(Integer.valueOf(qid), username) ? 1 : 0);
                return new R().success("success", map);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }
}

