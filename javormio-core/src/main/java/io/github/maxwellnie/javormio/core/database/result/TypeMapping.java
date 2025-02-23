package io.github.maxwellnie.javormio.core.database.result;

import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.property.Property;
import io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta.MetaProperty;
import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 类型映射，记录了Java对象与数据库列之间的映射关系
 *
 * @author Maxwell Nie
 */
public class TypeMapping {
    /**
     * 反射对象
     */
    private final Reflection<?> reflection;
    /**
     * 属性
     */
    private final Property property;
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
     * 是否是实体
     */
    private final boolean isEntity;
    /**
     * 哈希值
     */
    private int hashCode = -1;

    public TypeMapping(Reflection<?> reflection, Property property, TypeHandler<?> typeHandler, String columnName, Map<String, TypeMapping> children, boolean isEntity) {
        this.reflection = reflection;
        this.property = property;
        this.typeHandler = typeHandler;
        this.columnName = columnName;
        this.children = children;
        this.isEntity = isEntity;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public Class<?> getType() {
        return reflection.getDeclaringClass();
    }

    public Property getProperty() {
        return property;
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

    public boolean isComplex() {
        return children != null && !children.isEmpty();
    }

    public Reflection<?> getReflection() {
        return reflection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reflection, property, typeHandler, columnName, children, hashCode);
    }
}
