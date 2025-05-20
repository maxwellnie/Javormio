package io.github.maxwellnie.javormio.mapper;

import java.util.List;

/**
 * 分页对象
 * @param <T>
 * @author Maxwell Nie
 */
public interface Page<T> {
    /**
     * 总数
     * @return long
     */
    long getTotal();
    /**
     * 当前页
     * @return long
     */
    long getPageNum();
    /**
     * 每页数量
     * @return long
     */
    long getPageSize();
    /**
     * 总页数
     * @return long
     */
    long getTotalPage();
    /**
     * 数据列表
     * @return List<T>
     */
    List<T> getRecords();
}
