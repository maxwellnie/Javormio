package io.github.maxwellnie.javormio.common.java.reflect.method;

/**
 * 链式调用
 *
 * @author Maxwell Nie
 */
public interface Chainable<P> {
    /**
     * 结束当前层级的工作，返回上一层级
     *
     * @return P
     */
    P end();
}
