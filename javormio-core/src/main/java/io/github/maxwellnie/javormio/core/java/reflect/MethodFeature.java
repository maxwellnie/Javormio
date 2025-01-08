package io.github.maxwellnie.javormio.core.java.reflect;

import java.lang.reflect.Method;

/**
 * 方法特征，保存了方法特征码，用于在映射中快速找到对应的方法对象和附带的信息
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
    final Method method;

    public MethodFeature(Method method) {
        this.method = method;
        this.methodFeatureCode = method.hashCode();;
    }

    public int getMethodFeatureCode() {
        return methodFeatureCode;
    }

    public Method getMethod() {
        return method;
    }
    /**
     * 判断是否是setter方法
     * @return boolean
     */
    public boolean isSetter(){
        return method.getName().startsWith("set");
    }
    /**
     * 判断是否是getter方法
     * @return boolean
     */
    public boolean isGetter(){
        return method.getName().startsWith("get");
    }
    /**
     * 判断是否是boolean getter方法
     * @return boolean
     */
    public boolean isBooleanGetter(){
        return method.getName().startsWith("is");
    }
}
