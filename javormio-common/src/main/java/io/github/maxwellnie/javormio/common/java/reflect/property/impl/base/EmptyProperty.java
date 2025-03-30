package io.github.maxwellnie.javormio.common.java.reflect.property.impl.base;


import io.github.maxwellnie.javormio.common.java.reflect.property.Property;

/**
 * @author Maxwell Nie
 */
public class EmptyProperty implements Property {
    @Override
    public Object getValue(Object o, Object o2) {
        return null;
    }

    @Override
    public Object setValue(Object o, Object o2, Object value) {
        return o;
    }
}
