package io.github.maxwellnie.javormio.core.translation;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.common.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;

/**
 * 数据库表信息解析器
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public interface TableParser {
    /**
     * 解析数据库表信息
     *
     * @param clazz 实体类
     * @return TableInfo[]
     */
    TableInfo[] parse(Class<?>[] clazz);

    /**
     * 从缓存中获取数据库表信息
     *
     * @return TableInfo
     */
    default<T> TableInfo<T> get(Class<T> clazz) {
        throw new RuntimeException("TableParser.get(Class) is not support,please implement it.");
    }

    /**
     * 解析数据库表信息
     *
     * @param clazz 实体类
     * @return TableInfo
     */
    <T> TableInfo<T> parse(Class<T> clazz);

    /**
     * 解析数据库表信息
     *
     * @param reflection 反射对象
     * @return TableInfo
     */

    <T> TableInfo<T> parse(Reflection<T> reflection);
}
