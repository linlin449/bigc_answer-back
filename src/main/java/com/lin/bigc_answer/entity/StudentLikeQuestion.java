package com.lin.bigc_answer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class StudentLikeQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer studentId;

    /**
     * 问题id值
     */
    private Integer questionId;

    /**
     * 问题所属节
     */
    private Integer chapterId;

    /**
     * 笔记
     */
    private String note;

    /**
     * 是否喜欢，默认为0，表示错题
     */
    private Integer isLike;


}
