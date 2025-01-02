package com.maxwellnie.velox.sql.core.natives.database.mapping.returntype;

import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;

/**
 * @author Maxwell Nie
 */
public interface ReturnTypeParser {
    /**
     * 解析返回值类型
     *
     * @param returnType
     * @param entityClass
     * @return
     */
    ReturnTypeMapping parse(Class<?> returnType, Class<?> entityClass);

    /**
     * 解析返回值类型
     *
     * @param returnType
     * @param tableInfo
     * @return
     */
    ReturnTypeMapping parse(Class<?> returnType, TableInfo tableInfo);
}
