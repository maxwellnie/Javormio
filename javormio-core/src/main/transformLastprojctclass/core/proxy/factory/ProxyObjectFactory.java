package com.maxwellnie.velox.sql.core.proxy.factory;

import com.maxwellnie.velox.sql.core.proxy.info.impl.ProxyInfo;

/**
 * @author Maxwell Nie
 */
public interface ProxyObjectFactory<T>{
    T produce(Object target, ProxyInfo proxyInfo);

}
