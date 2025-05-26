package io.github.maxwellnie.javormio.common.annotation.table.column;

import io.github.maxwellnie.javormio.common.java.table.primary.KeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键注解
 *
 * @author Maxwell Nie
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {
     Class<? extends KeyGenerator> keyGenerator() default KeyGenerator.class;
}
