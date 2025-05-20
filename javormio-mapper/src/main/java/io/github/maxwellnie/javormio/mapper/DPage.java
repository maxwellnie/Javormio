package io.github.maxwellnie.javormio.mapper;

import java.util.List;

/**
 * 默认分页对象
 * @author Maxwell Nie
 */
public class DPage<T> implements Page<T>{
    long total;
    long pageNum;
    long pageSize;
    List<T> records;

    public DPage(long total, long pageNum, long pageSize, List<T> records) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.records = records;
    }

    public DPage(long pageNum, long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public long getPageNum() {
        return this.pageNum;
    }

    @Override
    public long getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getTotalPage() {
        return this.total / this.pageSize + (this.total % this.pageSize == 0 ? 0 : 1);
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }
}
