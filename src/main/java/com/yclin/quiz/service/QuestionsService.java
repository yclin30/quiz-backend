package com. yclin.quiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yclin. quiz.model.domain.PageBeanQuestions;
import com.yclin.quiz.model.domain.QSBean;
import com.yclin.quiz.model.domain.Questions;

import java.util.List;

public interface QuestionsService extends IService<Questions> {

    int insertQuestion(QSBean question);

    boolean deleteQuestionById(Integer id);

    PageBeanQuestions page(Integer page, Integer pageSize);

    List<Questions> findByKeyword(String keyword);

    /**
     * 🆕 更新题目
     */
    boolean updateQuestion(Questions question);

    /**
     * 🆕 根据ID查询题目
     */
    Questions getQuestionById(Integer id);
}