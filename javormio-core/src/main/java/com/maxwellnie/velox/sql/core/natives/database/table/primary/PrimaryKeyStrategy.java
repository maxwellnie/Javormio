package com.maxwellnie.velox.sql.core.natives.database.table.primary;

import com.maxwellnie.velox.sql.core.natives.database.table.primary.generator.KeyGenerator;
import com.maxwellnie.velox.sql.core.natives.database.table.primary.keyselector.KeySelector;

/**
 * 主键策略
 */
public class PrimaryKeyStrategy {
    private final KeyGenerator keyGenerator;
    private final KeySelector keySelector;

    public PrimaryKeyStrategy(KeyGenerator keyGenerator, KeySelector keySelector) {
        this.keyGenerator = keyGenerator;
        this.keySelector = keySelector;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public KeySelector getKeySelector() {
        return keySelector;
    }
}
