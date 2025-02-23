package io.github.maxwellnie.javormio.core.java.reflect.property.impl.base;

import io.github.maxwellnie.javormio.core.java.reflect.property.Property;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class MapProperty implements Property {
    public static final MapProperty INSTANCE = new MapProperty();

    @Override
    public Object getValue(Object o, Object o2) {
        if (o == null)
            return null;
        return ((Map) o).get(o2);
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
