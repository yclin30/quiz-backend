package com.yclin.quiz.model.domain;

import com.yclin.quiz.model.domain.Questions;
import lombok.Data;

import java.util.List;

/**
 * 题目分页结果对象
 */
@Data
public class PageBeanQuestions {
    private int total;
    private List<Questions> rows;
}