package io.github.maxwellnie.javormio.framework.common.java.proxy;

import io.github.maxwellnie.javormio.framework.common.java.api.Matcher;

import java.lang.reflect.Method;

/**
 * Matching any method.
 *
 * @author Maxwell Nie
 */
public class Signature implements Matcher<Method> {
    public static final Signature INSTANCE = new Signature();

    @Override
    public boolean matches(Method method) {
        return true;
    }

}
