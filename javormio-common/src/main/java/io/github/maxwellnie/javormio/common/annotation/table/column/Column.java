package io.github.maxwellnie.javormio.common.annotation.table.column;

import io.github.maxwellnie.javormio.common.java.type.TypeHandler;
import io.github.maxwellnie.javormio.common.java.api.Constants;

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
    /**
     * 字段名
     *
     * @return String
     */
    String value() default Constants.EMPTY_STRING;

    /**
     * 字段别名
     *
     * @return String
     */
    String alias() default Constants.EMPTY_STRING;

    /**
     * 类型处理器
     *
     * @return Class<? extends TypeHandler>
     */
    Class<? extends TypeHandler> typeHandler() default TypeHandler.class;
}
