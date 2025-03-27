package io.github.maxwellnie.javormio.framework.common.java.proxy.factory;

import io.github.maxwellnie.javormio.framework.common.java.proxy.DefaultProxyHandler;
import io.github.maxwellnie.javormio.framework.common.java.proxy.ProxyInfo;
import io.github.maxwellnie.javormio.framework.common.java.reflect.ObjectFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author Maxwell Nie
 */
public class DefaultProxyFactory implements ProxyFactory {
    @Override
    public <T> ObjectFactory<T> proxy(Class<?> clazz, ProxyInfo[] proxyInfos) throws ReflectiveOperationException {
        HashSet<Class<?>> interfaceSet = new HashSet<>();
        Map<Integer, List<ProxyInfo>> proxyInfoHashMap = new HashMap<>();
        for (ProxyInfo proxyInfo : proxyInfos) {
            Class<?> targetSuperClass = proxyInfo.getTargetSuperClass();
            if (!targetSuperClass.isInterface())
                throw new ReflectiveOperationException("The class " + targetSuperClass.getName() + " is not an interface.");
            checkInterface(clazz, targetSuperClass);
            for (Class<?> superClass : targetSuperClass.getInterfaces()) {
                if (superClass.equals(targetSuperClass)) {
                    interfaceSet.add(targetSuperClass);
                    break;
                }
            }
            for (Method interfaceDeclaredMethod : targetSuperClass.getMethods()) {
                if (proxyInfo.getMethodMatcher().matches(interfaceDeclaredMethod)) {
                    Integer hashCode = Objects.hash(interfaceDeclaredMethod.getName(), Arrays.hashCode(interfaceDeclaredMethod.getParameterTypes()));
                    List<ProxyInfo> proxyInfoList = proxyInfoHashMap.computeIfAbsent(hashCode, k -> new LinkedList<>());
                    proxyInfoList.add(proxyInfo);
                }
            }
        }
        for (List<ProxyInfo> proxyInfoList : proxyInfoHashMap.values()) {
            proxyInfoList.sort(Comparator.comparingInt(ProxyInfo::getLevel));
        }
        return new ProxyObjectFactory<>(clazz, interfaceSet.toArray(new Class<?>[0]), proxyInfoHashMap);
    }

    @Override
    public <T> ObjectFactory<T> proxy(Object target, ProxyInfo[] proxyInfos) throws ReflectiveOperationException {
        if (target == null)
            throw new ReflectiveOperationException("The target object is null.");
        Class<?> clazz = target.getClass();
        return proxy(clazz, proxyInfos);
    }

    private void checkInterface(Class<?> clazz, Class<?> interfaceClazz) throws ReflectiveOperationException {
        Class<?>[] targetClassInterfaces = clazz.getInterfaces();
        for (Class<?> targetClassInterface : targetClassInterfaces) {
            if (!targetClassInterface.equals(interfaceClazz))
                throw new ReflectiveOperationException("The interface " + clazz.getName() + " does not found in the class " + clazz);
        }
    }

    /**
     * ProxyObjectFactory
     *
     * @param <T>
     */
    public static class ProxyObjectFactory<T> implements ObjectFactory<T> {
        private final Class<?> clazz;
        private final Class<?>[] interfaces;
        private final Map<Integer, List<ProxyInfo>> proxyInfoHashMap;

        public ProxyObjectFactory(Class<?> clazz, Class<?>[] interfaces, Map<Integer, List<ProxyInfo>> proxyInfoHashMap) {
            this.clazz = clazz;
            this.interfaces = interfaces;
            this.proxyInfoHashMap = proxyInfoHashMap;
        }

        @Override
        public <T> T produce(Object object) throws ReflectiveOperationException {
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new DefaultProxyHandler(proxyInfoHashMap, object));
        }
    }
}
