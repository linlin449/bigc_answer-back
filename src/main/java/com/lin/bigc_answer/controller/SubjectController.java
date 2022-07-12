package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.Chapter;
import com.lin.bigc_answer.entity.question.Subject;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.ChapterService;
import com.lin.bigc_answer.service.SubjectService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/subject")
public class SubjectController {
    @Resource(name = "subjectServiceImpl")
    private SubjectService subjectService;

    @Resource(name = "chapterServiceImpl")
    private ChapterService chapterService;

    /**
     * 根据ID获取课程
     * @param sid 课程ID
     */
    @GetMapping("/{sid}")
    public R getByID(@PathVariable("sid") String sid) {
        Subject subject = subjectService.getById(sid);
        if (subject != null) {
            return new R().success("success", subject);
        }
        return new R().fail("课程不存在");
    }

    /**
     * 获取所有课程
     */
    @GetMapping("/getall")
    public R getAllSubject() {
        List<Subject> subjectList = subjectService.list();
        return new R().success("success", subjectList);
    }

    /**
     * 根据专业ID获取其课程列表
     * @param mid 专业ID
     */
    @GetMapping("/major/{mid}")
    public R getSubjectByMajorId(@PathVariable("mid") String mid) {
        if (!VerifyUtils.isObjectNumber(mid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        List<Subject> subjectList = subjectService.getByMajorId(Integer.valueOf(mid));
        return new R().success("success", subjectList);
    }

    /**
     * 添加课程
     * @param subject 课程实体类
     */
    @RequiresRoles("TEACHER")
    @PostMapping("/add")
    public R addSubject(@RequestBody Subject subject) {
        if (subjectService.save(subject)) {
            return new R().success("添加成功");
        }
        return new R().fail("添加失败");
    }

    /**
     * 通过课程ID删除课程
     * @param sid 课程ID
     */
    @RequiresRoles("TEACHER")
    @PostMapping("/delete/{sid}")
    public R deleteSubject(@PathVariable("sid") String sid) {
        if (!VerifyUtils.isObjectNumber(sid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        List<Chapter> chapterList = chapterService.getListBySubjectId(Integer.valueOf(sid));
        if (chapterList == null) return new R().fail("当前课程不存在,无法删除");
        if (chapterList.size() == 0) {
            if (subjectService.removeById(sid)) {
                return new R().success("删除成功");
            }
            return new R().fail("删除失败");
        }
        return new R().fail("抱歉,请先将该课程下的章节全部删除");
    }
}

