package com.maxwellnie.velox.sql.core.annotation.dao;

import com.maxwellnie.velox.sql.core.ext.executor.QuickCustomMethodExecutor;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识接口中操作数据库方法的SQL模板
 *
 * @author Maxwell Nie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SQL {
    /**
     * SQL模板
     *
     * @return
     */
    String value();

    /**
     * 是否是通用SQL
     *
     * @return
     */
    boolean isCommon() default false;

    /**
     * SQL类型
     *
     * @return
     */
    SqlType sqlType();

    /**
     * 默认执行器
     */
    Class<? extends MethodExecutor> executor() default QuickCustomMethodExecutor.class;
}
