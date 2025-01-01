package io.github.maxwellnie.javormio.core.database.name;

/**
 * 名称处理器，用于将Java类名字段名转换为数据库中表名或字段名
 * @author Maxwell Nie
 */
public interface NameHandler {
    /**
     * 获取表名
     * @param clazz
     * @return String
     */
    String getTableName(Class<?> clazz);
    /**
     * 获取字段名
     * @param fieldName
     * @return String
     */
    String getColumnName(String fieldName);
    /**
     * 获取字段名
     * @param fieldName
     * @param tableName
     * @return String
     */
    String getColumnName(String fieldName, String tableName);

    /**
     * 获取表名
     * @param tableName
     * @return String
     */
    String getTableName(String tableName);
}
