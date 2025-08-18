package com.yitiankeji.interceptor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.yitiankeji.annotation.Json;
import com.yitiankeji.response.HttpResponse;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice(basePackages = {"com.yitiankeji"})
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        return !HttpResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        if (mediaType.includes(MediaType.APPLICATION_OCTET_STREAM)) {
            return response;
        }

        ServletServerHttpResponse resp = (ServletServerHttpResponse) response;
        resp.getServletResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (Page.class.isAssignableFrom(returnType.getParameterType())) {
            return HttpResponse.success((Page<?>) data);
        }

        Json json = returnType.getExecutable().getDeclaredAnnotation(Json.class);
        if (json == null) {
            if (String.class.isAssignableFrom(returnType.getParameterType())) {
                return toJson(HttpResponse.success(data));
            }
            return HttpResponse.success(data);
        }

        resp.getServletResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (data != null && isComplexClass(data.getClass()) && json.value()) {
            fill(data);
        }

        return data;
    }

    @SneakyThrows
    private void fill(Object data) {
        if (data == null) {
            return;
        }
        List<Field> fields = getFields(data);
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            Object value = field.get(data);
            if (value != null) {
                if (value instanceof Collection) {
                    Collection<?> items = (Collection<?>) value;
                    for (Object item : items) {
                        fill(item);
                    }
                    continue;
                }
                if (value instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) value;
                    Set<? extends Map.Entry<?, ?>> entries = map.entrySet();
                    for (Map.Entry<?, ?> entry : entries) {
                        Object key = entry.getKey();
                        Object val = entry.getValue();
                        if (key != null && isComplexClass(key.getClass())) {
                            fill(key);
                        }
                        if (val != null && isComplexClass(val.getClass())) {
                            fill(val);
                        }
                    }
                    continue;
                }

                if (isComplexClass(type)) {
                    fill(value);
                }
                continue;
            }

            if (String.class.equals(type)) {
                field.set(data, "");
            } else if (Byte.class.equals(type)) {
                field.set(data, (byte) 0);
            } else if (Short.class.equals(type)) {
                field.set(data, (short) 0);
            } else if (Character.class.equals(type)) {
                field.set(data, (char) 0);
            } else if (Integer.class.equals(type)) {
                field.set(data, 0);
            } else if (Long.class.equals(type)) {
                field.set(data, 0L);
            } else if (Float.class.equals(type)) {
                field.set(data, 0.0F);
            } else if (Double.class.equals(type)) {
                field.set(data, 0.0D);
            } else if (BigDecimal.class.equals(type)) {
                field.set(data, BigDecimal.ZERO);
            } else if (List.class.equals(type)) {
                field.set(data, new ArrayList<>());
            } else if (ArrayList.class.equals(type)) {
                field.set(data, new ArrayList<>());
            } else if (LinkedList.class.equals(type)) {
                field.set(data, new LinkedList<>());
            } else if (Set.class.equals(type)) {
                field.set(data, new HashSet<>());
            } else if (HashSet.class.equals(type)) {
                field.set(data, new HashSet<>());
            } else if (SortedSet.class.equals(type)) {
                field.set(data, new TreeSet<>());
            } else if (TreeSet.class.equals(type)) {
                field.set(data, new TreeSet<>());
            } else if (Map.class.equals(type)) {
                field.set(data, new HashMap<>());
            } else if (HashMap.class.equals(type)) {
                field.set(data, new HashMap<>());
            } else if (SortedMap.class.equals(type)) {
                field.set(data, new TreeMap<>());
            } else if (TreeMap.class.equals(type)) {
                field.set(data, new TreeMap<>());
            } else if (LinkedHashMap.class.equals(type)) {
                field.set(data, new LinkedHashMap<>());
            } else if (ConcurrentHashMap.class.equals(type)) {
                field.set(data, new ConcurrentHashMap<>());
            }
        }
    }

    private boolean isComplexClass(Class<?> type) {
        if (String.class.equals(type)) {
            return false;
        } else if (byte.class.equals(type) || short.class.equals(type) || char.class.equals(type) || int.class.equals(type) || long.class.equals(type)) {
            return false;
        } else if (Byte.class.equals(type) || Short.class.equals(type) || Character.class.equals(type) || Integer.class.equals(type) || Long.class.equals(type)) {
            return false;
        } else if (float.class.equals(type) || double.class.equals(type)) {
            return false;
        } else if (Float.class.equals(type) || Double.class.equals(type)) {
            return false;
        } else if (BigDecimal.class.equals(type)) {
            return false;
        } else {
            return !Date.class.equals(type);
        }
    }

    private static List<Field> getFields(Object data) {
        List<Field> fields = new ArrayList<>();

        Class<?> clazz = data.getClass();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    @SneakyThrows
    @SuppressWarnings("deprecation")
    private String toJson(Object value) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.LowerCamelCaseStrategy());
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        return objectMapper.writeValueAsString(value);
    }
}
