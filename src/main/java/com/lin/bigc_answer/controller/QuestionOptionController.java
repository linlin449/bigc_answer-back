package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.QuestionOption;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.QuestionOptionService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.UserRole;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lin
 * @since 2022-07-06
 */
@RestController
@RequestMapping("/option")
public class QuestionOptionController {
    @Resource(name = "questionOptionServiceImpl")
    private QuestionOptionService questionOptionService;

    /**
     * 添加选项,需要管理员和老师权限
     */
    @RequiresRoles("TEACHER")
    @PostMapping("/add")
    public R addOption(@RequestBody QuestionOption questionOption) {
        if (questionOptionService.save(questionOption)) {
            return new R().success("添加成功");
        }
        return new R().fail("添加失败");
    }

    /**
     * 获取某题的选项
     * @param qid 问题ID
     */
    @GetMapping("/question/{qid}")
    public R getOption(@PathVariable("qid") String qid) {
        QuestionOption option = questionOptionService.getById(qid);
        if (option != null) {
            return new R().success("success", option);
        }
        return new R().fail("题目选项不存在");
    }

    /**
     * 根据问题ID列表获取其对应选项列表
     * @param questionList 问题ID列表
     */
    @GetMapping("/question")
    public R getOptionList(@RequestParam("questionIDs") List<Integer> questionList) {
        if (questionList.size() > 20) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.hasRole(UserRole.ADMIN.name()) && !subject.hasRole(UserRole.TEACHER.name()))
                return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
        }
        List<QuestionOption> option = questionOptionService.getByQuestionIds(questionList);
        if (option != null) {
            return new R().success("success", option);
        }
        return new R().fail("题目选项不存在");
    }
}

