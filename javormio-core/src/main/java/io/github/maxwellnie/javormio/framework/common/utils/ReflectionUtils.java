package io.github.maxwellnie.javormio.framework.common.utils;

import io.github.maxwellnie.javormio.framework.core.api.dynamic.SerializableFunction;
import io.github.maxwellnie.javormio.framework.common.java.proxy.invocation.MethodInvoker;
import io.github.maxwellnie.javormio.framework.common.java.proxy.invocation.TargetMethodInvoker;
import io.github.maxwellnie.javormio.framework.common.java.reflect.ArrayObjectFactory;
import io.github.maxwellnie.javormio.framework.common.java.reflect.DefaultObjectFactory;
import io.github.maxwellnie.javormio.framework.common.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.framework.common.java.reflect.Reflection;
import io.github.maxwellnie.javormio.framework.common.java.reflect.property.MetaField;
import io.github.maxwellnie.javormio.framework.common.java.type.map.AbstractImmutableMap;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 *
 * @author Maxwell Nie
 */
public class ReflectionUtils {
    private static final Map<Class<?>, Reflection<?>> REFLECTION_MAP = new ConcurrentHashMap<>();
    private static final Map<Class<?>, ObjectFactory<?>> OBJECT_FACTORY_MAP = new ConcurrentHashMap<>();
    private static final Map<SerializableFunction<?, ?>, String> METHOD_NAME_CACHE = new ConcurrentHashMap<>();
    private static final Map<Integer, Object> FUNCTION_INTERFACE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取Getter方法名
     *
     * @param getter Getter方法
     * @param <T>    实体类对象
     * @param <R>    Getter方法返回值类型
     * @return String
     * @
     */
    public static <T, R> String getMethodName(SerializableFunction<T, R> getter) throws ReflectiveOperationException {
        if (METHOD_NAME_CACHE.containsKey(getter))
            return METHOD_NAME_CACHE.get(getter);
        else {
            if (METHOD_NAME_CACHE.containsKey(getter))
                return METHOD_NAME_CACHE.get(getter);
            Method writeReplace = getter.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(getter);
            String name = serializedLambda.getImplMethodName();
            METHOD_NAME_CACHE.put(getter, name);
            return name;
        }
    }

    /**
     * 获取反射对象
     *
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
     *
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
     *
     * @param clazz
     * @param <T>
     * @return ObjectFactory
     * @throws NoSuchMethodException
     * @throws
     */
    static <T> ObjectFactory<T> createObjectFactory(Class<T> clazz) throws NoSuchMethodException, IllegalArgumentException {
        if (clazz.isInterface()) {
            if (TypeUtils.isCollection(clazz)) {
                if (TypeUtils.isList(clazz))
                    return new DefaultObjectFactory<>(LinkedList.class);
                else if (TypeUtils.isSet(clazz)) {
                    return new DefaultObjectFactory<>(HashSet.class);
                }
                throw new IllegalArgumentException("The class " + clazz.getName() + " is an interface, and cannot be used as a instantiable type.");
            } else if (TypeUtils.isMap(clazz))
                return new DefaultObjectFactory<>(LinkedHashMap.class);
            else
                throw new IllegalArgumentException("The class " + clazz.getName() + " is an interface, and cannot be used as a instantiable type.");
        } else if (clazz.isArray()) {
            return (ObjectFactory<T>) new ArrayObjectFactory<>(clazz.getComponentType(), 0);
        } else {
            return new DefaultObjectFactory<>(clazz);
        }
    }

    /**
     * 缓存反射
     *
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
         * 缓存锁
         */
        final Object lock = new Object();
        /**
         * 方法缓存
         */
        final Map<Integer, Method> methodMap = new ConcurrentHashMap<>();
        /**
         * 所有字段缓存
         */
        Map<String, MetaField> allMetaFieldMap = new LinkedHashMap<>();
        /**
         * 资源占用标志位
         */
        boolean occupied = false;

        public CachedReflection(Class<?> declaringClass) {
            this.declaringClass = declaringClass;
        }

        @Override
        public MethodInvoker getInvoker(String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
            return new TargetMethodInvoker(getMethod(name, parameterTypes));
        }

