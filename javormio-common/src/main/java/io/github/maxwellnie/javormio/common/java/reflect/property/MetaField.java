package io.github.maxwellnie.javormio.common.java.reflect.property;

import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.MethodInvoker;

import java.lang.reflect.Field;

/**
 * @author Maxwell Nie
 */
public class MetaField<E, T> {
    private final String name;
    private final Class<T> type;
    private final Class<E> declaringClass;
    private final MethodInvoker<E, T> getter;
    private final MethodInvoker<E, Object> setter;

    public MetaField(String name, Class<T> type, Class<E> declaringClass, MethodInvoker<E,  T> getter, MethodInvoker<E, Object> setter) {
        this.name = name;
        this.type = type;
        this.declaringClass = declaringClass;
        this.getter = getter;
        this.setter = setter;
    }
    @SuppressWarnings("unchecked")
    public MetaField(Field field, MethodInvoker<E,  T> getter, MethodInvoker<E, Object> setter) {
        this.name = field.getName();
        this.type = (Class<T>) field.getType();
        this.declaringClass = (Class<E>) field.getDeclaringClass();
        this.getter = getter;
        this.setter = setter;
    }
    /**
     * 设置对象的属性值
     *
     * @param bean
     * @param value
     * @throws MethodInvocationException
     */
    public void set(E bean, T value) throws MethodInvocationException{
        if (setter == null) {
            throw new MethodInvocationException("setter not found for " + name);
        }
        setter.invokeExactly(bean, value);
    }

    /**
     * 获取对象的属性值
     *
     * @param bean
     * @return
     * @throws MethodInvocationException
     */
    public Object get(E bean) throws MethodInvocationException {
        if (getter == null) {
            throw new MethodInvocationException("getter not found for " + name);
        }
        return getter.invoke(bean);
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public Class<E> getDeclaringClass() {
        return declaringClass;
    }

    public MethodInvoker<E, T> getGetter() {
        return getter;
    }

    public MethodInvoker<E, Object> getSetter() {
        return setter;
    }
}
