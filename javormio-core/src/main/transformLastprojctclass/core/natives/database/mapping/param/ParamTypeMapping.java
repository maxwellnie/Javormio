package com.maxwellnie.velox.sql.core.natives.database.mapping.param;

import com.maxwellnie.velox.sql.core.natives.database.mapping.TypeMapping;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;

/**
 * @author Maxwell Nie
 */
public class ParamTypeMapping extends TypeMapping<ParamTypeMapping> {
    TypeConvertor<?> typeConvertor;
    int argumentIndex;
    boolean batchParam;
    Object batchProperty;
    boolean isComplexType;

    public int getArgumentIndex() {
        return argumentIndex;
    }

    public void setArgumentIndex(int argumentIndex) {
        this.argumentIndex = argumentIndex;
    }

    public TypeConvertor<?> getTypeConvertor() {
        return typeConvertor;
    }

    public void setTypeConvertor(TypeConvertor<?> typeConvertor) {
        this.typeConvertor = typeConvertor;
    }

    public boolean isBatchParam() {
        return batchParam;
    }

    public void setBatchParam(boolean batchParam) {
        this.batchParam = batchParam;
    }

    public Object getBatchProperty() {
        return batchProperty;
    }

    public void setBatchProperty(Object batchProperty) {
        this.batchProperty = batchProperty;
    }

    public boolean isComplexType() {
        return isComplexType;
    }

    public void setComplexType(boolean complexType) {
        isComplexType = complexType;
    }

    @Override
    public String toString() {
        return "ParamTypeMapping{" +
                "typeConvertor=" + typeConvertor +
                ", argumentIndex=" + argumentIndex +
                ", batchParam=" + batchParam +
                ", batchProperty=" + batchProperty +
                ", isComplexObject=" + isComplexType +
                "} " + super.toString();
    }
}
