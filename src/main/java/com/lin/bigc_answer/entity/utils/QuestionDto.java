package com.lin.bigc_answer.entity.utils;

import com.lin.bigc_answer.entity.question.Chapter;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private String subjectname;
    private List<Chapter> chapters;
}
