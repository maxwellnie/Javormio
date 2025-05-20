package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import io.github.maxwellnie.javormio.common.java.api.Registry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collection;

/**
 * 动态数据源
 *
 * @author Maxwell Nie
 */
public interface DynamicDataSource extends Registry<Object, DataSource>{

    /**
     * 获取当前使用的数据源
     *
     * @return DataSource
     */
    DataSource getCurrentDataSource();

    /**
     * 获取默认的数据源
     *
     * @return DataSource
     */
    DataSource getDefaultDataSource();

    /**
     * 设置当前使用的数据源
     *
     * @param dataSourceName 数据源名称
     */
    void setCurrentDataSource(String dataSourceName);

    /**
     * 恢复初始数据源
     *
     */
    void resetCurrentDataSource();
    /**
     * 获取连接
     *
     * @throws Exception
     */
    Connection getConnection();

    /**
     * 获取所有的数据源
     *
     * @return Collection<DataSource>
     */
    Collection<DataSource> getDataSources();
}
