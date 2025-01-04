package com.maxwellnie.velox.sql.core.ext.template;

import com.maxwellnie.velox.sql.core.annotation.dao.SQL;
import com.maxwellnie.velox.sql.core.config.simple.SingletonConfiguration;
import com.maxwellnie.velox.sql.core.ext.executor.MethodExecutorBuilder;
import com.maxwellnie.velox.sql.core.ext.template.engine.TemplateEngine;
import com.maxwellnie.velox.sql.core.ext.template.engine.parser.TemplateParser;
import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.database.mapping.param.ParamTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.natives.database.sql.row.RowSql;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.exception.NotMappedMethodException;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertorManager;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.DeleteMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.InsertMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.UpdateMethodExecutor;
import com.maxwellnie.velox.sql.core.utils.base.CollectionUtils;
import com.maxwellnie.velox.sql.core.utils.reflect.property.ValueReader;
import com.maxwellnie.velox.sql.core.utils.reflect.property.impl.meta.MetaProperty;
import com.maxwellnie.velox.sql.core.natives.stream.Node;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.QueryMethodExecutor;
import com.maxwellnie.velox.sql.core.utils.framework.MethodExecutorUtils;
import com.maxwellnie.velox.sql.core.utils.reflect.property.impl.meta.NodeMetaProperty;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 解析SQL模板，获取MethodExecutor
 *
 * @author Maxwell Nie
 */
public class SQLTemplateParser {
    private static final SQLTemplateParser instance = new SQLTemplateParser();

    SQLTemplateParser() {
    }

    public static SQLTemplateParser getInstance() {
        return instance;
    }

    /**
     * 解析SQL模板，制造MethodExecutor
     *
     * @param method
     * @param clazz
     * @param tableInfo
     * @return
     */
    public MethodExecutor parse(Method method, Class<?> clazz, TableInfo tableInfo) {
        if (method.isAnnotationPresent(SQL.class)) {
            SQL sql = method.getAnnotation(SQL.class);
            SqlType sqlType = sql.sqlType();
            MethodExecutorBuilder.Builder builder = new MethodExecutorBuilder()
                    .init(MethodExecutorUtils.getMethodDeclaredName(method))
                    .paramsType(method.getParameterTypes());
            return builder.targetMethodExecutor(getParentMethodExecutor(sqlType, builder, sql, tableInfo, method))
                    .ok()
                    .build();
        } else
            return null;
    }

    /**
     * 解析SQL模板，获取MethodExecutor的依赖对象
     *
     * @param sqlType
     * @param builder
     * @param sql
     * @param tableInfo
     * @param method
     * @return
     */
    private MethodExecutor getParentMethodExecutor(SqlType sqlType, MethodExecutorBuilder.Builder builder, SQL sql, TableInfo tableInfo, Method method) {
        switch (sqlType) {
            case QUERY:
                return queryMethodExecutor(builder, sql, tableInfo, method);
            case INSERT:
                return insertMethodExecutor(false, builder, sql, tableInfo, method);
            case BATCH_INSERT:
                return insertMethodExecutor(true, builder, sql, tableInfo, method);
            case DELETE:
                return deleteMethodExecutor(false, builder, sql, tableInfo, method);
            case BATCH_DELETE:
                return deleteMethodExecutor(true, builder, sql, tableInfo, method);
            case UPDATE:
                return updateMethodExecutor(builder, sql, tableInfo, method);
            default:
                throw new NotMappedMethodException("Not supported SqlType =>[" + sqlType + "]");
        }
    }

    /**
     * 解析SQL模板，获取QueryMethodExecutor对象，配置新的MethodExecutor
     * 需要将ParamTypeMapping按index排序
     *
     * @param builder
     * @param sql
     * @param tableInfo
     * @param method
     * @return
     */
    MethodExecutor queryMethodExecutor(MethodExecutorBuilder.Builder builder, SQL sql, TableInfo tableInfo, Method method) {
        MethodExecutor queryMethodExecutor = new QueryMethodExecutor();
        TemplateEngine templateEngine = SingletonConfiguration.getInstance().getTemplateEngine();
        TemplateParser templateParser = templateEngine.getParser();
        SqlTemplateInfo sqlTemplateInfo = templateParser.parse(sql.value(), method, tableInfo);
        builder.targetMethodExecutor(queryMethodExecutor)
                .sql(sqlTemplateInfo.getNaiveSql())
                .paramsType(method.getParameterTypes())
                .buildRowSql(metaData -> {
                    RowSql rowSql = new RowSql();
                    rowSql.setNativeSql(sqlTemplateInfo.getNaiveSql());
                    rowSql.setSqlType(SqlType.QUERY);
                    addParamsList(metaData, sqlTemplateInfo, rowSql, false);
                    return rowSql;
                })
                .ok()
                .build();
        return queryMethodExecutor;
    }

