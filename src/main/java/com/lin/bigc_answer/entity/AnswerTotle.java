package com.lin.bigc_answer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class AnswerTotle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer studentId;

    /**
     * 错题数目
     */
    private Integer falseQuestionNumber;

    /**
     * 正确题目数目
     */
    private Integer trueQuestionNumber;

    /**
     * 所属专业
     */
    private Integer majorId;


}
