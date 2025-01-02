package com.maxwellnie.velox.sql.core.proxy.info.impl;

import com.maxwellnie.velox.sql.core.natives.wrapper.MetaStyleWrapper;
import com.maxwellnie.velox.sql.core.proxy.info.MethodProxyInfo;

/**
 * @author Maxwell Nie
 */
public class ProxyInfo extends MetaStyleWrapper<Object> {
    private MethodProxyInfo[] methodProxyInfos;
    private Class<?> clazz;
    private Object proxyObject;
    private Class<?>[] interfaces;
    public MethodProxyInfo[] getMethodProxyInfos() {
        return methodProxyInfos;
    }

    public void setMethodProxyInfos(MethodProxyInfo[] methodProxyInfos) {
        this.methodProxyInfos = methodProxyInfos;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object getProxyObject() {
        return proxyObject;
    }

    public void setProxyObject(Object proxyObject) {
        this.proxyObject = proxyObject;
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Class<?>[] interfaces) {
        this.interfaces = interfaces;
    }
}
