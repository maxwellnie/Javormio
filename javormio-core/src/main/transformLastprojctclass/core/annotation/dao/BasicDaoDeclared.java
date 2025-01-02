package com.maxwellnie.velox.sql.core.annotation.dao;

import com.maxwellnie.velox.sql.core.proxy.dao.executor.DefaultMethodMapRegister;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodMapRegister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Maxwell Nie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BasicDaoDeclared {
    Class<? extends MethodMapRegister> value() default DefaultMethodMapRegister.class;
}
