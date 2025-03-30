package io.github.maxwellnie.javormio.core.execution.result;

import java.sql.ResultSet;
import java.util.List;

/**
 * 将ResultSet转换为Java实体对象
 *
 * @author Maxwell Nie
 */
public interface ResultSetConvertor {
    /**
     * 将ResultSet转换为Java实体对象
     *
     * @param resultSet
     * @param typeMapping 类型映射
     * @return Object
     */
    Object convert(ResultSet resultSet, TypeMapping typeMapping, boolean multipleTable) throws ConvertException;

    /**
     * 将ResultSet列表转换为Java实体对象
     *
     * @param resultSet
     * @param typeMapping 类型映射
     * @return Object
     */
    Object convert(List<ResultSet> resultSet, TypeMapping typeMapping, boolean multipleTable) throws ConvertException;
}
