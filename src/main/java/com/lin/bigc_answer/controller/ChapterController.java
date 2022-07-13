package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.Chapter;
import com.lin.bigc_answer.entity.question.Question;
import com.lin.bigc_answer.entity.question.Subject;
import com.lin.bigc_answer.entity.utils.QuestionDto;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.ChapterService;
import com.lin.bigc_answer.service.QuestionService;
import com.lin.bigc_answer.service.SubjectService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

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
    private ChapterService chapterService;
    @Resource(name = "subjectServiceImpl")
    private SubjectService subjectService;

    @Resource(name = "questionServiceImpl")
    private QuestionService questionService;

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

    /**
     * 添加章节
     * @param chapter 章节实体类
     */
    @RequiresRoles("TEACHER")
    @GetMapping("/add")
    public R addChapter(@RequestBody Chapter chapter) {
        if (subjectService.getById(chapter.getSubjectId()) == null) return new R().fail("添加失败,课程不存在");
        if (chapterService.save(chapter)) {
            return new R().success("添加成功");
        }
        return new R().fail("添加失败");
    }

    /**
     * 根据章节ID删除章节
     * @param cid 章节ID
     */
    @RequiresRoles("TEACHER")
    @GetMapping("/delete/{cid}")
    public R deleteChapter(@PathVariable("cid") String cid) {
        if (!VerifyUtils.isObjectNumber(cid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        List<Question> questionList = questionService.getQuestionListByChapterId(Integer.valueOf(cid));
        if (questionList == null) return new R().fail("章节不存在,无需删除");
        if (questionList.size() == 0) {
            if (chapterService.removeById(cid)) {
                return new R().success("删除成功");
            }
            return new R().fail("删除失败");
        }
        return new R().fail("抱歉,请先将该章节下的问题全部删除");
    }

}

