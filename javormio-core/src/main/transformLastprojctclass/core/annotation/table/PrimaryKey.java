package com.maxwellnie.velox.sql.core.annotation.table;

import com.maxwellnie.velox.sql.core.natives.database.table.primary.KeyStrategyManager;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;
import com.maxwellnie.velox.sql.core.natives.type.convertor.impl.DefaultConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键注解
 *
 * @author Maxwell Nie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PrimaryKey {

    /**
     * 主键策略：默认策略、jdbc自增策略、自定义策略
     *
     * @return
     */
    String strategyKey() default KeyStrategyManager.DEFAULT;

    /**
     * @return 主键名
     */
    String name() default "";

    /**
     * @return 类型转换器
     */
    Class<? extends TypeConvertor> convertor() default DefaultConvertor.class;
}
