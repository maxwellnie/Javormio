package com.maxwellnie.velox.sql.core.proxy.info;

import java.lang.reflect.Method;

/**
 * 代理方法信息
 *
 * @author Maxwell Nie
 */
public interface MethodProxyInfo {
    String getMethodName();

    void setMethodName(String methodName);

    Class<?>[] getParameterTypes();

    void setParameterTypes(Class<?>[] parameterTypes);

    Class<?> getReturnType();

    void setReturnType(Class<?> returnType);

    Method getProxyMethod();

    void setProxyMethod(Method method);

    Object getProxyObject();

    void setProxyObject(Object proxyObject);
}
