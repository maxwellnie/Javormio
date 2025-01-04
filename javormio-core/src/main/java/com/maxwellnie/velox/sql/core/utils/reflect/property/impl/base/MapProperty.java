package com.maxwellnie.velox.sql.core.utils.reflect.property.impl.base;

import com.maxwellnie.velox.sql.core.utils.reflect.property.Property;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class MapProperty implements Property<Object> {
    public static final MapProperty INSTANCE = new MapProperty();

    @Override
    public <T> T getValue(Object o, Object o2) {
        if (o == null)
            return null;
        return (T) ((Map) o).get(o2);
    }

    @Override
    public Object setValue(Object o, Object o2, Object value) {
        if (o == null)
            throw new NullPointerException("The target object is null.");
        Map map = (Map) o;
        map.put(o2, value);
        return map;
    }
}
