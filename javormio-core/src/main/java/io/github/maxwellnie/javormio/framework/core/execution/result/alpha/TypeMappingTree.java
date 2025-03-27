package io.github.maxwellnie.javormio.framework.core.execution.result.alpha;

import io.github.maxwellnie.javormio.framework.core.execution.result.TypeMapping;

/**
 * @author Maxwell Nie
 */
public class TypeMappingTree {
    final boolean complexQuery;
    final TypeMapping rootTypeMapping;
    final TypeMapping masterTypeMapping;

    public TypeMappingTree(boolean complexQuery, TypeMapping rootTypeMapping, TypeMapping masterTypeMapping) {
        this.complexQuery = complexQuery;
        this.rootTypeMapping = rootTypeMapping;
        this.masterTypeMapping = masterTypeMapping;
    }

    public TypeMapping getMasterTypeMapping() {
        return masterTypeMapping;
    }


    public boolean isComplexQuery() {
        return complexQuery;
    }

    public TypeMapping getRootTypeMapping() {
        return rootTypeMapping;
    }
}
