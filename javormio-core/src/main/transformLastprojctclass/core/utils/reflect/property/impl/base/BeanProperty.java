package com.maxwellnie.velox.sql.core.utils.reflect.property.impl.base;

import com.maxwellnie.velox.sql.core.natives.exception.NotMappedFieldException;
import com.maxwellnie.velox.sql.core.utils.reflect.MetaField;
import com.maxwellnie.velox.sql.core.utils.reflect.property.Property;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class BeanProperty implements Property<Object> {
    Map<String, MetaField> metaFields;

    public BeanProperty(Map<String, MetaField> metaFields) {
        this.metaFields = metaFields;
    }

    @Override
    public <T> T getValue(Object o, Object param) {
        if (metaFields == null || o == null)
            throw new NullPointerException("\"Object o\" or \"MetaField m\" is null.");
        try {
            MetaField metaField = metaFields.get(param);
            if(metaField == null)
                throw new NullPointerException("MetaField is null");
            else
                return (T) (metaField.get(o));
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new NotMappedFieldException(e);
        }
    }

    @Override
    public Object setValue(Object o, Object param, Object value) {
        MetaField metaField = metaFields.get(param);
        if(metaField == null)
            throw new NullPointerException("MetaField is null");
        try {
            metaField.set(o, value);
            return o;
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new NotMappedFieldException(e);
        }
    }
}
