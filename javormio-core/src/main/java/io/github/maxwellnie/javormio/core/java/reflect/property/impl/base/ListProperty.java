package io.github.maxwellnie.javormio.core.java.reflect.property.impl.base;


import io.github.maxwellnie.javormio.core.java.reflect.property.Property;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class ListProperty implements Property {
    public static final ListProperty INSTANCE = new ListProperty();

    @Override
    public Object getValue(Object o, Object key) {
        if (o == null)
            return null;
        List<?> list = (List) o;
        return list.get((int) key);
    }

    @Override
    public Object setValue(Object o, Object key, Object value) {
        if (o == null)
            throw new NullPointerException("The target object is null.");
        List list = (List) o;
        list.add(value);
        return list;
    }
}
