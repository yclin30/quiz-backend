package com.yclin.quiz.mapper;

import com.yclin.quiz.model.domain.Questions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author yclin
* @description 针对表【questions】的数据库操作Mapper
* @createDate 2025-11-14 10:07:45
* @Entity com.yclin.quiz.model.domain.Questions
*/
@Mapper
public interface QuestionsMapper extends BaseMapper<Questions> {

    // 逻辑删除：根据id将 isDelete 置为 1
    @Update("UPDATE questions SET isDelete = 1, updateTime = NOW() WHERE id = #{id} AND isDelete = 0")
    int softDeleteById(@Param("id") Integer id);

    // 统计未删除的题目数量
    @Select("SELECT COUNT(*) FROM questions WHERE isDelete = 0")
    int count();

    // 分页查询未删除题目
    @Select("SELECT * FROM questions WHERE isDelete = 0 ORDER BY id DESC LIMIT #{start}, #{pageSize}")
    List<Questions> page(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

    // 按关键词模糊查询（只查未删除）
    // 你也可以扩展到选项字段：answer1Text/answer2Text/...，这里先按题干查询
    @Select("SELECT * FROM questions WHERE isDelete = 0 AND questionText LIKE CONCAT('%', #{keyword}, '%') ORDER BY id DESC")
    List<Questions> findByKeyword(@Param("keyword") String keyword);
}



