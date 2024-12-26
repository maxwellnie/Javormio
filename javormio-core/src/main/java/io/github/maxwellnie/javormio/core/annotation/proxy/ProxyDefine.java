package io.github.maxwellnie.javormio.core.annotation.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代理类定义
 *
 * @author Maxwell Nie
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyDefine {
    Class<?>[] interfaces();
    long index();
}
