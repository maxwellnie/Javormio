package com.maxwellnie.velox.sql.core.natives.database.dialect;

import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.QueryRowSql;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect.SqlInterceptor;

/**
 * @author Maxwell Nie
 * 方言父类
 */
public abstract class Dialect extends SqlInterceptor {
    public Dialect() {
        super(2000, null);
    }

    public Dialect(long index, TargetMethodSignature signature) {
        super(index, signature);
    }

    @Override
    public RowSql beforeBuildRowSql(MetaData metaData) {
        if(!SqlType.QUERY.equals(metaData.getProperty("sqlType")))
            return null;
        else
            return beforeBuild(metaData);
    }
    public abstract RowSql beforeBuild(MetaData metaData);
    @Override
    public RowSql afterBuildRowSql(MetaData metaData, RowSql rowSql) {
        if(!SqlType.QUERY.equals(metaData.getProperty("sqlType")))
            return rowSql;
        else
            return afterBuild((QueryRowSql) rowSql);
    }
    public abstract QueryRowSql afterBuild(QueryRowSql rowSql);
}
