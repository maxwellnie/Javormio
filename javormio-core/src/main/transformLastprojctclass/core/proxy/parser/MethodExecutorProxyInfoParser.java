package com.maxwellnie.velox.sql.core.proxy.parser;

import com.maxwellnie.velox.sql.core.annotation.proxy.MethodInterceptor;
import com.maxwellnie.velox.sql.core.proxy.info.impl.SimpleMethodProxyInfo;
import com.maxwellnie.velox.sql.core.natives.exception.ProxyExtendsException;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect.SimpleInvocation;
import com.maxwellnie.velox.sql.core.proxy.info.MethodProxyInfo;
import com.maxwellnie.velox.sql.core.proxy.info.impl.ProxyInfo;
import com.maxwellnie.velox.sql.core.utils.reflect.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Maxwell Nie
 */
public class MethodExecutorProxyInfoParser implements ProxyInfoParser {
    @Override
    public ProxyInfo parse(Class<?> clazz) {
        try {
            return parse(ReflectionUtils.newInstance(clazz));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new ProxyExtendsException(e);
        }
    }

    @Override
    public ProxyInfo parse(Object proxyObject) {
        if (proxyObject == null)
            throw new ProxyExtendsException("Proxy obj is null");
        Class<?> clazz = proxyObject.getClass();
        ProxyInfo proxyInfo = new ProxyInfo();
        MethodProxyInfo[] methodProxyInfos = new MethodProxyInfo[0];
        Class<?>[] interfaces = new Class[]{MethodExecutor.class};
        for (Method method : clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(MethodInterceptor.class)){
                MethodProxyInfo methodProxyInfo = new SimpleMethodProxyInfo();
                methodProxyInfo.setMethodName(method.getName());
                methodProxyInfo.setParameterTypes(checkAndGetCorrectParameterTypes(method.getParameterTypes()));
                methodProxyInfo.setProxyObject(proxyObject);
                methodProxyInfo.setProxyMethod(method);
                methodProxyInfo.setReturnType(method.getReturnType());
                MethodProxyInfo[] newMethodProxyInfos = new MethodProxyInfo[methodProxyInfos.length + 1];
                System.arraycopy(methodProxyInfos, 0, newMethodProxyInfos, 0, methodProxyInfos.length);
                newMethodProxyInfos[methodProxyInfos.length] = methodProxyInfo;
                methodProxyInfos = newMethodProxyInfos;
            }
        }
        proxyInfo.setProxyObject(proxyObject);
        proxyInfo.setClazz(clazz);
        proxyInfo.setMethodProxyInfos(methodProxyInfos);
        proxyInfo.setInterfaces(interfaces);
        return proxyInfo;
    }

    private Class<?>[] checkAndGetCorrectParameterTypes(Class<?>[] parameterTypes) {
        if (parameterTypes == null || parameterTypes.length == 0)
            throw new ProxyExtendsException("Parameter types is null or empty,At least contains parameter[" + SimpleInvocation.class + "].");
        if (parameterTypes[0] != SimpleInvocation.class)
            throw new ProxyExtendsException("Parameter types is not correct,The first parameter must [" + SimpleInvocation.class + "].");
        Class<?>[] correctParameterTypes = new Class[parameterTypes.length - 1];
        System.arraycopy(parameterTypes, 1, correctParameterTypes, 0, parameterTypes.length - 1);
        return correctParameterTypes;
    }
}
