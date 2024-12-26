package io.github.maxwellnie.javormio.core.database.result;

import io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta.MetaProperty;
import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

import java.util.Map;
import java.util.Objects;

/**
 * @author Maxwell Nie
 */
public class TypeMapping {
    private final Class<?> type;
    private final MetaProperty metaProperty;
    private final TypeHandler<?> typeHandler;
    private final String columnName;
    private final Map<String, TypeMapping> children;
    private int hashCode;

    public TypeMapping(Class<?> type, MetaProperty metaProperty, TypeHandler<?> typeHandler, String columnName, Map<String, TypeMapping> children) {
        this.type = type;
        this.metaProperty = metaProperty;
        this.typeHandler = typeHandler;
        this.columnName = columnName;
        this.children = children;
    }

    public Class<?> getType() {
        return type;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    public String getColumnName() {
        return columnName;
    }

    public Map<String, TypeMapping> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeMapping that = (TypeMapping) o;
        return that.hashCode == hashCode();
    }

    @Override
    public int hashCode() {
        if (hashCode != 0)
            return hashCode;
        return hashCode = Objects.hash(type, metaProperty, typeHandler, columnName, children);
    }
}
