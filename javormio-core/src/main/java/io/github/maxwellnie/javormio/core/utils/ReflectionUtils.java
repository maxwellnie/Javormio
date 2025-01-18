package io.github.maxwellnie.javormio.core.utils;

import io.github.maxwellnie.javormio.core.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.core.java.proxy.invocation.TargetMethodInvoker;
import io.github.maxwellnie.javormio.core.java.reflect.DefaultObjectFactory;
import io.github.maxwellnie.javormio.core.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.property.MetaField;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 * @author Maxwell Nie
 */
public class ReflectionUtils {
    private static final Map<Class<?>, Reflection<?>> REFLECTION_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, ObjectFactory<?>> OBJECT_FACTORY_MAP = new ConcurrentHashMap<>();
    /**
     * 获取反射对象
     * @param clazz
     * @param <T>
     * @return Reflection
     */
    public static <T> Reflection<T> getReflection(Class<T> clazz) {
        Reflection<?> reflection = REFLECTION_MAP.get(clazz);
        if (reflection == null) {
            synchronized (REFLECTION_MAP) {
                reflection = REFLECTION_MAP.computeIfAbsent(clazz, k -> new CachedReflection<>(clazz));
            }
        }
        return (Reflection<T>) reflection;
    }
    /**
     * 获取对象工厂<br/>
     * <p>支持了集合、map、实体类</p>
     * @param clazz
     * @param <T>
     * @return ObjectFactory
     * @throws NoSuchMethodException
     */
    public static <T> ObjectFactory<T> getObjectFactory(Class<?> clazz) throws NoSuchMethodException {
        ObjectFactory<?> objectFactory = OBJECT_FACTORY_MAP.get(clazz);
        if (objectFactory == null) {
            synchronized (OBJECT_FACTORY_MAP) {
                if ((objectFactory = OBJECT_FACTORY_MAP.get(clazz)) == null) {
                    objectFactory = createObjectFactory(clazz);
                    OBJECT_FACTORY_MAP.put(clazz, objectFactory);
                }
            }
        }
        return (ObjectFactory<T>) objectFactory;
    }
    /**
     * 创建对象工厂
     * @param clazz
     * @param <T>
     * @return ObjectFactory
     * @throws NoSuchMethodException
     */
    static <T> ObjectFactory<T> createObjectFactory(Class<?> clazz) throws NoSuchMethodException {
        if (clazz.isInterface()){
            if (TypeUtils.isCollection(clazz)){
                if (TypeUtils.isList(clazz))
                    return new DefaultObjectFactory<>(LinkedList.class);
                else if (TypeUtils.isSet(clazz)) {
                    return new DefaultObjectFactory<>(HashSet.class);
                }
                    return new DefaultObjectFactory<>(HashMap.class);
            }else if (TypeUtils.isMap(clazz))
                return new DefaultObjectFactory<>(LinkedHashMap.class);
            else
                throw new NoSuchMethodException("The class " + clazz.getName() + " is an interface, and cannot be used as a instantiable type.");
        } else if (clazz.isArray()) {
            throw  new NoSuchMethodException("The class " + clazz.getName() + " is an array, and cannot be used as a instantiable type.");
        }else{
            return new DefaultObjectFactory<>(clazz);
        }
    }
    /**
     * 缓存反射
     * @param <T>
     */
    static class CachedReflection<T> implements Reflection<T> {
        /**
         * 类
         */
        final Class<?> declaringClass;
        /**
         * 字段缓存
         */
        final Map<String, MetaField> metaFieldMap = new ConcurrentHashMap<>();
        /**
         * 方法缓存
         */
        final Map<Integer, Method> methodMap = new ConcurrentHashMap<>();

        public CachedReflection(Class<?> declaringClass) {
            this.declaringClass = declaringClass;
        }
        @Override
        public MethodInvoker getInvoker(String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
            return new TargetMethodInvoker(getMethod(name, parameterTypes));
        }

        @Override
        public MetaField getMetaField(String name) throws NoSuchMethodException, NoSuchFieldException{
            MetaField metaField = metaFieldMap.get(name);
            if (metaField == null){
                synchronized (metaFieldMap) {
                    if ((metaField = metaFieldMap.get(name)) == null){
                        Field field = this.declaringClass.getField(name);
                        metaField = new MetaField(field ,
                                getInvoker("set" + name.substring(0, 1).toUpperCase() + name.substring(1),
                                        new Class<?>[]{field.getType()}),
                                getInvoker("get" + name.substring(0, 1).toUpperCase() + name.substring(1),
                                        new Class<?>[0])
                        );
                        metaFieldMap.put(name, metaField);
                    }
                }
            }
            return metaField;
        }

        @Override
        public Method getMethod(String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
            int methodKey = Objects.hash(name.hashCode(), Arrays.hashCode(parameterTypes));
            Method method = methodMap.get(methodKey);
            if (method == null) {
                synchronized (methodMap) {
                    if((method = methodMap.get(methodKey)) == null){
                        method = this.declaringClass.getMethod(name, parameterTypes);
                        methodMap.put(methodKey, method);
                    }
                }
            }
            return method;
        }

        @Override
        public ObjectFactory<T> getObjectFactory() throws NoSuchMethodException {
            return ReflectionUtils.getObjectFactory(this.declaringClass);
        }

        @Override
        public Class<?> getDeclaringClass() {
            return this.declaringClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CachedReflection<?> that = (CachedReflection<?>) o;
            return Objects.equals(declaringClass, that.declaringClass);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(declaringClass);
        }
    }
}
