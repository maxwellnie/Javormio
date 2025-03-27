package io.github.maxwellnie.javormio.framework.common.java.reflect.entity;

/**
 * 实体动作
 * @param <E> 实体类
 * @param <P> 参数
 * @param <R> 行为产生的结果
 * @author Maxwell Nie
 */
public interface Action<E, P, R>{
    /**
     * 执行动作
     * @param entity 实体
     * @param param 参数
     * @return R
     */
    R perform(E entity, P param) throws Throwable;
}