        @Override
        public MetaField getMetaField(String name, boolean deepSearch) throws NoSuchMethodException, NoSuchFieldException {
            MetaField metaField;
            metaField = allMetaFieldMap.get(name);
            if (metaField == null) {
                if ((metaField = metaFieldMap.get(name)) != null)
                    return metaField;
                else {
                    Field field = getField(name, deepSearch);
                    metaField = buildMetaField(name, field);
                }
                metaFieldMap.putIfAbsent(name, metaField);
            }
            return metaField;
        }

        private MetaField buildMetaField(String name, Field field) {
            MethodInvoker getter = null, setter = null;
            try {
                getter = getInvoker("get" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class<?>[0]);
            } catch (NoSuchMethodException e) {
                // ignore
            }
            try {
                setter = getInvoker("set" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class<?>[]{field.getType()});
            } catch (NoSuchMethodException e) {
                // ignore
            }
            return new MetaField(field, setter, getter);
        }

        public Field getField(String name, boolean deepSearch) throws NoSuchFieldException {
            if (deepSearch) {
                Class<?> currentClass = this.declaringClass;
                while (currentClass != null && currentClass != Object.class) {
                    try {
                        return currentClass.getDeclaredField(name);
                    } catch (NoSuchFieldException e) {
                        currentClass = currentClass.getSuperclass();
                    }
                }
                throw new NoSuchFieldException(name);
            } else {
                return this.declaringClass.getDeclaredField(name);
            }
        }

        @Override
        public Map<String, MetaField> linedFindAllFieldsMap() throws NoSuchMethodException, NoSuchFieldException {
            /*
             * 功能：线程安全地初始化类的所有字段元数据映射（包含父类字段）
             *
             * 逻辑流程：
             * 1. 首次访问时检测到 allMetaFieldMap 为空
             * 2. 进入同步块执行双重检查锁定（Double-Checked Locking）
             *   a. 设置 occupied 标志防止并发操作
             *   b. 通过类继承链逐级获取字段（当前类 -> 父类 -> ... -> Object）
             *   c. 对每个字段构建 MetaField 并缓存到 allMetaFieldMap
             *   d. 最终将 Map 转换为不可变形式保证数据安全
             * 3. 非首次访问时：
             *   a. 若未处于初始化状态（!occupied），直接返回已缓存结果
             *   b. 若正在初始化（occupied），进入同步块等待完成
             */
            if (allMetaFieldMap.isEmpty()) {
                synchronized (lock) { // 同步锁保证线程安全
                    if (allMetaFieldMap.isEmpty()) { // 双重检查锁定
                        occupied = true; // 设置资源占用标志
                        try {
                            // 遍历类继承链
                            Class<?> currentClass = this.declaringClass;
                            while (currentClass != null && currentClass != Object.class) {
                                Field[] fields = currentClass.getDeclaredFields();
                                for (Field f : fields) // 处理每个字段
                                    // 并发安全的字段注册（putIfAbsent）
                                    allMetaFieldMap.putIfAbsent(f.getName(), buildMetaField(f.getName(), f));
                                currentClass = currentClass.getSuperclass(); // 向父类追溯
                            }
                            occupied = false; // 释放资源占用标志
                        } catch (Throwable e) {
                            allMetaFieldMap.clear(); // 异常时清理缓存
                            throw e;
                        }
                        // 转换为不可变Map（线程安全+防止后续修改）
                        allMetaFieldMap = AbstractImmutableMap.immutable(allMetaFieldMap);
                    }
                }
                return allMetaFieldMap;
            } else { // 已有缓存的情况
                if (!occupied) { // 常规访问路径（无锁快速返回）
                    return allMetaFieldMap;
                } else { // 处理初始化过程中的并发访问
                    synchronized (lock) { // 等待初始化完成
                        return allMetaFieldMap;
                    }
                }
            }

        }

        @Override
        public Method getMethod(String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
            int methodKey = Objects.hash(name.hashCode(), Arrays.hashCode(parameterTypes));
            Method method = methodMap.get(methodKey);
            if (method == null) {
                Class<?> currentClass = this.declaringClass;
                while (currentClass != null && currentClass != Object.class) {
                    try {
                        return currentClass.getDeclaredMethod(name, parameterTypes);
                    } catch (NoSuchMethodException e) {
                        currentClass = currentClass.getSuperclass();
                    }
                }
                throw new NoSuchMethodException(name + "(" + Arrays.toString(parameterTypes) + ")");
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
