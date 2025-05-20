package io.github.maxwellnie.javormio.common.java.proxy;

import io.github.maxwellnie.javormio.common.java.api.Matcher;

import java.lang.reflect.Method;

/**
 * The profile about Target, Target method, Method Executor and Level.
 *
 * @author Maxwell Nie
 */
public class ProxyInfo implements Comparable<ProxyInfo> {
    final Class<?> targetParentClass;
    final Matcher<Method> methodMatcher;
    final MethodExecutor methodExecutor;
    final int level;
    final Class<?> proxyDefinedClass;


    public ProxyInfo(Class<?> targetParentClass, Matcher<Method> methodMatcher, MethodExecutor methodExecutor, int level, Class<?> proxyDefinedClass) {
        this.targetParentClass = targetParentClass;
        this.methodMatcher = methodMatcher;
        this.methodExecutor = methodExecutor;
        this.level = level;
        this.proxyDefinedClass = proxyDefinedClass;
    }

    public Class<?> getTargetParentClass() {
        return targetParentClass;
    }

    public Class<?> getProxyDefinedClass() {
        return proxyDefinedClass;
    }

    public Class<?> getTargetSuperClass() {
        return targetParentClass;
    }

    public Matcher<Method> getMethodMatcher() {
        return methodMatcher;
    }

    public MethodExecutor getMethodExecutor() {
        return methodExecutor;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int compareTo(ProxyInfo o) {
        if (level == o.level)
            throw new IllegalArgumentException("The level of two ProxyInfo cannot be equal.Proxy0:"+this+"Proxy1:"+o);
        return Integer.compare(level, o.level);
    }

    @Override
    public String toString() {
        return "ProxyInfo{" +
                "targetParentClass=" + targetParentClass +
                ", methodMatcher=" + methodMatcher +
                ", methodExecutor=" + methodExecutor +
                ", level=" + level +
                ", proxyDefinedClass=" + proxyDefinedClass +
                '}';
    }
}
