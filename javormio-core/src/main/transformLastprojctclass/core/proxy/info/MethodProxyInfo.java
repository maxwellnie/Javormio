package com.maxwellnie.velox.sql.core.proxy.info;

import java.lang.reflect.Method;

/**
 * 代理方法信息
 * @author Maxwell Nie
 */
public interface MethodProxyInfo {
    void setMethodName(String methodName);
    String getMethodName();
    void setParameterTypes(Class<?>[] parameterTypes);
    Class<?>[] getParameterTypes();
    void setReturnType(Class<?> returnType);
    Class<?> getReturnType();
    void setProxyMethod(Method method);
    Method getProxyMethod();
    void setProxyObject(Object proxyObject);
    Object getProxyObject();
}
