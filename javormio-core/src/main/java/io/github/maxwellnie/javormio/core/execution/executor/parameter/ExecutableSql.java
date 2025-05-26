package io.github.maxwellnie.javormio.core.execution.executor.parameter;

import io.github.maxwellnie.javormio.core.translation.SqlParameter;
import io.github.maxwellnie.javormio.common.java.table.primary.KeyGenerator;

import java.util.List;

/**
 * 可直接打开报表提交到数据库的SQL对象
 *
 * @author Maxwell Nie
 */
public class ExecutableSql {

    /**
     * SQL列表
     */
    private String sql;
    /**
     * 参数列表
     */
    private List<SqlParameter[]> parametersList;
    /**
     * 主键生成器列表
     */
    private KeyGenerator[] keyGenerators;
    /**
     * 主键生成器参数列表
     */
    private List<Object> keyGeneratorParameters;
    /**
     * 类型
     */
    private int type;

    public ExecutableSql(String sql, List<SqlParameter[]> parametersList, KeyGenerator[] keyGenerators, List<Object> keyGeneratorParameters, int type) {
        this.sql = sql;
        this.parametersList = parametersList;
        this.keyGenerators = keyGenerators;
        this.keyGeneratorParameters = keyGeneratorParameters;
        this.type = type;
    }

    public ExecutableSql() {
    }

    public List<SqlParameter[]> getParametersList() {
        return parametersList;
    }

    public void setParametersList(List<SqlParameter[]> parametersList) {
        this.parametersList = parametersList;
    }

    public int getType() {
        return type;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getKeyGeneratorParameters() {
        return keyGeneratorParameters;
    }

    public void setKeyGeneratorParameters(List<Object> keyGeneratorParameters) {
        this.keyGeneratorParameters = keyGeneratorParameters;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public KeyGenerator[] getKeyGenerators() {
        return keyGenerators;
    }

    public void setKeyGenerators(KeyGenerator[] keyGenerators) {
        this.keyGenerators = keyGenerators;
    }

    public void setType(int type) {
        this.type = type;
    }

}
