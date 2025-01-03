package io.github.maxwellnie.javormio.core.java.proxy;

import io.github.maxwellnie.javormio.core.java.api.Matcher;

import java.lang.reflect.Method;

/**
 * @author Maxwell Nie
 */
public interface Enhance extends MethodExecutor {
    Matcher<Method> getMatcher();
}
