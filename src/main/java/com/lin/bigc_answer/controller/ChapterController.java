package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.Chapter;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.ChapterService;
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
@RequestMapping("/chapter")
public class ChapterController {
    @Resource(name = "chapterServiceImpl")
    ChapterService chapterService;

    @GetMapping("/subject/{sid}")
    public R getChapterBySubjectId(@PathVariable("sid") String sid) {
        if (!VerifyUtils.isStrNumber(sid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        List<Chapter> listBySubjectId = chapterService.getListBySubjectId(Integer.valueOf(sid));
        return new R().success("success", listBySubjectId);
    }
}

