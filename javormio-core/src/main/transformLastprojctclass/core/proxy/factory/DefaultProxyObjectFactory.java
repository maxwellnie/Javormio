package com.maxwellnie.velox.sql.core.proxy.factory;

import com.maxwellnie.velox.sql.core.proxy.info.MethodProxyInfo;
import com.maxwellnie.velox.sql.core.proxy.info.impl.ProxyInfo;
import com.maxwellnie.velox.sql.core.natives.exception.ProxyExtendsException;
import com.maxwellnie.velox.sql.core.utils.framework.MethodExecutorUtils;

import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class DefaultProxyObjectFactory implements ProxyObjectFactory<Object> {
    @Override
    public Object produce(Object target, ProxyInfo proxyInfo) {
        Class<?> [] interfaceClasses = proxyInfo.getInterfaces();
        MethodProxyInfo[] methodProxyInfos = proxyInfo.getMethodProxyInfos();
        if (interfaceClasses == null)
            throw new ProxyExtendsException("interfaceClass is null");
        if (methodProxyInfos.length == 0) {
            return target;
        }
        checkInterfaces(interfaceClasses);
        Map<String, MethodProxyInfo> proxyInfoMap = new LinkedHashMap<>();
        for (MethodProxyInfo methodProxyInfo : methodProxyInfos) {
            proxyInfoMap.put(MethodExecutorUtils.getMethodDeclaredName(methodProxyInfo.getMethodName(), methodProxyInfo.getParameterTypes()), methodProxyInfo);
        }
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                interfaceClasses,
                new SimpleProxy(proxyInfoMap, target));
    }
    private void checkInterfaces(Class<?>[] interfaceClasses) throws ProxyExtendsException{
        for (Class<?> interfaceClass : interfaceClasses){
            if(interfaceClass == null)
                throw new ProxyExtendsException("interfaceClass is null");
            if (!interfaceClass.isInterface())
                throw new ProxyExtendsException(interfaceClass.getName() + " is not interface");
        }
    }
}

