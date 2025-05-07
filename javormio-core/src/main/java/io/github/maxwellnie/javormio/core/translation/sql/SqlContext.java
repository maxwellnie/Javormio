package io.github.maxwellnie.javormio.core.translation.sql;

import io.github.maxwellnie.javormio.common.java.reflect.ObjectFactory;

import java.util.Map;

/**
 * @author Maxwell Nie
 */
public class SqlContext {
    private Map<Object, String> aliasMap;
    private ObjectFactory<SqlBuilder> sqlBuilderFactory;
    private SqlExpressionBuilder sqlExpressionBuilder;
}
