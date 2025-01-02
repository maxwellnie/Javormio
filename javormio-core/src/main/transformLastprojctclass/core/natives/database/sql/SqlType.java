package com.maxwellnie.velox.sql.core.natives.database.sql;

import com.maxwellnie.velox.sql.core.natives.exception.TypeNotSupportedException;

/**
 * @author Maxwell Nie
 */
public enum SqlType {
    INSERT,
    BATCH_INSERT,
    DELETE,
    BATCH_DELETE,
    BATCH,
    BATCH_UPDATE(),
    UPDATE,
    QUERY,
    OTHER,
    JDBC,
    TEMPLATE;
    public SqlType toSqlRunnerType(){
        SqlType newSqlType = this;
        switch (this){
            case UPDATE:
            case DELETE:
            case INSERT:
                newSqlType = SqlType.UPDATE;
                break;
            case QUERY:
                newSqlType = SqlType.QUERY;
                break;
            case BATCH:
            case BATCH_DELETE:
            case BATCH_UPDATE:
            case BATCH_INSERT:
                newSqlType = SqlType.BATCH;
                break;
            default:
                throw new TypeNotSupportedException("Not supported SqlType =>["+this+"]");
        }
        return newSqlType;
    }

}
