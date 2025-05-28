package io.github.maxwellnie.javormio.common.java.sql;

/**
 * SQL的类型：插入、更新、删除、查询、批量操作
 *
 * @author Maxwell Nie
 */
public interface SqlType {
    int UPDATE = 1;
    int SELECT = 2;
    int INSERT = 3;
    int DELETE = 4;
    int BATCH = 5;

    /**
     * 判断是否为插入SQL
     *
     * @param type
     * @return boolean
     */
    static boolean isInsert(int type) {
        return (type & INSERT) != 0;
    }

    /**
     * 判断是否为更新SQL
     *
     * @param type
     * @return boolean
     */
    static boolean isUpdate(int type) {
        return (type & UPDATE) != 0;
    }

    /**
     * 判断是否为数据修改SQL
     *
     * @param type
     * @return boolean
     */
    static boolean isDataModifyingSqlType(int type) {
        return (type & (INSERT | UPDATE | DELETE)) != 0;
    }

    /**
     * 判断是否为删除SQL
     *
     * @param type
     * @return boolean
     */
    static boolean isDelete(int type) {
        return (type & DELETE) != 0;
    }

    /**
     * 判断是否为查询SQL
     *
     * @param type
     * @return boolean
     */
    static boolean isSelect(int type) {
        return (type & SELECT) != 0;
    }

    /**
     * 判断是否为批量操作SQL
     *
     * @param type
     * @return boolean
     */
    static boolean isBatch(int type) {
        return (type & BATCH) != 0;
    }
}
