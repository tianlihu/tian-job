package com.yitiankeji.interceptor;

import com.yitiankeji.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/** 全局异常处理器 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 进行全局异常的过滤和处理 **/
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public HttpResponse<String> defaultErrorHandler(HttpServletRequest req, Exception ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return HttpResponse.error(ex.getLocalizedMessage());
    }
}
