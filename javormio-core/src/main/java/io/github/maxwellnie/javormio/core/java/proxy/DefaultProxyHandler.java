package io.github.maxwellnie.javormio.core.java.proxy;

import io.github.maxwellnie.javormio.core.java.proxy.invocation.InvocationLine;
import io.github.maxwellnie.javormio.core.java.proxy.invocation.InvokerContext;
import io.github.maxwellnie.javormio.core.java.proxy.invocation.MethodInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The default proxy implementation
 *
 * @author Maxwell Nie
 */
public class DefaultProxyHandler implements InvocationHandler {
    final Map<Integer, List<ProxyInfo>> proxyInfos;
    final Object target;
    /**
     * Cache the proxy method invocation line to avoid the cost of building the invocation line every time
     */
    final ConcurrentMap<Method, Object[]> proxyMethodInvocationLines = new ConcurrentHashMap<>();

    public DefaultProxyHandler(Map<Integer, List<ProxyInfo>> proxyInfos, Object target) {
        this.proxyInfos = proxyInfos;
        this.target = target;
    }

    /**
     * 1. If it is an Object method, call directly<br/>
     * 2. If it is a proxy class method, execute the proxy logic<br/>
     * 3. If it is a default method or normal method of the target object, call directly<br/>
     *
     * @return Object
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int methodId;
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else if (proxyMethodInvocationLines.containsKey(method)) {
            return invokeProxyMethod(method, args);
        } else if (proxyInfos.containsKey((methodId = Objects.hash(method.getName(), Arrays.hashCode(method.getParameterTypes()))))) {
            return invokeProxyMethod(method, args, methodId);
        } else if (method.isDefault())
            return JavaVersionAdaptor.handleDefaultMethod(method, target, args);
        else
            return method.invoke(target, args);
    }

    /**
     * Reads the cached invocation line and executes the proxy logic
     *
     * @param method Method to be invoked
     * @param args   method arguments
     * @return Object
     */
    private Object invokeProxyMethod(Method method, Object[] args) throws MethodInvocationException {
        Object[] invocation = proxyMethodInvocationLines.get(method);
        MethodInvoker invoker = (MethodInvoker) invocation[0];
        InvocationLine invocationLine = (InvocationLine) invocation[1];
        return invoker.invoke(invocationLine, new InvokerContext(target, args));
    }

    /**
     * Builds the invocation line and executes the proxy logic
     *
     * @param method   Method to be invoked
     * @param args     method arguments
     * @param methodId method hash
     * @return Object
     */
    private Object invokeProxyMethod(Method method, Object[] args, Integer methodId) throws MethodInvocationException {
        MethodInvoker invoker = new TargetMethodInvoker(method);
        InvocationLine invocationLine = new InvocationLine(null, null, method);
        Class<?> declaringClass = method.getDeclaringClass();
        for (ProxyInfo proxyInfo : proxyInfos.get(methodId)) {
            if (!declaringClass.equals(proxyInfo.targetParentClass))
                continue;
            invocationLine = new InvocationLine(invoker, invocationLine, method);
            invoker = new MethodExecutorInvoker(proxyInfo.methodExecutor);
        }
        proxyMethodInvocationLines.put(method, new Object[]{invoker, invocationLine});
        return invoker.invoke(invocationLine, new InvokerContext(target, args));
    }


    /**
     * Proxy method invoker that wraps the method executor
     */
    static class MethodExecutorInvoker implements MethodInvoker {
        final MethodExecutor methodExecutor;

        public MethodExecutorInvoker(MethodExecutor methodExecutor) {
            this.methodExecutor = methodExecutor;
        }

        @Override
        public Object invoke(InvokerContext context) throws MethodInvocationException {
            throw new MethodInvocationException("MethodExecutorInvoker.invoke() should not be called");
        }

        @Override
        public Object invoke(InvocationLine invocationLine, InvokerContext context) throws MethodInvocationException {
            return methodExecutor.execute(invocationLine, context);
        }
    }

    /**
     * Target method invoker
     */
    static class TargetMethodInvoker implements MethodInvoker {
        final Method method;

        public TargetMethodInvoker(Method method) {
            this.method = method;
        }

        @Override
        public Object invoke(InvokerContext context) throws MethodInvocationException {
            try {
                return method.invoke(context.getTarget(), context.getArgs());
            } catch (Throwable e) {
                throw new MethodInvocationException(e);
            }
        }

        @Override
        public Object invoke(InvocationLine invocationLine, InvokerContext context) throws MethodInvocationException {
            return invoke(context);
        }
    }
}
