package io.github.maxwellnie.javormio.core.database;

import io.github.maxwellnie.javormio.core.database.table.TableInfo;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;

/**
 * 数据库表信息解析器
 * @author Maxwell Nie
 */
public interface TableParser {
    /**
     * 解析数据库表信息
     * @param clazz 实体类
     * @return TableInfo[]
     */
    TableInfo[] parse(Class<?>[] clazz);
    /**
     * 从缓存中获取数据库表信息
     * @return TableInfo
     */
    default TableInfo get()  {
        throw new RuntimeException("TableParser.get() is not support,please implement it in implement class.");
    }
    /**
     * 解析数据库表信息
     * @param clazz 实体类
     * @return TableInfo
     */
    TableInfo parse(Class<?> clazz);
    /**
     * 解析数据库表信息
     * @param reflection 反射对象
     * @return TableInfo
     */

    TableInfo parse(Reflection<?> reflection);
}
