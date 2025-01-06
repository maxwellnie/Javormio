package io.github.maxwellnie.javormio.core.database.sql;

/**
 * SQL的类型：插入、更新、删除、查询、批量操作
 *
 * @author Maxwell Nie
 */
public interface SqlType {
    int INSERT = 1;
    int UPDATE = 2;
    int DELETE = 3;
    int SELECT = 4;
    int BATCH = 5;
    static boolean isInsert(int type) {
        return (type & INSERT)!= 0;
    }
    static boolean isUpdate(int type) {
        return (type & UPDATE)!= 0;
    }
    static boolean isMayChange(int type) {
        return (type & (INSERT | UPDATE | DELETE))!= 0;
    }
    static boolean isDelete(int type) {
        return (type & DELETE)!= 0;
    }
    static boolean isSelect(int type) {
        return (type & SELECT)!= 0;
    }
    static boolean isBatch(int type) {
        return (type & BATCH)!= 0;
    }
}
