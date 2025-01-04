package com.maxwellnie.velox.sql.core.natives.database.mapping;

import com.maxwellnie.velox.sql.core.utils.base.TypeUtils;
import com.maxwellnie.velox.sql.core.utils.reflect.property.Property;

import java.util.Collection;
import java.util.Map;

/**
 * @author Maxwell Nie
 */
public abstract class TypeMapping<T extends TypeMapping<T>> {
    Class<?> type;
    T parentTypeMapping;
    Map<Object, T> innerTypeMappings;
    Property property;
    boolean isComplexType;

    public Object setProperty(Object bean, Object param, Object value) {
        return property.setValue(bean, param, value);
    }

    public Object getProperty(Object bean, Object param) {
        return property.getValue(bean, param);
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public T getParentTypeMapping() {
        return parentTypeMapping;
    }

    public void setParentTypeMapping(T parentTypeMapping) {
        this.parentTypeMapping = parentTypeMapping;
    }

    public Map<Object, T> getInnerTypeMappings() {
        return innerTypeMappings;
    }

    public void setInnerTypeMappings(Map<Object, T> innerTypeMappings) {
        this.innerTypeMappings = innerTypeMappings;
    }

    public Property getPropertyDefined() {
        return property;
    }

    public void setPropertyDefined(Property property) {
        this.property = property;
    }

    public T getChild(Object key) {
        if (innerTypeMappings == null)
            return null;
        return innerTypeMappings.get(key);
    }

    public boolean isArray() {
        return type.isArray();
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(type);
    }

    public boolean isComplexType() {
        return isComplexType;
    }

    public void setComplexType(boolean complexType) {
        isComplexType = complexType;
    }

    public boolean isBasicType() {
        return TypeUtils.isBasic(type);
    }

    @Override
    public String toString() {
        return "TypeMapping{" +
                "type=" + type +
                ", innerTypeMappings=" + innerTypeMappings +
                ", property=" + property +
                '}';
    }
}
