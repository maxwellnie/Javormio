package io.github.maxwellnie.javormio.core.database.sql;

import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

import java.util.Objects;

/**
 * SQL参数，即SQL中所嵌入的值或Java对象
 * @author Maxwell Nie
 */
public class SqlParameter {
    /**
     * 参数值
     */
    private final Object value;
    /**
     * 参数处理器
     */
    private final TypeHandler<?> typeHandler;

    public SqlParameter(Object value, TypeHandler<?> typeHandler) {
        this.value = value;
        this.typeHandler = typeHandler;
    }

    public Object getValue() {
        return value;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
