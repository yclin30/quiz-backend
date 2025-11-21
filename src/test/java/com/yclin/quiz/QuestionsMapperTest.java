package com.yclin.quiz;

import com.yclin.quiz.mapper.QuestionsMapper;
import com.yclin.quiz.model.domain.Questions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class QuestionsMapperTest {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Test
    public void testInsertQuestion() {
        Questions q = new Questions();
        q.setQuestionText("What is the capital of France?");
        q.setAnswer1Text("Paris");
        q.setAnswer1Correct(true);
        q.setAnswer2Text("London");
        q.setAnswer2Correct(false);
        q.setAnswer3Text("Berlin");
        q.setAnswer3Correct(false);
        q.setAnswer4Text("Madrid");
        q.setAnswer4Correct(false);
        q.setIsDelete(0);
        q.setCreateTime(new Date());
        q.setUpdateTime(new Date());

        int rows = questionsMapper.insert(q);
        System.out.println("影响行数 = " + rows);
        System.out.println("生成主键ID = " + q.getId());
        Assertions.assertTrue(rows > 0);
        Assertions.assertNotNull(q.getId());
    }
}