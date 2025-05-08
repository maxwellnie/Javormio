package io.github.maxwellnie.javormio.core.translation.table.primary;

import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

/**
 * 主键信息
 *
 * @author Maxwell Nie
 */
public class PrimaryInfo<E, T> extends ColumnInfo<E, T> {
    /**
     * 主键生成器
     */
    private KeyGenerator keyGenerator;

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
