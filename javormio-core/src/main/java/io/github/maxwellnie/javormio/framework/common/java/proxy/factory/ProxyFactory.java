package io.github.maxwellnie.javormio.framework.common.java.proxy.factory;

import io.github.maxwellnie.javormio.framework.common.java.api.Factory;
import io.github.maxwellnie.javormio.framework.common.java.proxy.ProxyInfo;
import io.github.maxwellnie.javormio.framework.common.java.reflect.ObjectFactory;

/**
 * @author Maxwell Nie
 */
public interface ProxyFactory extends Factory<Object> {
    /**
     * Create a proxy object
     *
     * @param clazz      The target class
     * @param proxyInfos All proxy information
     * @return ObjectFactory<T>
     */
    <T> ObjectFactory<T> proxy(Class<?> clazz, ProxyInfo[] proxyInfos) throws ReflectiveOperationException;

    /**
     * Create a proxy object
     *
     * @param target     The target class
     * @param proxyInfos All proxy information
     * @return ObjectFactory<T>
     */
    <T> ObjectFactory<T> proxy(Object target, ProxyInfo[] proxyInfos) throws ReflectiveOperationException;
}
