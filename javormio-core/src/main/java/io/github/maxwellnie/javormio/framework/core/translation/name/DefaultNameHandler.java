package io.github.maxwellnie.javormio.framework.core.translation.name;

/**
 * @author Maxwell Nie
 */
public class DefaultNameHandler implements NameHandler {
    @Override
    public String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    @Override
    public String getColumnName(String fieldName) {
        return fieldName;
    }

    @Override
    public String getColumnName(String fieldName, String tableName) {
        return tableName + "_" + fieldName;
    }

    @Override
    public String getTableName(String tableName) {
        return tableName;
    }
}
