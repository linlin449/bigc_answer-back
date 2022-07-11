package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.Chapter;
import com.lin.bigc_answer.entity.question.Subject;
import com.lin.bigc_answer.entity.utils.QuestionDto;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.ChapterService;
import com.lin.bigc_answer.service.SubjectService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource(name = "subjectServiceImpl")
    SubjectService subjectService;

    /**
     * 通过subject ID 获取章节列表
     * @param sid subject ID
     */
    @GetMapping("/subject/{sid}")
    public R getChapterBySubjectId(@PathVariable("sid") String sid) {
        if (!VerifyUtils.isObjectNumber(sid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        List<Chapter> listBySubjectId = chapterService.getListBySubjectId(Integer.valueOf(sid));
        return new R().success("success", listBySubjectId);
    }

    /**
     * 封装一个subject和对应的章节 在一起的数组
     * @return R
     */
    @GetMapping("")
    public R getAllChapter() {
        List<QuestionDto> questionDtos = new ArrayList<>();
        List<Subject> subjects = subjectService.list();
        for (Subject subject : subjects) {
            List<Chapter> chapters = chapterService.getListBySubjectId(subject.getId());
            QuestionDto questionDto = new QuestionDto();
            questionDto.setSubjectname(subject.getName());
            questionDto.setChapters(chapters);
            questionDtos.add(questionDto);
        }
        return new R().success("success", questionDtos);
    }
}

