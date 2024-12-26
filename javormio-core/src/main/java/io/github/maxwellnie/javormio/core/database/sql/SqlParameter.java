package io.github.maxwellnie.javormio.core.database.sql;

import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

import java.util.Objects;

/**
 * @author Maxwell Nie
 */
public class SqlParameter {
    private final Object value;
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
