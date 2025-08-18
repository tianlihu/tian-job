package com.yitiankeji.executor.context;

import com.yitiankeji.executor.exception.JobException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class MethodJobHandler {

    private Object target;
    private Method method;

    public void execute(String executorParams) throws Exception {
        // 获取方法的参数类型数组
        Class<?>[] paramTypes = method.getParameterTypes();

        // 如果方法有参数
        if (paramTypes.length == 0) {
            // 如果方法没有参数，直接调用方法
            method.invoke(target);
            return;
        }
        if (paramTypes.length > 1) {
            throw new JobException("执行器方法参数最多只能1个");
        }
        if (!paramTypes[0].equals(String.class)) {
            throw new JobException("带参数的执行器方法，参数只能是字符串类型");
        }
        // 使用空对象数组作为参数调用方法
        // 注意：方法的参数不能是基本类型（如 int, boolean 等），
        // 因为基本类型不能为 null
        method.invoke(target, executorParams);
    }

    @Override
    public String toString() {
        return super.toString() + "[" + target.getClass() + "#" + method.getName() + "]";
    }
}
