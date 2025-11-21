package com.yclin.quiz.model.domain;

/**
 * 统一返回结果封装类
 *
 * code: 1 表示成功，0 表示失败
 * msg : 成功/失败的描述信息
 * data: 放任意数据
 */
public class Result implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public static final Integer SUCCESS = 1;
    public static final Integer FAIL = 0;

    private Integer code; // 1 成功，0 失败
    private String msg;   // 成功消息或失败消息
    private Object data;  // 放数据

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // getter / setter
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // toString
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    // 静态工厂方法 - success
    public static Result success() {
        return new Result(SUCCESS, "success", null);
    }

    public static Result success(String msg) {
        return new Result(SUCCESS, msg, null);
    }

    public static Result success(Object data) {
        return new Result(SUCCESS, "success", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(SUCCESS, msg, data);
    }

    // 静态工厂方法 - error
    public static Result error() {
        return new Result(FAIL, "error", null);
    }

    public static Result error(String msg) {
        return new Result(FAIL, msg, null);
    }

    public static Result error(Object data) {
        return new Result(FAIL, "error", data);
    }

    public static Result error(String msg, Object data) {
        return new Result(FAIL, msg, data);
    }
}