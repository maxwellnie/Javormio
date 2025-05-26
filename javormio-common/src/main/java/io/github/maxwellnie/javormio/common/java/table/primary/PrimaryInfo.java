package io.github.maxwellnie.javormio.common.java.table.primary;

import io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo;

/**
 * 主键信息
 *
 * @author Maxwell Nie
 */
public class PrimaryInfo<E, T> extends ColumnInfo<E, T> {
    /**
     * 主键生成器
     */
    private KeyGenerator<E, T> keyGenerator;

    public KeyGenerator<E, T> getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator<E, T> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
