package io.github.maxwellnie.javormio.core.annotation.table.join;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表关联注解
 *
 * @author Maxwell Nie
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinField {
    /**
     * 从表名
     *
     * @return String
     */
    String slaveName();
    /**
     * 主表字段名
     *
     * @return String
     */
    String slaveFieldName();
}
