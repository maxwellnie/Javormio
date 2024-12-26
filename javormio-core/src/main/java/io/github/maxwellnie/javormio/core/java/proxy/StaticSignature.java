package io.github.maxwellnie.javormio.core.java.proxy;

import io.github.maxwellnie.javormio.core.java.api.Matcher;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * The signature of a method, which is used to match the method in the proxy.<br/>
 * It can't be modified after creation.
 *
 * @author Maxwell Nie
 */
public class StaticSignature implements Matcher<Method> {
    final String methodName;
    final Class<?>[] parameters;
    final Class<?> returnType;

    public StaticSignature(String methodName, Class<?>[] parameters, Class<?> returnType) {
        this.methodName = methodName;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public boolean matches(Method method) {
        return (method.getName().equals(methodName)
                && method.getReturnType().equals(returnType)
                && Arrays.equals(method.getParameterTypes(), parameters));
    }
}
