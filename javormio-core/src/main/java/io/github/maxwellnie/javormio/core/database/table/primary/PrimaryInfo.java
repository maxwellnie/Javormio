package io.github.maxwellnie.javormio.core.database.table.primary;

import io.github.maxwellnie.javormio.core.database.table.column.ColumnInfo;

/**
 * 主键信息
 *
 * @author Maxwell Nie
 */
public class PrimaryInfo extends ColumnInfo {
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
