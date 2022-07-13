package com.lin.bigc_answer.controller;


import com.lin.bigc_answer.entity.question.Major;
import com.lin.bigc_answer.exception.ErrorCode;
import com.lin.bigc_answer.service.MajorService;
import com.lin.bigc_answer.utils.R;
import com.lin.bigc_answer.utils.VerifyUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/major")
public class MajorController {
    @Resource(name = "majorServiceImpl")
    private MajorService majorService;

    /**
     * 获取专业列表
     */
    @GetMapping("/list")
    public R getMajorList() {
        return new R().success("success", majorService.list());
    }

    /**
     * 添加专业
     * @param major 专业实体类
     */
    @RequiresRoles("TEACHER")
    @PostMapping("/add")
    public R addMajor(@RequestBody Major major) {
        if (majorService.save(major)) {
            return new R().success("添加成功");
        }
        return new R().fail("添加失败");
    }

    /**
     * 通过专业ID删除专业
     * @param mid 专业ID
     */
    @RequiresRoles("TEACHER")
    @GetMapping("/delete/{mid}")
    public R deleteMajor(@PathVariable("mid") String mid) {
        if (!VerifyUtils.isObjectNumber(mid)) return new R().fail("参数错误", null, ErrorCode.PARAMETER_ERROR);
        Major major = majorService.getById(mid);
        if (major != null) {
            if (majorService.removeById(mid)) {
                return new R().success("删除成功");
            }
            return new R().fail("删除失败");
        }
        return new R().fail("专业不存在,无法删除");
    }

    /**
     * 修改专业
     * @param major 专业实体类
     */
    @RequiresRoles("TEACHER")
    @PostMapping("/update")
    public R updateMajor(@RequestBody Major major) {
        if (majorService.updateById(major)) {
            return new R().success("修改成功");
        }
        return new R().fail("修改失败");
    }
}

