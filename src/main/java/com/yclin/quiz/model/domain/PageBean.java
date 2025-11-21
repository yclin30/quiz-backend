package com.yclin.quiz.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class PageBean{
    private int total;
    private List<User> rows;
}