package io.github.maxwellnie.javormio.core.translation;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.core.translation.table.TableInfo;
import io.github.maxwellnie.javormio.common.java.reflect.Reflection;

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
    default TableInfo get() {
        throw new RuntimeException("TableParser.get() is not support,please implement it.");
    }

    /**
     * 解析数据库表信息
     *
     * @param clazz 实体类
     * @return TableInfo
     */
    TableInfo parse(Class<?> clazz);

    /**
     * 解析数据库表信息
     *
     * @param reflection 反射对象
     * @return TableInfo
     */

    TableInfo parse(Reflection<?> reflection);
}
