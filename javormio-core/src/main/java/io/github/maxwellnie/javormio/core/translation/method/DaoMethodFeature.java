package io.github.maxwellnie.javormio.core.translation.method;

import io.github.maxwellnie.javormio.common.java.api.ObjectMap;
import io.github.maxwellnie.javormio.common.java.reflect.MethodFeature;

import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * DAO接口的方法特征
 *
 * @author Maxwell Nie
 */
public class DaoMethodFeature<E> extends MethodFeature {
    /**
     * 类型映射
     */
    final ObjectMap<ResultSet, E> typeMapping;

    public DaoMethodFeature(Method method, ObjectMap<ResultSet, E> typeMapping) {
        super(method);
        this.typeMapping = typeMapping;
    }

    public ObjectMap<ResultSet, E> getTypeMapping() {
        return typeMapping;
    }

}
