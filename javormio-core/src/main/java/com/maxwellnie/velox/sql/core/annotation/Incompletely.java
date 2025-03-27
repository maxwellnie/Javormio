package com.maxwellnie.velox.sql.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标记一个未完成的类、方法、字段、构造器
 * @author Maxwell Nie
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Incompletely {
}
