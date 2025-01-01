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
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableJoin {
    /**
     * 关联信息
     *
     * @return Join[]
     */
    Join[] getJoins();
}
