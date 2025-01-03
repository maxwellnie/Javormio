package com.maxwellnie.velox.sql.core.proxy.parser;

import com.maxwellnie.velox.sql.core.proxy.info.impl.ProxyInfo;

/**
 * @author Maxwell Nie
 */
public interface ProxyInfoParser {
    ProxyInfo parse(Object obj);

    ProxyInfo parse(Class<?> clazz);
}
