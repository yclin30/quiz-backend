package com.yclin.quiz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yclin.quiz.mapper.QuestionsMapper;
import com.yclin.quiz.model.domain.PageBeanQuestions;
import com.yclin.quiz.model.domain.QSBean;
import com.yclin.quiz.model.domain.Questions;
import com.yclin.quiz.service.QuestionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionsMapper, Questions> implements QuestionsService {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Override
    @Transactional
    public int insertQuestion(QSBean qsBean) {
        // 基本参数校验
        if (qsBean == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (qsBean.getQuestion() == null || qsBean.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("题干不能为空");
        }
        if (qsBean.getOptiona() == null || qsBean.getOptiona().trim().isEmpty()
                || qsBean.getOptionb() == null || qsBean.getOptionb().trim().isEmpty()
                || qsBean.getOptionc() == null || qsBean.getOptionc().trim().isEmpty()
                || qsBean.getOptiond() == null || qsBean.getOptiond().trim().isEmpty()) {
            throw new IllegalArgumentException("所有选项都不能为空");
        }

        String ans = qsBean.getAnswer();
        if (ans == null) {
            throw new IllegalArgumentException("答案不能为空");
        }
        ans = ans.trim().toLowerCase();

        // JDK8 使用 Arrays.asList 替代 List.of
        List<String> allowed = Arrays.asList("a", "b", "c", "d");
        if (!allowed.contains(ans)) {
            throw new IllegalArgumentException("Answer must be one of: a, b, c, or d");
        }

        // 构建实体（你的 Questions 中 answerXCorrect 是 Integer，这里用 1/0 赋值）
        Questions q = new Questions();
        q.setQuestionText(qsBean.getQuestion());

        q.setAnswer1Text(qsBean.getOptiona());
        q.setAnswer1Correct("a".equals(ans));

        q.setAnswer2Text(qsBean.getOptionb());
        q.setAnswer2Correct("b".equals(ans) );

        q.setAnswer3Text(qsBean.getOptionc());
        q.setAnswer3Correct("c".equals(ans) );

        q.setAnswer4Text(qsBean.getOptiond());
        q.setAnswer4Correct("d".equals(ans) );

        q.setIsDelete(0);
        Date now = new Date();
        q.setCreateTime(now);
        q.setUpdateTime(now);

        // 使用 BaseMapper 的 insert（可用 this.baseMapper 或注入的 questionsMapper）
        return this.baseMapper.insert(q);
    }

    /**
     * 3、题目逻辑删除：根据 id 将 isDelete 置为 1
     */
    @Override
    @Transactional
    public boolean deleteQuestionById(Integer id) {
        if (id == null || id <= 0) {
            throw new RuntimeException("题目id不合法");
        }
        // 先检查是否存在且未删除
        Questions exists = this.getById(id);
        if (exists == null || (exists.getIsDelete() != null && exists.getIsDelete() == 1)) {
            throw new RuntimeException("题目不存在");
        }
        int rows = questionsMapper.softDeleteById(id);
        log.info("软删除题目 id={}，影响行数={}", id, rows);
        return rows > 0;
    }

    /**
     * 4.1、分页查询（只查未删除）
     */
    @Override
    public PageBeanQuestions page(Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        int total = questionsMapper.count();
        int start = (page - 1) * pageSize;
        List<Questions> list = questionsMapper.page(start, pageSize);

        PageBeanQuestions bean = new PageBeanQuestions();
        bean.setTotal(total);
        bean.setRows(list);
        return bean;
    }

    /**
     * 4.2、按关键词查询（题干模糊匹配，只查未删除）
     */
    @Override
    public List<Questions> findByKeyword(String keyword) {
        if (keyword == null) keyword = "";
        return questionsMapper.findByKeyword(keyword.trim());
    }
}