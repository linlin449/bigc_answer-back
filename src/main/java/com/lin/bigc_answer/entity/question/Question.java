package com.lin.bigc_answer.entity.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题名称，可以加入图片
     */
    private String question;

    /**
     * 题目的简单描述，目的，建议等
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 分数<100分
     */
    private Integer score;

    /**
     * 对应的节
     */
    private Integer chapterId;


    private Integer subjectId;

    /**
     * 难度表，难，中，易
     */
    private Integer levelId;

    /**
     * 类型，单选，多选，简答
     */
    private Integer typeId;


}
