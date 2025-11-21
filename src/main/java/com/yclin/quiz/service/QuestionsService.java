package com.yclin.quiz.service;

import com.yclin.quiz.model.domain.PageBeanQuestions;
import com.yclin.quiz.model.domain.QSBean;
import com.yclin.quiz.model.domain.Questions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author yclin
* @description 针对表【questions】的数据库操作Service
* @createDate 2025-11-14 10:07:45
*/
public interface QuestionsService extends IService<Questions> {

    /**
     * 插入单选题，返回影响行数（也可以改成返回新记录ID）
     */
    int insertQuestion(QSBean qsBean);

    // 3、题目删除（逻辑删除）
    boolean deleteQuestionById(Integer id);

    // 4、题目查询
    PageBeanQuestions page(Integer page, Integer pageSize);

    List<Questions> findByKeyword(String keyword);
}