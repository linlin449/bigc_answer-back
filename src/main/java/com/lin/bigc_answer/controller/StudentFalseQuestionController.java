package com.lin.bigc_answer.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.bigc_answer.entity.StudentFalseQuestion;
import com.lin.bigc_answer.entity.user.Student;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.StudentFalseQuestionService;
import com.lin.bigc_answer.service.StudentService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.UserRole;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("/favorite")
public class StudentFalseQuestionController {

    @Resource(name = "studentFalseQuestionServiceImpl")
    private StudentFalseQuestionService studentFalseQuestionService;
    @Resource(name = "studentServiceImpl")
    private StudentService studentService;

    /**
     * 获取学生收藏题目的数量
     * @param username 学生username
     * @return 返回的数量为 data = 数量
     */
    @GetMapping("/count/username/{username}")
    public R getFavoriteNums(@PathVariable("username") String username) {
        Subject subject = SecurityUtils.getSubject();
        Integer count = studentFalseQuestionService.getCountByUsername(username);
        if (subject.isPermitted(UserRole.STUDENT + ":" + username)) {
            if (count != null) {
                return new R().success("success", count);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 分页获取学生收藏的题目,页大小默认为10
     * @param page 页码
     * @param username 学生username
     */
    @GetMapping("/list/{page}/username/{username}")
    public R getFavoriteList(@PathVariable("page") String page, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(page)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                IPage<StudentFalseQuestion> iPage = studentFalseQuestionService.selectPageByStudentId(student.getId(), Integer.parseInt(page), 10);
                return new R().success("success", iPage);
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 根据题目ID和学生username删除其收藏题目
     * @param qid 题目ID
     * @param username 学生username
     */
    @GetMapping("/delete/{qid}/username/{username}")
    public R deleteByQuestionId(@PathVariable("qid") String qid, @PathVariable("username") String username) {
        if (!VerifyUtils.isObjectNumber(qid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                Boolean delete = studentFalseQuestionService.deleteByQuestionIdAndUsername(Integer.valueOf(qid), student.getId());
                if (delete != null) {
                    if (delete) {
                        return new R().success("删除成功");
                    }
                    return new R().fail("删除失败");
                }
                return new R().fail("收藏题目不存在,无法删除");
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }

    /**
     * 修改收藏题目的笔记
     * @param qid 问题ID
     * @param username 学生username
     * @param param Post提交参数,需要名为note的参数
     */
    @PostMapping("/update/note/question/{qid}/username/{username}")
    public R updateNote(@PathVariable("qid") String qid, @PathVariable("username") String username, @RequestBody Map<String, Object> param) {
        Object note = param.get("note");
        if (!VerifyUtils.isObjectNumber(qid) || note == null)
            return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted(UserRole.STUDENT + ":" + username)) {
            Student student = studentService.queryByUserName(username);
            if (student != null) {
                StudentFalseQuestion question = studentFalseQuestionService.getByQuestionIdAndUsername(Integer.valueOf(qid), student.getId());
                if (question != null) {
                    if (!question.getNote().equals(note.toString())) {
                        question.setNote(note.toString());
                        if (studentFalseQuestionService.updateById(question)) {
                            return new R().success("修改成功");
                        }
                        return new R().fail("修改失败");
                    }
                    return new R().fail("内容相同,无需修改");
                }
                return new R().fail("获取失败,收藏题目不存在");
            }
            return new R().fail("学生不存在");
        }
        return new R().fail("权限不足", null, ErrorCode.UNAUTHORIZED_ERROR);
    }
}

