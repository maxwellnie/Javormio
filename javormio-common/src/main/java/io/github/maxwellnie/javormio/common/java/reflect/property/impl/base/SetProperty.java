package io.github.maxwellnie.javormio.common.java.reflect.property.impl.base;

import io.github.maxwellnie.javormio.common.java.reflect.property.Property;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Maxwell Nie
 */
public class SetProperty implements Property {
    public static final SetProperty INSTANCE = new SetProperty();

    @Override
    public Object getValue(Object o, Object key) {
        int index = (int) key;
        if (o == null)
            return null;
        Iterator iterator = ((Collection) o).iterator();
        Object value = null;
        for (int i = 0; i < index; i++) {
            value = iterator.next();
        }
        return value;
    }

    /**
     * 请注意，index参数无法对Set生效
     *
     * @param o
     * @param key
     * @param value
     * @return
     */
    @Override
    public Object setValue(Object o, Object key, Object value) {
        if (o == null)
            throw new NullPointerException("The target object is null.");
        Set set = (Set) o;
        set.add(value);
        return set;
    }
}
