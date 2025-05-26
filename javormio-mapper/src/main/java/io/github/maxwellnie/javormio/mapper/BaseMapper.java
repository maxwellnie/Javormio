package io.github.maxwellnie.javormio.mapper;

import io.github.maxwellnie.javormio.core.execution.ExecutionException;
import io.github.maxwellnie.javormio.common.java.table.primary.PrimaryException;

import java.io.Serializable;
import java.util.List;

/**
 * @author Maxwell Nie
 */
public interface BaseMapper<T>{
    /**
     * 查询唯一主键值对应数据
     * @param id id
     * @return T 返回该id对应数据，如果无结果，则返回null
     * @throws PrimaryException 如果该表无主键或无唯一主键
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    T selectById(Serializable id);
    /**
     * 插入数据
     * @param t 待插入数据
     * @return int 成功插入数据条数
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    int insert(T t);
    /**
     * 更新唯一主键对应的数据
     * @param t 待更新数据
     * @return int 成功更新数据条数
     * @throws PrimaryException 如果该表无主键或无唯一主键
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    int updateById(T t);
    /**
     * 删除唯一主键值对应数据
     * @param id 待删除数据
     * @return int 成功删除数据条数
     * @throws PrimaryException 如果该表无主键或无唯一主键
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    int deleteById(Serializable id);
    /**
     * 获取该表所有数据
     * @return List<T> 获取该表所有数据
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    List<T> selectAll();
    /**
     * 获取该表分页数据
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return Page<T> 获取该表分页数据
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    Page<T> selectPage(int pageNum, int pageSize);
    /**
     * 批量删除数据
     * @param ids 待删除数据id集合
     * @return int 成功删除数据条数
     * @throws PrimaryException 如果该表无主键或无唯一主键
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    int deleteByIds(List<Serializable> ids);
    /**
     * 批量插入数据
     * @param ts 待插入数据集合
     * @return int 成功插入数据条数
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    int insertBatch(List<T> ts);
    /**
     * 获取该表数据总数
     * @return long 该表数据总数
     * @throws ExecutionException 如果执行SQL中出现错误
     */
    long count();
}
