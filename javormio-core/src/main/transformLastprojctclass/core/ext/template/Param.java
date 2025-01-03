package com.maxwellnie.velox.sql.core.ext.template;

import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Maxwell Nie
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value();

    Class<? extends TypeConvertor> convertor() default TypeConvertor.class;
}
