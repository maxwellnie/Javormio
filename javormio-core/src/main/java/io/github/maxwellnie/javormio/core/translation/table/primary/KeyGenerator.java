package io.github.maxwellnie.javormio.core.translation.table.primary;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;

/**
 * 主键生成器
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public interface KeyGenerator {
    /**
     * 再添加数据前生成主键
     *
     * @param params
     */
    void beforeInsert(Object[] params);

    /**
     * 在添加数据后查询主键
     *
     * @param params
     */
    void afterInsert(Object[] params);
}
