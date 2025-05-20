package io.github.maxwellnie.javormio.common.java.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 方法特征，保存了方法特征码，用于在映射中快速找到对应的方法对象和附带的信息
 *
 * @author Maxwell Nie
 */
public class MethodFeature {
    /**
     * 方法特征码，一般是方法对象的hash
     */
    final int methodFeatureCode;
    /**
     * 方法对象
     */
    final Class<?> superClass;
    final String name;
    final Class<?>[]  parameterTypes;
    final Class<?> returnType;

    public MethodFeature(Method method) {
        this.superClass = method==null? null: method.getDeclaringClass();
        this.name = method==null? null: method.getName();
        this.parameterTypes = method==null? null: method.getParameterTypes();
        this.returnType = method==null? null: method.getReturnType();
        this.methodFeatureCode = method==null? 0: method.hashCode();
    }

    public MethodFeature(Method method, int methodFeatureCode) {
        this.methodFeatureCode = methodFeatureCode;
        this.superClass = method==null? null: method.getDeclaringClass();
        this.name = method==null? null: method.getName();
        this.parameterTypes = method==null? null: method.getParameterTypes();
        this.returnType = method==null? null: method.getReturnType();
    }

    public int getMethodFeatureCode() {
        return methodFeatureCode;
    }

    public Class<?> getSuperClass() {
        return superClass;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodFeature that = (MethodFeature) o;
        return methodFeatureCode == that.methodFeatureCode ;
    }

    @Override
    public int hashCode() {
        return methodFeatureCode;
    }
}
