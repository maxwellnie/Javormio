package com.maxwellnie.velox.sql.core.proxy.factory;

import com.maxwellnie.velox.sql.core.config.Configuration;
import com.maxwellnie.velox.sql.core.proxy.info.MethodProxyInfo;
import com.maxwellnie.velox.sql.core.utils.framework.MethodExecutorUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 代理对象
 *
 * @author Maxwell Nie
 */
public class SimpleProxy implements InvocationHandler {
    final Map<String, MethodProxyInfo> proxyInfoMap;
    final Object target;

    public SimpleProxy(Map<String, MethodProxyInfo> proxyInfoMap, Object target) {
        this.proxyInfoMap = proxyInfoMap;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodProxyKey = MethodExecutorUtils.getMethodDeclaredName(method);
        if (Object.class.equals(method.getDeclaringClass())) {

            return method.invoke(this, args);

        } else if (proxyInfoMap.containsKey(methodProxyKey)) {

            MethodProxyInfo methodProxyInfo = proxyInfoMap.get(methodProxyKey);
            return methodProxyInfo.getProxyMethod().invoke(methodProxyInfo.getProxyObject(), args);

        } else if (method.isDefault())

            return Configuration.JavaByteCodeConfiguration.handleDefaultMethod(method, target, args);

        else

            return method.invoke(target, args);
    }
}
