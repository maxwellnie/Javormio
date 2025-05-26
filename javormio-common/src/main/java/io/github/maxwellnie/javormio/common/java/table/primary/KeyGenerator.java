package io.github.maxwellnie.javormio.common.java.table.primary;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

/**
 * 主键生成器
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public interface KeyGenerator<E, T>{
    /**
     * 插入之前
     * @param target
     */
    void beforeInsert(E target);
    /**
     * 批量插入之前
     * @param ts
     */
    void beforeInsert(Collection<E> ts);
    /**
     * 插入之后
     * @param target
     * @param statement
     */
    void afterInsert(E target, Statement statement) throws SQLException;
    /**
     * 批量插入之后
     * @param ts
     * @param statement
     */
    void afterInsert(List<E> ts, Statement statement) throws SQLException;
    /**
     * 设置主键信息
     * @param primaryInfo
     * */
    void setPrimaryInfo(PrimaryInfo<E, T> primaryInfo);
    /**
     * 获取表信息
     * @return PrimaryInfo
     * */
    PrimaryInfo<E, T> getPrimaryInfo();
    /**
     * 是否接受自动生成主键
     * @return boolean
     * */
    boolean isAcceptGenerateKeys();
}
