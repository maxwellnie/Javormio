package com.maxwellnie.velox.sql.core.natives.database.dialect;

import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlPool;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.QueryRowSql;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;

/**
 * @author Maxwell Nie
 */
public class MySqlDialect extends Dialect {
    @Override
    public RowSql beforeBuild(MetaData metaData) {
        return null;
    }

    @Override
    public QueryRowSql afterBuild(QueryRowSql rowSql) {
        String sql = rowSql.getNativeSql();
        sql = sql + SqlPool.SPACE + "LIMIT " + rowSql.getStart() + ", " + rowSql.getOffset();
        rowSql.setNativeSql(sql);
        return rowSql;
    }
}
