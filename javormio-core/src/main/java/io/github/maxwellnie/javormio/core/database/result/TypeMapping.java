package io.github.maxwellnie.javormio.core.database.result;

import io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta.MetaProperty;
import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

import java.util.Map;
import java.util.Objects;

/**
 * 类型映射，记录了Java对象与数据库列之间的映射关系
 * @author Maxwell Nie
 */
public class TypeMapping {
    /**
     * 类型
     */
    private final Class<?> type;
    /**
     * 属性
     */
    private final MetaProperty metaProperty;
    /**
     * 类型处理器
     */
    private final TypeHandler<?> typeHandler;
    /**
     * 列名
     */
    private final String columnName;
    /**
     * 子映射
     */
    private final Map<String, TypeMapping> children;
    /**
     * 哈希值
     */
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
