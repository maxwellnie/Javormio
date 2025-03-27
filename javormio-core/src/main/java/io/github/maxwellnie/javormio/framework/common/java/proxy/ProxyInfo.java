package io.github.maxwellnie.javormio.framework.common.java.proxy;

import io.github.maxwellnie.javormio.framework.common.java.api.Matcher;

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

    public ProxyInfo(Class<?> targetParentClass, Matcher<Method> methodMatcher, MethodExecutor methodExecutor, int level) {
        this.targetParentClass = targetParentClass;
        this.methodMatcher = methodMatcher;
        this.methodExecutor = methodExecutor;
        this.level = level;
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
        return Integer.compare(level, o.level);
    }
}
