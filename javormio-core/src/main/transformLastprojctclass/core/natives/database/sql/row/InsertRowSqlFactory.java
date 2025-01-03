package com.maxwellnie.velox.sql.core.natives.database.sql.row;

import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.exception.ExecutorException;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlPool;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.sql.creator.SqlCreator;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.column.ColumnInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.primary.KeyStrategyManager;
import com.maxwellnie.velox.sql.core.natives.database.table.primary.keyselector.JdbcSelector;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * @author Maxwell Nie
 */
public class InsertRowSqlFactory implements RowSqlFactory {
    @Override
    public RowSql getRowSql(MetaData metaData) throws ExecutorException {
        assert metaData != null : "Meta data is null.";
        TableInfo tableInfo = metaData.getProperty("tableInfo");
        assert tableInfo != null : "TableInfo is null.";
        Collection<?> entityObjects = metaData.getProperty("entityObjects");
        assert entityObjects != null : "The entityObjects is null.";
        SqlType sqlType = metaData.getProperty("sqlType");
        assert sqlType != null : "The sqlType is null.";
        String tableName = tableInfo.getTableName();
        List<List<Object>> paramsList = new LinkedList<>();
        StringBuilder valueSql = new StringBuilder();
        StringBuilder columnSql = new StringBuilder();
        List<TypeConvertor<?>> typeConvertors = new LinkedList<>();
        boolean needPrimary = true;
        if (tableInfo.hasPk()) {
            if (KeyStrategyManager.getPrimaryKeyStrategy(tableInfo.getPkColumn().getStrategyName()).getKeySelector() instanceof JdbcSelector)
                needPrimary = false;
        }
        Iterator<?> iterator = entityObjects.iterator();
        boolean justSetParam = false;
        while (iterator.hasNext()) {
            Object entityObject = iterator.next();
            List<Object> params = new LinkedList<>();
            if (tableInfo.hasPk() && needPrimary) {
                try {
                    params.add(tableInfo.getPkColumn().getColumnMappedField().get(entityObject));
                    if (!justSetParam) {
                        valueSql.append("?,");
                        columnSql.append(tableInfo.getPkColumn().getColumnName()).append(",");
                        typeConvertors.add(tableInfo.getPkColumn().getTypeConvertor());
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new ExecutorException(e);
                }
            }
            for (ColumnInfo columnInfo : tableInfo.getColumnMappedMap().values()) {
                try {
                    params.add(columnInfo.getColumnMappedField().get(entityObject));
                    if (!justSetParam) {
                        valueSql.append("?,");
                        columnSql.append(columnInfo.getColumnName()).append(",");
                        typeConvertors.add(columnInfo.getTypeConvertor());
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new ExecutorException(e);
                }
            }
            justSetParam = true;
            paramsList.add(params);
        }
        RowSql rowSql = new RowSql();
        rowSql.setSqlType(sqlType);
        rowSql.setNativeSql(SqlCreator.create(SqlPool.INSERT, tableName, columnSql.substring(0, columnSql.length() - 1), valueSql.substring(0, valueSql.length() - 1)));
        rowSql.setParamsList(paramsList);
        rowSql.setTypeConvertors(typeConvertors);
        return rowSql;
    }
}
