package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.QuestionOption;
import com.lin.bigc_answer.service.QuestionOptionService;
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
@RequestMapping("/option")
public class QuestionOptionController {
    @Resource(name = "questionOptionServiceImpl")
    QuestionOptionService questionOptionService;

    /**
     * 添加选项,需要管理员权限
     */
    @RequiresRoles({"ADMIN", "TEACHER"})
    @PostMapping("/add")
    public R addOption(@RequestBody QuestionOption questionOption) {
        if (questionOptionService.save(questionOption)) {
            return new R().success("添加成功");
        }
        return new R().fail("添加失败");
    }
}

