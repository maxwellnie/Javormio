package io.github.maxwellnie.javormio.core.database.table.primary;

import io.github.maxwellnie.javormio.core.database.table.column.ColumnInfo;

/**
 * @author Maxwell Nie
 */
public class PrimaryInfo extends ColumnInfo {
    private KeyGenerator keyGenerator;

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }
}
