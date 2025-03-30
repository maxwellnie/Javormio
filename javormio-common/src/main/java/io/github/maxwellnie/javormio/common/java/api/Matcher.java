package io.github.maxwellnie.javormio.common.java.api;

/**
 * 匹配器，按规则匹配
 *
 * @author Maxwell Nie
 */
@FunctionalInterface
public interface Matcher<T> {
    /**
     * 判断当前对象是否匹配目标对象
     *
     * @param t
     * @return boolean
     */
    boolean matches(T t);
}
