package com.maxwellnie.velox.sql.core.proxy.factory;

import com.maxwellnie.velox.sql.core.config.Configuration;
import com.maxwellnie.velox.sql.core.natives.exception.ProxyExtendsException;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect.SimpleInvocation;
import com.maxwellnie.velox.sql.core.proxy.info.MethodProxyInfo;
import com.maxwellnie.velox.sql.core.proxy.info.impl.ProxyInfo;
import com.maxwellnie.velox.sql.core.utils.framework.MethodExecutorUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class MethodExecutorProxyObjectFactory implements ProxyObjectFactory<MethodExecutor> {
    @Override
    public MethodExecutor produce(Object target, ProxyInfo proxyInfo) {
        Class<?>[] interfaceClasses = proxyInfo.getInterfaces();
        MethodProxyInfo[] methodProxyInfos = proxyInfo.getMethodProxyInfos();
        if (interfaceClasses == null)
            throw new ProxyExtendsException("interfaceClass is null");
        if (methodProxyInfos.length == 0) {
            return (MethodExecutor) target;
        }
        Map<String, MethodProxyInfo> proxyInfoMap = new LinkedHashMap<>();
        for (MethodProxyInfo methodProxyInfo : methodProxyInfos) {
            proxyInfoMap.put(MethodExecutorUtils.getMethodDeclaredName(methodProxyInfo.getMethodName(), methodProxyInfo.getParameterTypes()), methodProxyInfo);
        }
        return (MethodExecutor) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                interfaceClasses,
                new MethodExecutorProxy(proxyInfoMap, target));
    }

    public static class MethodExecutorProxy extends SimpleProxy {

        public MethodExecutorProxy(Map<String, MethodProxyInfo> proxyInfoMap, Object target) {
            super(proxyInfoMap, target);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodProxyKey = MethodExecutorUtils.getMethodDeclaredName(method);
            if (Object.class.equals(method.getDeclaringClass())) {

                return method.invoke(this, args);

            } else if (proxyInfoMap.containsKey(methodProxyKey)) {

                MethodProxyInfo methodProxyInfo = proxyInfoMap.get(methodProxyKey);
                SimpleInvocation invocation = new SimpleInvocation(target, proxy, args, method);
                if (args == null || args.length == 0)
                    args = new Object[]{invocation};
                else {
                    Object[] newArgs = new Object[args.length + 1];
                    System.arraycopy(args, 0, newArgs, 1, args.length);
                    newArgs[0] = invocation;
                    args = newArgs;
                }
                return methodProxyInfo.getProxyMethod().invoke(methodProxyInfo.getProxyObject(), args);

            } else if (method.isDefault())

                return Configuration.JavaByteCodeConfiguration.handleDefaultMethod(method, target, args);

            else

                return method.invoke(target, args);
        }
    }
}
