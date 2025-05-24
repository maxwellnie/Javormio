package io.github.maxwellnie.javormio.flexible.sql.plugin.execution.query;

import io.github.maxwellnie.javormio.core.translation.table.BaseMetaTableInfo;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.core.translation.table.primary.KeyGenerator;
import io.github.maxwellnie.javormio.core.translation.table.primary.PrimaryInfo;
import io.github.maxwellnie.javormio.core.translation.sql.SqlBuilder;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @author Maxwell Nie
 */
public class SubqueryTable<T, O> extends BaseMetaTableInfo<T> {
    protected final BaseMetaTableInfo<O> parent;
    protected final ColumnInfo<T, ?>[] columnInfos;
    protected final PrimaryInfo<T, ?>[] primaryInfos;
    protected final SqlBuilder sqlInstance;
    protected final KeyGenerator[] keyGenerators;

    public SubqueryTable(String alias, String defaultDataSourceName, Class<T> mappingClass, Supplier<T> instanceInvoker, BaseMetaTableInfo<O> parent, ColumnInfo<T, ?>[] columnInfos, PrimaryInfo<T, ?>[] primaryInfos, SqlBuilder sqlInstance) {
        super(alias, defaultDataSourceName, mappingClass, instanceInvoker);
        this.parent = parent;
        this.columnInfos = columnInfos;
        this.primaryInfos = primaryInfos;
        this.sqlInstance = sqlInstance;
        this.keyGenerators = Arrays.stream(primaryInfos).map(PrimaryInfo::getKeyGenerator).toArray(KeyGenerator[]::new);
    }

    public static <O> SubqueryTable<O, O> from(BaseMetaTableInfo<O> parent, SqlBuilder sqlInstance) {
        return new SubqueryTable<>(null, parent.defaultDataSourceName, parent.mappingClass, parent.instanceInvoker, parent, parent.getColumnInfos(), parent.getPrimaryInfos(), sqlInstance);
    }

    @Override
    public ColumnInfo<T, ?>[] getColumnInfos() {
        return columnInfos;
    }

    @Override
    public PrimaryInfo<T, ?>[] getPrimaryInfos() {
        return primaryInfos;
    }

    @Override
    public KeyGenerator<T, ?>[] getAllKeyGenerators() {
        return new KeyGenerator[0];
    }

    public BaseMetaTableInfo<O> getParent() {
        return parent;
    }

    public SqlBuilder getSqlInstance() {
        return sqlInstance;
    }
}
