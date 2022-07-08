package com.lin.bigc_answer.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.QuestionService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.VerifyUtils;
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
@RequestMapping("/question")
public class QuestionController {
    @Resource(name = "questionServiceImpl")
    QuestionService questionService;

    //根据页码获取题目
    //页大小默认为10
    @GetMapping("/list/{page}")
    public R getQuestionByPage(@PathVariable("page") String page) {
        if (!VerifyUtils.isStrNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        IPage<Question> questionPage = questionService.getQuestionPage(Integer.parseInt(page), 10);
        return new R().success("success", questionPage);
    }

    //根据id获取题目
    @GetMapping("/{id}")
    public R getQuestionPage(@PathVariable("id") String id) {
        if (!VerifyUtils.isStrNumber(id)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Question question = questionService.getById(id);
        if (question != null) {
            return new R().success("success", question);
        }
        return new R().fail("题目不存在");
    }

    //根据页码和章节获取题目
    //页大小默认为10
    @GetMapping("/list/chapter/{pid}/{cid}")
    private R getQuestionPageByChapter(@PathVariable("pid") String pid, @PathVariable("cid") String cid) {
        if (!VerifyUtils.isStrNumber(pid) || !VerifyUtils.isStrNumber(cid))
            return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        IPage<Question> questionIPage = questionService.getQuestionPageByChapter(Integer.parseInt(cid), Integer.parseInt(pid), 10);
        return new R().success("success", questionIPage);
    }
}

