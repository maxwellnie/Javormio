package io.github.maxwellnie.javormio.flexible.sql.plugin.function;

import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.translation.sql.SqlFragment;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.SqlBuilder;

/**
 * @author Maxwell Nie
 */
public class Count<T extends Number> extends SqlFunction<T> {
    protected String[] columns;

    public Count(Class<T> returnType, Context context, String... columns) {
        super("COUNT", null, returnType, context.getTypeHandler(returnType));
        this.columns = columns;
    }

    public Count(Class<T> returnType, ColumnInfo<?, T> columnInfo, String... columns) {
        super("COUNT", null, returnType, columnInfo.getTypeHandler());
        this.columns = columns;
    }

    @Override
    public SqlFragment toSqlFragment() {
        SqlBuilder sqlBuilder = new SqlBuilder();
        sqlBuilder.append(getName())
                .append("(");
        for (int i = 0; i < columns.length; i++) {
            if (i != 0)
                sqlBuilder.append(",");
            sqlBuilder.append(columns[i]);
        }
        sqlBuilder.append(")");
        return sqlBuilder;
    }
}
