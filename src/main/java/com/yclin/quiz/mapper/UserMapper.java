package com.yclin.quiz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yclin.quiz.model.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper //运行时，框架会自动实现实现类对象，并将对象实例交由IoC容器管理；
public interface UserMapper extends BaseMapper<User> {


    //根据id删除用户；
    @Update("UPDATE user SET isDelete = 1, updateTime = NOW() WHERE id = #{id} AND isDelete = 0")
    public int deleteUserById(@Param("id") Long id);

    //根据username删除用户；
    @Update("UPDATE user SET isDelete = 1, updateTime = NOW() WHERE userName = #{username} AND isDelete = 0")
    public int deleteByUsername(@Param("username") String username);

    /**
     * 硬删除 - 根据id
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    int hardDeleteById(@Param("id") Long id);

    /**
     * 硬删除 - 根据用户名
     */
    @Delete("DELETE FROM user WHERE userName = #{username}")
    int hardDeleteByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM user WHERE isDelete=0")
    public int count();

    @Select("SELE" +
            "CT * FROM user WHERE isDelete=0 limit #{start},#{pageSize}")
    public List<User> page(Integer start, Integer pageSize);

    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%') AND isDelete=0")
    List<User> findByName(String keyword);
}