package io.github.maxwellnie.javormio.common.java.proxy;

import io.github.maxwellnie.javormio.common.java.api.Matcher;

import java.lang.reflect.Method;

/**
 * @author Maxwell Nie
 */
public interface Enhance extends MethodExecutor {
    Matcher<Method> getMatcher();
}
