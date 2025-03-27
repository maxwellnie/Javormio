package io.github.maxwellnie.javormio.framework.common.java.reflect.property.impl.base;

import io.github.maxwellnie.javormio.framework.common.java.reflect.property.Property;

import java.lang.reflect.Array;

/**
 * 从属数组的属性
 *
 * @author Maxwell Nie
 */
public class ArrayProperty implements Property {
    public static final ArrayProperty INSTANCE = new ArrayProperty();

    @Override
    public Object getValue(Object o, Object key) {
        if (o == null)
            return null;
        return Array.get(o, (int) key);
    }

    @Override
    public Object setValue(Object o, Object key, Object value) {
        if (o == null)
            throw new NullPointerException("The target object is null.");
        int integer = (int) key;
        int length = Array.getLength(o);
        if (integer < length) {
            Array.set(o, integer, value);
            return o;
        } else if (integer == length + 1) {
            Object o1 = Array.newInstance(o.getClass().getComponentType(), length + 1);
            System.arraycopy(o, 0, o1, 0, Array.getLength(o));
            Array.set(o1, integer, value);
            return o1;
        } else
            throw new ArrayIndexOutOfBoundsException("The index is out of bounds.");
    }
}
