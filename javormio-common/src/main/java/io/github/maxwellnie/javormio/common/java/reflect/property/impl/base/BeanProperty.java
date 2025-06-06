package io.github.maxwellnie.javormio.common.java.reflect.property.impl.base;


import io.github.maxwellnie.javormio.common.java.NotMappedFieldException;
import io.github.maxwellnie.javormio.common.java.proxy.MethodInvocationException;
import io.github.maxwellnie.javormio.common.java.reflect.property.MetaField;
import io.github.maxwellnie.javormio.common.java.reflect.property.Property;

import java.util.Map;

/**
 * 从属Bean的属性
 *
 * @author Maxwell Nie
 */
public class BeanProperty implements Property {
    Map<String, MetaField> metaFields;

    public BeanProperty(Map<String, MetaField> metaFields) {
        this.metaFields = metaFields;
    }

    @Override
    public Object getValue(Object o, Object param) {
        if (metaFields == null || o == null)
            throw new NullPointerException("\"Object o\" or \"MetaField m\" is null.");
        try {
            MetaField metaField = metaFields.get(param);
            if (metaField == null)
                throw new NullPointerException("MetaField is null");
            else
                return metaField.get(o);
        } catch (MethodInvocationException e) {
            throw new NotMappedFieldException(e);
        }
    }

    @Override
    public Object setValue(Object o, Object param, Object value) {
        MetaField metaField = metaFields.get(param);
        if (metaField == null)
            throw new NullPointerException("MetaField is null");
        try {
            metaField.set(o, value);
            return o;
        } catch (MethodInvocationException e) {
            throw new NotMappedFieldException(e);
        }
    }
}
