package com.yclin.quiz.model.domain;

import lombok.Data;

/**
 * 题目导入/传输 Bean (单选)
 */
@Data
public class QSBean {
    private String question;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;
    private String answer; // a / b / c / d
}