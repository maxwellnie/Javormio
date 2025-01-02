package com.maxwellnie.velox.sql.core.utils.reflect.property.impl.base;

import com.maxwellnie.velox.sql.core.utils.reflect.property.Property;

import java.lang.reflect.Array;

/**
 * @author Maxwell Nie
 */
public class ArrayProperty implements Property<Integer> {
    public static final ArrayProperty INSTANCE = new ArrayProperty();

    @Override
    public <T> T getValue(Object o, Integer integer) {
        if (o == null)
            return null;
        return (T) Array.get(o, integer);
    }

    @Override
    public Object setValue(Object o, Integer integer, Object value) {
        if (o == null)
            throw new NullPointerException("The target object is null.");
        int length = Array.getLength(o);
        if(integer < length){
            Array.set(o, integer, value);
            return o;
        } else if (integer == length + 1){
            Object o1 = Array.newInstance(o.getClass().getComponentType(), length + 1);
            System.arraycopy(o, 0, o1, 0, Array.getLength(o));
            Array.set(o1, integer, value);
            return o1;
        }else
            throw new ArrayIndexOutOfBoundsException("The index is out of bounds.");
    }
}
