package io.github.maxwellnie.javormio.core.execution.result;

import io.github.maxwellnie.javormio.common.java.reflect.Reflection;
import io.github.maxwellnie.javormio.common.java.reflect.property.Property;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;

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
     * 列索引
     */
    private final Object columnIndex;
    /**
     * 子映射
     */
    private final Map<Object, TypeMapping> children;
    /**
     * 表信息，若非实体，则该字段为null
     */
    private final TableInfo tableInfo;
    /**
     * 是否为数字索引
     */
    private final boolean columnNumberIndex;
    /**
     * 哈希值
     */
    private int hashCode = -1;

    /**
     * 构造函数
     *
     * @param reflection  反射对象
     * @param property    属性
     * @param typeHandler 类型处理器
     * @param columnIndex 列索引，若为null，则表示该映射为实体映射，否则为字段映射，若为数字，则表示该映射为数字索引映射，若为字符串，则表示该映射为字符串索引映射
     * @param children    子映射
     * @param tableInfo   表信息，若非实体，则该字段为null
     */
    public TypeMapping(Reflection<?> reflection, Property property, TypeHandler<?> typeHandler, Object columnIndex, Map<Object, TypeMapping> children, TableInfo tableInfo) {
        this.reflection = reflection;
        this.property = property;
        this.typeHandler = typeHandler;
        this.columnIndex = columnIndex;
        this.children = children;
        this.tableInfo = tableInfo;
        if (columnIndex != null) {
            if (columnIndex instanceof String)
                this.columnNumberIndex = false;
            else if (columnIndex instanceof Number)
                this.columnNumberIndex = true;
            else
                throw new IllegalArgumentException("The \'columnIndex\' must be Number or String type.");
        } else
            this.columnNumberIndex = false;
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }

    public boolean isEntity() {
        return tableInfo != null;
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

    public Object getColumnIndex() {
        return columnIndex;
    }

    public Map<Object, TypeMapping> getChildren() {
        return children;
    }

    public boolean isColumnNumberIndex() {
        return columnNumberIndex;
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
        return Objects.hash(reflection, property, typeHandler, columnIndex, children, hashCode);
    }
}
