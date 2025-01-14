package io.github.maxwellnie.javormio.core.database.sql.method;

import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.java.reflect.MethodFeature;

import java.lang.reflect.Method;

/**
 * DAO接口的方法特征
 * @author Maxwell Nie
 */
public class DaoMethodFeature extends MethodFeature {
    /**
     * 类型映射
     */
    final TypeMapping typeMapping;
    final boolean multipleTable;
    public DaoMethodFeature(Method method, TypeMapping typeMapping, boolean multipleTable) {
        super(method);
        this.typeMapping = typeMapping;
        this.multipleTable = multipleTable;
    }

    public TypeMapping getTypeMapping() {
        return typeMapping;
    }

    public boolean isMultipleTable() {
        return multipleTable;
    }
}
