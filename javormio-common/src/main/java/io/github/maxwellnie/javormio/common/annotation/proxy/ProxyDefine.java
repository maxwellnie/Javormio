package io.github.maxwellnie.javormio.common.annotation.proxy;

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
    /**
     * 被代理的接口
     *
     * @return Class<?>[]
     */
    Class<?>[] interfaces();

    /**
     * 此代理类的优先级
     *
     * @return long
     */
    long index();
}
