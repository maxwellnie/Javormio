package io.github.maxwellnie.javormio.core.annotation.table.join;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主从表链接
 *
 * @author Maxwell Nie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {
    String slaveName();

    String masterFieldName();

    String slaveFieldName();
}
