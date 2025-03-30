package io.github.maxwellnie.javormio.common.annotation.table;

import io.github.maxwellnie.javormio.common.java.api.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解
 *
 * @author Maxwell Nie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 表名
     *
     * @return String
     */
    String value() default Constants.EMPTY_STRING;

    /**
     * 别名
     *
     * @return String
     */
    String alias() default Constants.EMPTY_STRING;

    /**
     * 默认数据源
     *
     * @return String
     */
    String defaultDataSourceName() default Constants.EMPTY_STRING;
}
