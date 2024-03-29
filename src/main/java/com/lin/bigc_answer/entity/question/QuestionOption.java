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
 * @since 2022-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer questionId;
    @TableField("A")
    private String a;

    @TableField("B")
    private String b;

    @TableField("C")
    private String c;

    @TableField("D")
    private String d;

    @TableField("E")
    private String e;

    @TableField("F")
    private String f;


}
