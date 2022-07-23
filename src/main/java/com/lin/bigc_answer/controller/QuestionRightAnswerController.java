package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.AnswerDetail;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.entity.question.QuestionRightAnswer;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.AnswerDetailService;
import com.lin.bigc_answer.service.QuestionRightAnswerService;
import com.lin.bigc_answer.service.QuestionService;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.utils.JWTUtil;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.UserRole;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lin
 * @since 2022-07-06
 */
@RestController
@RequestMapping("/rightanswer")
public class QuestionRightAnswerController {
    @Resource(name = "questionRightAnswerServiceImpl")
    private QuestionRightAnswerService questionRightAnswerService;

    @Resource(name = "questionServiceImpl")
    private QuestionService questionService;

    @Resource(name = "answerDetailServiceImpl")
    private AnswerDetailService answerDetailService;
    @Resource(name = "studentServiceImpl")
    private StudentService studentService;

    /**
     * 获取问题的正确答案以及作答答案
     * @param qid 问题ID
     */
    @GetMapping("/question/{qid}")
    public R getRightAnswer(@PathVariable("qid") String qid, HttpServletRequest request) {
        if (!VerifyUtils.isObjectNumber(qid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Question question = questionService.getById(qid);
        Subject subject = SecurityUtils.getSubject();
        String token = request.getHeader("X-Token");
        String userName = JWTUtil.getUserName(token);
        Student student = studentService.queryByUserName(userName);
        if (subject.isPermitted(UserRole.STUDENT + ":" + userName)) {
            if (question != null) {
                AnswerDetail answerDetail = answerDetailService.getByQuestionIdAndStudentId(question.getId(), student.getId());
                if (answerDetail != null) {
                    QuestionRightAnswer rightAnswer = questionRightAnswerService.getByQuestionId(question.getId());
                    if (rightAnswer != null) {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("rightAnswer", rightAnswer.getRightAnswer());
                        map.put("analysis", rightAnswer.getAnalysis());
                        map.put("isRight", answerDetail.getIsRight());
                        map.put("answerText", answerDetail.getAnswerText());
                        return new R().success("success", map);
                    }
                    return new R().fail("答案不存在");
                }
                return new R().fail("未作答无法查看题目答案");
            }
            return new R().fail("题目不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 添加正确答案,需要管理员和老师权限
     */
    @RequiresRoles("TEACHER")
    @PostMapping("/add")
    public R addRightAnswer(@RequestBody QuestionRightAnswer questionRightAnswer) {
        if (questionRightAnswer.getQuestionId() == null) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        if (questionRightAnswerService.getByQuestionId(questionRightAnswer.getQuestionId()) != null) {
            return new R().fail("添加错误,当前题目已存在正确答案");
        }
        if (questionRightAnswerService.save(questionRightAnswer)) {
            return new R().success("添加成功");
        }
        return new R().fail("添加失败");
    }
    @RequiresRoles("TEACHER")
    @PutMapping("/uptate")
    public R updateRightAnswer(@RequestBody QuestionRightAnswer questionRightAnswer){
        if (questionRightAnswer.getQuestionId() == null) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        QuestionRightAnswer qRA=questionRightAnswerService.getByQuestionId(questionRightAnswer.getQuestionId());
        if (qRA != null) {
            questionRightAnswer.setId(qRA.getId());
            questionRightAnswerService.updateById(questionRightAnswer);
            return new R().success("更新成功");
        }
        return new R().fail("更新失败");
    }

    /**
     *
     * @param qid
     * @return
     */
    @GetMapping("/question/{qid}")
    public R get(@PathVariable("qid") String qid) {
        if (!VerifyUtils.isObjectNumber(qid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        QuestionRightAnswer questionRightAnswer=questionRightAnswerService.getByQuestionId(Integer.valueOf(qid));
        if (questionRightAnswer != null) {
            return new R().success("success", questionRightAnswer);
        }
        return new R().fail("题目正确答案不存在");
    }
}