    /**
     * 添加参数列表
     *
     * @param metaData
     * @param sqlTemplateInfo
     * @param rowSql
     * @param batch
     */
    private void addParamsList(MetaData metaData, SqlTemplateInfo sqlTemplateInfo, RowSql rowSql, boolean batch) {
        if (sqlTemplateInfo.getParameterTypeMapping() != null
                && (sqlTemplateInfo.getSqlParamWays() != null && !sqlTemplateInfo.getSqlParamWays().isEmpty())) {
            List<Node<ValueReader>> ways = sqlTemplateInfo.getSqlParamWays();
            Object[] args = metaData.getProperty("args");
            ParamTypeMapping rootTypeMapping = sqlTemplateInfo.getParameterTypeMapping();
            ParamTypeMapping batchParamTypeMapping = rootTypeMapping.getChild(rootTypeMapping.getBatchProperty());
            if (batch && rootTypeMapping.isBatchParam()) {
                Object bean = args[0];
                int length = -1;
                if (batchParamTypeMapping.isArray())
                    length = Array.getLength(bean);
                else if (batchParamTypeMapping.isCollection())
                    length = ((Collection<?>) bean).size();
                else
                    length = 1;
                List<TypeConvertor<?>> typeConvertors = new LinkedList<>();
                boolean notSet = false;
                for (int i = 0; i < length; i++) {
                    Object rowProperty = batchParamTypeMapping.getProperty(bean, i);
                    List<Object> params = new LinkedList<>();
                    addParams(rowSql, batchParamTypeMapping.getChild(rootTypeMapping.getBatchProperty()), rowProperty, ways, params, typeConvertors, notSet);
                    notSet = true;
                }
            } else {
                List<TypeConvertor<?>> typeConvertors = new LinkedList<>();
                List<Object> params = new LinkedList<>();
                addParams(rowSql, rootTypeMapping, args, ways, params, typeConvertors, false);
            }
        }
    }

    /**
     * 向参数列表添加参数
     *
     * @param rowSql
     * @param rootTypeMapping
     * @param o
     * @param ways
     * @param params
     */
    private void addParams(RowSql rowSql, ParamTypeMapping rootTypeMapping, Object o, List<Node<ValueReader>> ways, List<Object> params, List<TypeConvertor<?>> typeConvertors, boolean notSet) {
        MetaProperty<ParamTypeMapping> metaProperty = new MetaProperty<>(rootTypeMapping, o, null);
        for (Node<ValueReader> wayLinkedNode : ways) {
            MetaProperty.Result<ParamTypeMapping, ValueReader> result = metaProperty.searchByWay(wayLinkedNode);
            ValueReader.Value value = result.getNode().getValue().read(result.getBean());
            if (value == ValueReader.Value.NULL)
                throw new ValueReaderException("The value of the parameter cannot be null.Please check log.");
            if (value != null) {
                Object param = value.getValue();
                if (param == null)
                    params.add(null);
                else if (value.isArray()) {
                    for (int i = 0; i < Array.getLength(value.getValue()); i++) {
                        params.add(Array.get(value.getValue(), i));
                    }
                } else if (value.isCollection()) {
                    params.addAll((Collection<?>) value.getValue());
                } else {
                    params.add(value.getValue());
                }
                if (result.getPropertyTypeMapping() == null)
                    throw new TemplateException("It may be a bug.TypeMapping must not be null.");
                if (!notSet) {
                    TypeConvertor<?> typeConvertor = result.getBasicTypeMapping().getTypeConvertor();
                    if (typeConvertor == null)
                        typeConvertor = TypeConvertorManager.getConvertor(result.getBasicTypeMapping().getType());
                    typeConvertors.add(typeConvertor);
                }
            }
        }
        if (!notSet)
            rowSql.setTypeConvertors(typeConvertors);
        rowSql.getParamsList().add(params);
    }

