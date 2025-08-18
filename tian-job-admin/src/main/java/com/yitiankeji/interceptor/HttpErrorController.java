package com.yitiankeji.interceptor;

import com.yitiankeji.response.HttpResponse;
import com.yitiankeji.utils.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/** HTTP错误控制器类，用于处理HTTP请求错误并返回相应的错误信息 */
public class HttpErrorController {

    // 使用日志记录器记录错误日志
    private static final Logger logger = LoggerFactory.getLogger(HttpErrorController.class);

    private static final Map<String, String> ERROR_RESPONSE_MAP = Maps.of(
            "400", "系统错误",
            "403", "请求未经授权",
            "404", "网页未找到",
            "500", "服务器内部错误",
            "501", "服务器不具备完成请求的功能",
            "502", "路由错误");

    @RequestMapping(value = "/error")
    public HttpResponse<String> handleError(HttpServletRequest req, HttpServletResponse res) {
        String path = req.getAttribute("javax.servlet.error.request_uri").toString();
        String code = req.getAttribute("javax.servlet.error.status_code").toString();
        String message = req.getAttribute("javax.servlet.error.message").toString();

        logger.error("错误的请求-->code:{},message:{},path:{}", code, message, path);
        String errorMessage = ERROR_RESPONSE_MAP.get(code);
        return StringUtils.isNotEmpty(errorMessage) ? HttpResponse.error(errorMessage) : HttpResponse.error("服务器内部错误");
    }
}
