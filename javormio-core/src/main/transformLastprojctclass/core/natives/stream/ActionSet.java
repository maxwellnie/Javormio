package com.maxwellnie.velox.sql.core.natives.stream;

/**
 * 行动集合操作接口
 *
 * @author Maxwell Nie
 */
public interface ActionSet<T, I> {
    /**
     * 执行
     *
     * @param t
     */
    void accept(T t);

    /**
     * 添加
     *
     * @param action
     */
    void add(I action);

    /**
     * 清空
     */
    void clear();

    /**
     * 移除
     *
     * @param action
     */
    void remove(I action);
}
