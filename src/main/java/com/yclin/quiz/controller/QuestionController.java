package com.yclin.quiz.controller;

import com.yclin.quiz.model.domain.PageBeanQuestions;
import com.yclin.quiz.model.domain.QSBean;
import com.yclin.quiz.model.domain.Questions;
import com.yclin.quiz.model.domain.Result;
import com.yclin.quiz.service.QuestionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目管理接口
 */
@RestController
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Autowired
    private QuestionsService questionService;

    /**
     * 新增单选题
     * Content-Type: application/json
     */
    @PostMapping("/addQuestion")
    public Result addQuestion(@RequestBody QSBean question) {
        int result = questionService.insertQuestion(question);
        if (result > 0) {
            return Result.success("插入新问题成功");
        } else {
            return Result.error("插入失败");
        }
    }

    // 逻辑删除
    @DeleteMapping("/soft/{id}")
    public Result softDelete(@PathVariable Integer id) {
        try {
            boolean ok = questionService.deleteQuestionById(id);
            return ok ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    // 分页查询
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBeanQuestions bean = questionService.page(page, pageSize);
        return Result.success(bean);
    }

    // 关键词查询
    @GetMapping("/search")
    public Result search(@RequestParam String keyword) {
        List<Questions> list = questionService.findByKeyword(keyword);
        return Result.success(list);
    }
}