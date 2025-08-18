package com.yitiankeji.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class HttpResult {

    public JSONObject data;
    public boolean error;
    public String errorMessage;

    public HttpResult(JSONObject data) {
        this.data = data;
    }

    public Integer getInteger(String... keys) {
        JSONObject temp = getLastJsonObject(keys);
        if (temp == null) return null;

        String key = getKey(keys, keys.length - 1);
        return temp.getInteger(key);
    }

    public String getString(String... keys) {
        JSONObject temp = getLastJsonObject(keys);
        if (temp == null) return null;

        String key = getKey(keys, keys.length - 1);
        return temp.getString(key);
    }

    public <T> T getObject(Class<T> clazz, String... keys) {
        JSONObject temp = getLastJsonObject(keys);
        if (temp == null) return null;

        String key = getKey(keys, keys.length - 1);
        return temp.getObject(key, clazz);
    }

    public JSONArray getJsonArray(String... keys) {
        JSONObject temp = getLastJsonObject(keys);
        if (temp == null) return null;

        String key = getKey(keys, keys.length - 1);
        return temp.getJSONArray(key);
    }

    public JSONObject getJsonObject(String... keys) {
        JSONObject temp = getLastJsonObject(keys);
        if (temp == null) return null;

        String key = getKey(keys, keys.length - 1);
        return temp.getJSONObject(key);
    }

    private JSONObject getLastJsonObject(String[] keys) {
        JSONObject temp = data;
        for (int i = 0; i < keys.length - 1; i++) {
            if (temp == null) return null;
            String key = getKey(keys, i);
            temp = data.getJSONObject(key);
        }
        return temp;
    }

    private String getKey(String[] keys, int index) {
        String key = keys[index];
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("参数不能有空值");
        }
        return key;
    }

    public static HttpResult error(String errorMessage) {
        HttpResult result = new HttpResult();
        result.setError(true);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
