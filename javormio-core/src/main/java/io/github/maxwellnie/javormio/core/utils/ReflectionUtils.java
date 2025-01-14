package io.github.maxwellnie.javormio.core.utils;

import io.github.maxwellnie.javormio.core.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.property.MetaField;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Maxwell Nie
 */
public class ReflectionUtils {
    private static final Map<Class<?>, Reflection<?>> REFLECTION_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, ObjectFactory<?>> OBJECT_FACTORY_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, MetaField> META_FIELD_MAP = new ConcurrentHashMap<>();
    public static <T> Reflection<T> getReflection(Class<T> clazz) {
        Reflection<?> reflection = REFLECTION_MAP.get(clazz);
        if (reflection == null) {
            synchronized (REFLECTION_MAP) {
                reflection = REFLECTION_MAP.computeIfAbsent(clazz, k -> new SimpleReflection<>());
            }
        }
        return (Reflection<T>) reflection;
    }
    static class SimpleReflection<T> implements Reflection<T> {
        @Override
        public MethodInvoker getInvoker(String name, Class<?>[] parameterTypes) {
            return null;
        }

        @Override
        public MetaField getMetaField(String name) {
            return null;
        }

        @Override
        public Method getMethod(String name, Class<?>[] parameterTypes, boolean deepSearch) {
            return null;
        }

        @Override
        public ObjectFactory<T> getObjectFactory() {
            return null;
        }

        @Override
        public Class<?> getDeclaringClass() {
            return null;
        }
    }
}
