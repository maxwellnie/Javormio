package com.maxwellnie.velox.sql.core.utils.reflect.property.impl.base;

import com.maxwellnie.velox.sql.core.utils.reflect.property.Property;

/**
 * @author Maxwell Nie
 */
public class EmptyProperty implements Property<Object> {
    @Override
    public <T> T getValue(Object o, Object o2) {
        return null;
    }

    @Override
    public Object setValue(Object o, Object o2, Object value) {
        return o;
    }
}
