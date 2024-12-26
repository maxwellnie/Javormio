package io.github.maxwellnie.javormio.core.database.name;

/**
 * @author Maxwell Nie
 */
public interface NameHandler {
    String getTableName(Class<?> clazz);

    String getColumnName(String fieldName);

    String getColumnName(String fieldName, String tableName);

    String getTableName(String tableName);
}
