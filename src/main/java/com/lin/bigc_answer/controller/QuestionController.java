package com.lin.bigc_answer.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.entity.AnswerDetail;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.entity.question.QuestionRightAnswer;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.*;
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
import java.util.HashMap;
import java.util.List;
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
    private QuestionService questionService;
    @Resource(name = "studentServiceImpl")
    private StudentService studentService;
    @Resource(name = "answerDetailServiceImpl")
    private AnswerDetailService answerDetailService;
    @Resource(name = "questionRightAnswerServiceImpl")
    private QuestionRightAnswerService questionRightAnswerService;

    @Resource(name = "questionOptionServiceImpl")
    private QuestionOptionService questionOptionService;

    @Resource(name = "subjectServiceImpl")
    private SubjectService subjectService;

    @Resource(name = "chapterServiceImpl")
    private ChapterService chapterService;

    /**
     * 根据页码获取题目,页大小默认为10
     * @param page 页码
     */
    @GetMapping("/list/{page}")
    public R getQuestionByPage(@PathVariable("page") String page) {
        if (!VerifyUtils.isObjectNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        IPage<Question> questionPage = questionService.getQuestionPage(Integer.parseInt(page), 10);
        return new R().success("success", questionPage);
    }

    /**
     * 根据id获取题目
     * @param id 题目ID
     */
    @GetMapping("/{id}")
    public R getQuestionPage(@PathVariable("id") String id) {
        if (!VerifyUtils.isObjectNumber(id)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
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
    public R getQuestionPageByChapter(@PathVariable("page") String page, @PathVariable("sid") String sid) {
        if (!VerifyUtils.isObjectNumber(page) || !VerifyUtils.isObjectNumber(sid))
            return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        IPage<Question> questionIPage = questionService.getQuestionPageBySubject(Integer.parseInt(sid), Integer.parseInt(page), 10);
        return new R().success("success", questionIPage);
    }

    /**
     * 根据章节ID获取题目列表
     * @param cid 章节ID
     */
    @GetMapping("/chapter/{cid}")
    public R getQuestionPageByChapter(@PathVariable("cid") String cid) {
        if (!VerifyUtils.isObjectNumber(cid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        List<Question> questionList = questionService.getQuestionListByChapterId(Integer.parseInt(cid));
        return new R().success("success", questionList);
    }

    /**
     * 分页获取学生做过的题目,页大小默认为10
     * @param page 页码
     * @param username 学生username
     */
    @GetMapping("/list/{page}/answered/{username}")
    public R getAnsweredQuestionPage(@PathVariable("page") String page, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
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
     * 分页获取学生未做过的题目,页大小默认为10
     * @param page 页码
     * @param username 学生username
     */
    @GetMapping("/list/{page}/unanswered/{username}")
    public R getUnansweredQuestionPage(@PathVariable("page") String page, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
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
     * 检查学生某一题的答题状态(答对true 答错false 其他null)
     * @param qid 问题ID
     * @param username 学生username
     */
    @GetMapping("/check/{qid}/username/{username}")
    public R getStudentQuestionStatus(@PathVariable("qid") String qid, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(qid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        //权限检验
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionID", qid);
                map.put("username", username);
                map.put("answerRight", questionService.isQuestionAnswerRight(Integer.valueOf(qid), username));
                return new R().success("success", map);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 检查学生一些题目的答题状态(答对true 答错false 其他null)
     * @param questionList 问题ID列表
     * @param username 学生username
     */
    @GetMapping("/check/username/{username}")
    public R getStudentQuestionStatusList(@RequestParam("questionIDs") List<Integer> questionList, @PathVariable("username") String username) {
        Subject subject = SecurityUtils.getSubject();
        //权限检验
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("answerRight", questionService.isQuestionListRight(questionList, username));
                return new R().success("success", map);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 获取某学生的错题,分页展示,页大小默认为10
     * @param page 页码
     * @param username 学生username
     */
    @GetMapping("/list/{page}/wrong/{username}")
    public R getStudentWrongQuestionPage(@PathVariable("page") String page, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        Student student = studentService.queryByUserName(username);
        //权限检验
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            if (student != null) {
                return new R().success("success", questionService.getWrongPageByStudentId(student.getId(), Integer.parseInt(page), 10));
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 获取某学生答对的题目,分页展示,页大小默认为10
     * @param page 页码
     * @param username 学生username
     */
    @GetMapping("/list/{page}/right/{username}")
    public R getStudentRightQuestionPage(@PathVariable("page") String page, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        Student student = studentService.queryByUserName(username);
        //权限检验
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            if (student != null) {
                return new R().success("success", questionService.getRightPageByStudentId(student.getId(), Integer.parseInt(page), 10));
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     *添加题目,需要管理员和老师权限
     */
    @RequiresRoles("TEACHER")
    @PostMapping("/add")
    public R addQuestion(@RequestBody Question question) {
        if (chapterService.getById(question.getChapterId()) == null) return new R().fail("添加失败,章节不存在");
        if (subjectService.getById(question.getSubjectId()) == null) return new R().fail("添加失败,课程不存在");
        Integer questionId = questionService.addQuestion(question);
        if (questionId != null) {
            return new R().success("添加成功", questionId);
        }
        return new R().fail("添加失败!");
    }

    /**
     * 更新操作需要老师和管理员权限
     * @param question
     * @return
     */
    @RequiresRoles("TEACHER")
    @PutMapping("/uptate")
    public R updateQuestion(@RequestBody Question question){
        if (chapterService.getById(question.getChapterId()) == null) return new R().fail("更新失败,章节不存在");
        if (subjectService.getById(question.getSubjectId()) == null) return new R().fail("更新失败,课程不存在");
        if(questionService.updateById(question)){
            return new R().success("更新成功");
        }
        return new R().fail("更新失败");
    }

    /**
     * 答题
     * @param params 请求数据需要携带 answer 和 questionID
     */
    @PostMapping("/answer")
    public R answerQuestion(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String answer = params.get("answer");
        String questionID = params.get("questionID");
        if (answer == null || questionID == null || !VerifyUtils.isObjectNumber(questionID))
            return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        String token = request.getHeader("X-Token");
        String userName = JWTUtil.getUserName(token);
        Student student = studentService.queryByUserName(userName);
        if (subject.isPermitted(UserRole.STUDENT + ":" + userName)) {
            Question question = questionService.getById(questionID);
            QuestionRightAnswer questionRightAnswer = questionRightAnswerService.getByQuestionId(Integer.valueOf(questionID));
            if (answerDetailService.getByQuestionIdAndStudentId(Integer.valueOf(questionID), student.getId()) != null) {
                return new R().fail("抱歉,该题您已经答过,无法重复答题");
            }
            if (question != null && questionRightAnswer != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("rightAnswer", questionRightAnswer.getRightAnswer());
                result.put("analysis", questionRightAnswer.getAnalysis());
                AnswerDetail answerDetail = new AnswerDetail();
                answerDetail.setQuestionId(Integer.valueOf(questionID));
                answerDetail.setStudentId(student.getId());
                if (questionRightAnswer.getRightAnswer().equals(answer)) {
                    result.put("result", true);
                    answerDetail.setIsRight(1);
                } else {
                    result.put("result", false);
                }
                answerDetail.setIsRight(0);
                if (answerDetailService.save(answerDetail)) {
                    return new R().success("success", result);
                }
                return new R().fail("服务器异常,答题失败");
            }
            return new R().fail("题目出现异常");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 根据题目ID删除题目
     * @param qid 题目ID
     */
    @RequiresRoles("TEACHER")
    @GetMapping("/delete/{qid}")
    public R delQuestion(@PathVariable("qid") String qid) {
        if (!VerifyUtils.isObjectNumber(qid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        if (questionService.getById(qid) != null) {
            if (questionService.deleteQuestionAndOptionById(Integer.valueOf(qid))) {
                return new R().success("删除成功");
            }
            return new R().fail("删除失败");
        }
        return new R().fail("题目不存在,无法删除");
    }

    /**
     * 获取学生的做题详细(已做题数量 未做题数量 正确数量)
     * @param username 学生username
     */
    @GetMapping("/info/student/{username}")
    public R getStudentQuestionDetail(@PathVariable("username") String username) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT.name() + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                IPage<Question> answeredPage = questionService.getAnsweredPageByStudentId(student.getId(), 1, 1);
                IPage<Question> unansweredPage = questionService.getUnansweredPageByStudentId(student.getId(), 1, 1);
                IPage<Question> rightPage = questionService.getRightPageByStudentId(student.getId(), 1, 1);
                Map<String, Object> map = new HashMap<>();
                map.put("answered", answeredPage.getTotal());
                map.put("unanswered", unansweredPage.getTotal());
                map.put("answerright", rightPage.getTotal());
                return new R().success("success", map);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }
}