    /**
     * 解析SQL模板，获取InsertMethodExecutor对象，配置新的MethodExecutor
     *
     * @param batch
     * @param builder
     * @param sql
     * @param tableInfo
     * @param method
     * @return
     */
    MethodExecutor insertMethodExecutor(boolean batch, MethodExecutorBuilder.Builder builder, SQL sql, TableInfo tableInfo, Method method) {
        MethodExecutor insertMethodExecutor = new InsertMethodExecutor();
        TemplateEngine templateEngine = SingletonConfiguration.getInstance().getTemplateEngine();
        TemplateParser templateParser = templateEngine.getParser();
        SqlTemplateInfo sqlTemplateInfo = templateParser.parse(sql.value(), method, tableInfo);
        builder.targetMethodExecutor(insertMethodExecutor)
                .sql(sqlTemplateInfo.getNaiveSql())
                .paramsType(method.getParameterTypes())
                .buildRowSql(metaData -> {
                    RowSql rowSql = new RowSql();
                    if (batch)
                        rowSql.setSqlType(SqlType.BATCH);
                    else
                        rowSql.setSqlType(SqlType.UPDATE);
                    addParamsList(metaData, sqlTemplateInfo, rowSql, batch);
                    return rowSql;
                })
                .ok()
                .build();
        return insertMethodExecutor;
    }

    /**
     * 解析SQL模板，获取DeleteMethodExecutor对象，配置新的MethodExecutor
     *
     * @param batch
     * @param builder
     * @param sql
     * @param tableInfo
     * @param method
     * @return
     */
    MethodExecutor deleteMethodExecutor(boolean batch, MethodExecutorBuilder.Builder builder, SQL sql, TableInfo tableInfo, Method method) {
        MethodExecutor deleteMethodExecutor = new DeleteMethodExecutor();
        TemplateEngine templateEngine = SingletonConfiguration.getInstance().getTemplateEngine();
        TemplateParser templateParser = templateEngine.getParser();
        SqlTemplateInfo sqlTemplateInfo = templateParser.parse(sql.value(), method, tableInfo);
        builder.targetMethodExecutor(deleteMethodExecutor)
                .sql(sqlTemplateInfo.getNaiveSql())
                .paramsType(method.getParameterTypes())
                .buildRowSql(metaData -> {
                    RowSql rowSql = new RowSql();
                    if (batch)
                        rowSql.setSqlType(SqlType.BATCH);
                    else
                        rowSql.setSqlType(SqlType.UPDATE);
                    addParamsList(metaData, sqlTemplateInfo, rowSql, batch);
                    return rowSql;
                })
                .ok()
                .build();
        return deleteMethodExecutor;
    }

    /**
     * 解析SQL模板，获取UpdateMethodExecutor对象，配置新的MethodExecutor
     *
     * @param builder
     * @param sql
     * @param tableInfo
     * @param method
     * @return
     */
    MethodExecutor updateMethodExecutor(MethodExecutorBuilder.Builder builder, SQL sql, TableInfo tableInfo, Method method) {
        MethodExecutor updateMethodExecutor = new UpdateMethodExecutor();
        TemplateEngine templateEngine = SingletonConfiguration.getInstance().getTemplateEngine();
        TemplateParser templateParser = templateEngine.getParser();
        SqlTemplateInfo sqlTemplateInfo = templateParser.parse(sql.value(), method, tableInfo);
        builder.targetMethodExecutor(updateMethodExecutor)
                .sql(sqlTemplateInfo.getNaiveSql())
                .paramsType(method.getParameterTypes())
                .buildRowSql(metaData -> {
                    RowSql rowSql = new RowSql();
                    rowSql.setSqlType(SqlType.UPDATE);
                    addParamsList(metaData, sqlTemplateInfo, rowSql, false);
                    return rowSql;
                })
                .ok()
                .build();
        return updateMethodExecutor;
    }
}
