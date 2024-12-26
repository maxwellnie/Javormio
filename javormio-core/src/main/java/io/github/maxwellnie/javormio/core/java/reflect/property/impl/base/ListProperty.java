package io.github.maxwellnie.javormio.core.java.reflect.property.impl.base;


import io.github.maxwellnie.javormio.core.java.reflect.property.Property;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public class ListProperty implements Property<Integer> {
    public static final ListProperty INSTANCE = new ListProperty();

    @Override
    public <T> T getValue(Object o, Integer integer) {
        if (o == null)
            return null;
        Iterator iterator = ((Collection) o).iterator();
        Object value = null;
        for (int i = 0; i < integer; i++) {
            value = iterator.next();
        }
        return (T) value;
    }

    @Override
    public Object setValue(Object o, Integer integer, Object value) {
        if (o == null)
            throw new NullPointerException("The target object is null.");
        List list = (List) o;
        list.add(integer, value);
        return list;
    }
}
