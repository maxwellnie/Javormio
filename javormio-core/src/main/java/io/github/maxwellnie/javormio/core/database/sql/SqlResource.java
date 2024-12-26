package io.github.maxwellnie.javormio.core.database.sql;


import io.github.maxwellnie.javormio.core.java.api.Resource;

/**
 * @author Maxwell Nie
 */
public class SqlResource implements Resource {
    private final String sql;
    private final SqlType type;
    private final SqlKey key;
    private final int line;

    public SqlResource(String sql, SqlType type, SqlKey key, int line) {
        this.sql = sql;
        this.type = type;
        this.key = key;
        this.line = line;
    }

    public String getSql() {
        return sql;
    }

    public SqlType getType() {
        return type;
    }

    public SqlKey getKey() {
        return key;
    }

    public int getLine() {
        return line;
    }
}
