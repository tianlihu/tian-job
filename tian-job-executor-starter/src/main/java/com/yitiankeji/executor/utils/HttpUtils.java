package com.yitiankeji.executor.utils;

import com.alibaba.fastjson.JSONObject;
import com.yitiankeji.executor.response.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;

import javax.net.ssl.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtils {

    private static SSLSocketFactory socketFactory;

    static {
        try {
            class TrustAllTrustManager implements TrustManager, X509TrustManager {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }

            //  直接通过主机认证
            HostnameVerifier hv = (urlHostName, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            //  配置认证管理器
            TrustManager[] trustAllCerts = {new TrustAllTrustManager()};
            SSLContext sc = SSLContext.getInstance("SSL");
            SSLSessionContext sslsc = sc.getServerSessionContext();
            sslsc.setSessionTimeout(0);
            sc.init(null, trustAllCerts, null);
            socketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            log.error("生成证书错误", e);
        }
    }

    public static HttpResult post(String url, Map<String, String> headers, Map<String, Object> json, Serializable... data) {
        Map<String, String> params = convertParams(data);
        try {
            if (json == null) json = Collections.emptyMap();
            if (headers == null) {
                headers = new HashMap<>();
            } else {
                headers = new HashMap<>(headers);
            }
            if (!json.isEmpty()) {
                headers.put("Content-Type", "application/json");
            }
            String result = Jsoup.connect(url).sslSocketFactory(socketFactory).ignoreContentType(true).headers(headers).requestBody(JSONObject.toJSONString(json)).data(params).post().body().text();
            log.debug("[api-url]: {}, [api-reqParams]: {}, [api-data]: {}, [api-response]]: {}", url, JSONObject.toJSONString(params), JSONObject.toJSONString(json), result);
            return new HttpResult(JSONObject.parseObject(result));
        } catch (Exception e) {
            log.error("[api-url]: {}, [api-reqParams]: {}, [api-data] {}, [api-exception]: {}", url, JSONObject.toJSONString(params), JSONObject.toJSONString(json), ExceptionUtils.getStackTrace(e));
            return HttpResult.error(e.getMessage());
        }
    }

    private static Map<String, String> convertParams(Object[] data) {
        if (data.length % 2 != 0) {
            throw new RuntimeException("请求参数和值必须成对出现");
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < data.length; i += 2) {
            String key = (String) data[i];
            if (StringUtils.isEmpty(key)) {
                throw new RuntimeException("请求参数名不能为空");
            }

            Object value = data[i + 1];
            if (value != null) {
                params.put(key, value.toString());
            }
        }
        return params;
    }
}
