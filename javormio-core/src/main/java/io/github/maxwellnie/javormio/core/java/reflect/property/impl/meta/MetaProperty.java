package io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta;

import io.github.maxwellnie.javormio.core.java.reflect.property.Property;
import io.github.maxwellnie.javormio.core.java.reflect.property.PropertyType;
import io.github.maxwellnie.javormio.core.utils.TypeUtils;

import java.util.Map;

/**
 * 元属性
 *
 * @author Maxwell Nie
 */
public class MetaProperty {
    private final Class<?> type;
    private final Property<Object> property;
    private final Map<String, MetaProperty> metaProperties;
    private final PropertyType propertyType;


    public MetaProperty(Class<?> type, Property<Object> property, Map<String, MetaProperty> metaProperties) {
        this.type = type;
        this.property = property;
        this.propertyType = TypeUtils.getPropertyType(type);
        this.metaProperties = metaProperties;
    }

    public Class<?> getType() {
        return type;
    }

    public Property<Object> getProperty() {
        return property;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public Map<String, MetaProperty> getMetaProperties() {
        return metaProperties;
    }

    public MetaProperty getChild(String name) {
        if (metaProperties == null)
            return null;
        return metaProperties.get(name);
    }

    public boolean hasChildren() {
        return metaProperties != null && !metaProperties.isEmpty();
    }
}
