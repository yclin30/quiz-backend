package com.yclin.quiz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("hello");
        return "Hello World";
    }
    @RequestMapping("/simpleParam") //表示如果接收到浏览器的/simpleParam，就执行下面的getParam()方法；
    public String getParam(String name, Integer age) {
        System.out.println(name+":"+age);
        return "ok";
    }

}
