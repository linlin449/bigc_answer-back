package com.lin.bigc_answer.entity.question;

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
 * @since 2022-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionRightAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "question_id", type = IdType.AUTO)
    private Integer id;
    private Integer questionId;

    /**
     * 若是多选题以-隔开
     */
    private String rightAnswer;

    private String analysis;


}
