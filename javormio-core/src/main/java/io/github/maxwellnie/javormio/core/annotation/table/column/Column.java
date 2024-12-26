package io.github.maxwellnie.javormio.core.annotation.table.column;

import io.github.maxwellnie.javormio.core.java.api.Constants;
import io.github.maxwellnie.javormio.core.java.type.TypeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表字段注解
 *
 * @author Maxwell Nie
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default Constants.EMPTY_STRING;

    String alias() default Constants.EMPTY_STRING;

    Class<? extends TypeHandler> typeHandler() default TypeHandler.class;
}
