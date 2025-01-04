package com.maxwellnie.velox.sql.core.utils.reflect.property;

/**
 * 属性接口
 *
 * @author Maxwell Nie
 */
public interface Property<P> {
    <T> T getValue(Object o, P p);

    Object setValue(Object o, P p, Object value);
}
