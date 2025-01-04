package com.maxwellnie.velox.sql.core.ext.template;

import com.maxwellnie.velox.sql.core.natives.database.mapping.param.ParamTypeMapping;
import com.maxwellnie.velox.sql.core.natives.stream.Node;
import com.maxwellnie.velox.sql.core.utils.reflect.property.ValueReader;

import java.util.List;

/**
 * @author Maxwell Nie
 */
public class SqlTemplateInfo {
    private String naiveSql;
    private String templateSql;
    private ParamTypeMapping parameterTypeMapping;
    private List<Node<ValueReader>> sqlParamNodes;

    public String getNaiveSql() {
        return naiveSql;
    }

    public void setNaiveSql(String naiveSql) {
        this.naiveSql = naiveSql;
    }

    public String getTemplateSql() {
        return templateSql;
    }

    public void setTemplateSql(String templateSql) {
        this.templateSql = templateSql;
    }

    public ParamTypeMapping getParameterTypeMapping() {
        return parameterTypeMapping;
    }

    public void setParameterTypeMapping(ParamTypeMapping parameterTypeMapping) {
        this.parameterTypeMapping = parameterTypeMapping;
    }

    public List<Node<ValueReader>> getSqlParamWays() {
        return sqlParamNodes;
    }

    public void setSqlParamWays(List<Node<ValueReader>> sqlParamNodes) {
        this.sqlParamNodes = sqlParamNodes;
    }
}
