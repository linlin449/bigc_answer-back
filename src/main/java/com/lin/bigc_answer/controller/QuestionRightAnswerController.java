package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.QuestionRightAnswer;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.QuestionRightAnswerService;
import com.lin.bigc_answer.utils.R;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}

