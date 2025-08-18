package com.yitiankeji.job.response;

import lombok.Data;

/**
 * 统一响应结果封装类
 * 用于封装API接口返回的数据格式
 *
 * @param <T> 泛型，表示返回的数据类型
 */
@Data
public class HttpResponse<T> {

    /** 成功状态码 **/
    public static final String SUCCESS_CODE = "200";
    /** 失败状态码 **/
    public static final String FAILURE_CODE = "500";
    /** 禁止访问状态码 **/
    public static final String FORBIDDING_CODE = "401";
    /** 权限不足状态码 **/
    public static final String PERMISSION_CODE = "403";

    /** 成功提示信息 **/
    public static final String SUCCESS_MESSAGE = "请求成功";
    /** 失败提示信息 **/
    public static final String FAILURE_MESSAGE = "请求失败";

    /** 是否成功 **/
    private boolean error;
    /** 结果代码 **/
    private String code = SUCCESS_CODE;
    /** 结果信息 **/
    private String message;
    /** 数据 **/
    private T data;
    /** 总记录数 **/
    private long total;
    /** 当前页码 **/
    private long page;

    public static <T> HttpResponse<T> success() {
        return success(SUCCESS_MESSAGE, null);
    }

    public static <T> HttpResponse<T> success(String message) {
        return success(message, null);
    }

    public static <T> HttpResponse<T> success(String message, T data) {
        HttpResponse<T> response = new HttpResponse<>();
        response.setError(false);
        response.setCode(SUCCESS_CODE);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> HttpResponse<T> success(T data) {
        return success(SUCCESS_MESSAGE, data);
    }

    public static <T> HttpResponse<T> error(String code, String message) {
        return error(code, message, null);
    }

    public static <T> HttpResponse<T> error(String message) {
        return error(FAILURE_CODE, message);
    }

    public static <T> HttpResponse<T> error(String message, T data) {
        return error(FAILURE_CODE, message, data);
    }

    public static <T> HttpResponse<T> error(String code, String message, T data) {
        HttpResponse<T> response = new HttpResponse<>();
        response.setError(true);
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
