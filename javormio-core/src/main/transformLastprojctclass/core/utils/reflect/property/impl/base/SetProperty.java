package com.maxwellnie.velox.sql.core.utils.reflect.property.impl.base;

import com.maxwellnie.velox.sql.core.utils.reflect.property.Property;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Maxwell Nie
 */
public class SetProperty implements Property<Integer> {
    public static final SetProperty INSTANCE = new SetProperty();

    @Override
    public <T> T getValue(Object o, Integer index) {
        if (o == null)
            return null;
        Iterator iterator = ((Collection) o).iterator();
        Object value = null;
        for (int i = 0; i < index; i++) {
            value = iterator.next();
        }
        return (T) value;
    }

    /**
     * 请注意，index参数无法对Set生效
     *
     * @param o
     * @param index
     * @param value
     * @return
     */
    @Override
    public Object setValue(Object o, Integer index, Object value) {
        if (o == null)
            throw new NullPointerException("The target object is null.");
        Set set = (Set) o;
        set.add(value);
        return set;
    }
}
