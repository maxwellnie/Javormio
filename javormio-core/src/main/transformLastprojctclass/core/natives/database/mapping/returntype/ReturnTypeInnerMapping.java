package com.maxwellnie.velox.sql.core.natives.database.mapping.returntype;

import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;
import com.maxwellnie.velox.sql.core.utils.reflect.MetaField;

import java.util.List;

/**
 * 类型映射
 */
public class ReturnTypeInnerMapping {
    public static final ReturnTypeInnerMapping READ_ROW = new ReturnTypeInnerMapping(ReturnTypeInnerMapping.class);
    public static final ReturnTypeInnerMapping BATCH_ROW = new ReturnTypeInnerMapping(ReturnTypeInnerMapping.class);
    Class<?> type;
    List<ReturnTypeInnerMapping> returnTypeInnerMapping;
    ReturnTypeInnerMapping parentReturnTypeInnerMapping;
    TypeConvertor<?> typeConvertor;
    String columnName;
    MetaField metaField;
    boolean isNeedInstantiate = false;

    ReturnTypeInnerMapping primaryKeyPropertyMapping;

    boolean isJoinedFlag;
    boolean isCollection;

    public ReturnTypeInnerMapping() {
    }

    ReturnTypeInnerMapping(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public List<ReturnTypeInnerMapping> getInnerTypeMapping() {
        return returnTypeInnerMapping;
    }

    public void setInnerTypeMapping(List<ReturnTypeInnerMapping> returnTypeInnerMapping) {
        this.returnTypeInnerMapping = returnTypeInnerMapping;
    }

    public ReturnTypeInnerMapping getParentTypeMapping() {
        return parentReturnTypeInnerMapping;
    }

    public void setParentTypeMapping(ReturnTypeInnerMapping parentReturnTypeInnerMapping) {
        this.parentReturnTypeInnerMapping = parentReturnTypeInnerMapping;
    }

    public TypeConvertor<?> getTypeConvertor() {
        return typeConvertor;
    }

    public void setTypeConvertor(TypeConvertor<?> typeConvertor) {
        this.typeConvertor = typeConvertor;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public MetaField getMetaField() {
        return metaField;
    }

    public void setMetaField(MetaField metaField) {
        this.metaField = metaField;
    }

    public boolean isNeedInstantiate() {
        return isNeedInstantiate;
    }

    public void setNeedInstantiate(boolean needInstantiate) {
        isNeedInstantiate = needInstantiate;
    }

    public ReturnTypeInnerMapping getPrimaryKeyPropertyMapping() {
        return primaryKeyPropertyMapping;
    }

    public void setPrimaryKeyPropertyMapping(ReturnTypeInnerMapping primaryKeyPropertyMapping) {
        this.primaryKeyPropertyMapping = primaryKeyPropertyMapping;
    }

    public boolean isJoinedFlag() {
        return isJoinedFlag;
    }

    public void setJoinedFlag(boolean joinedFlag) {
        isJoinedFlag = joinedFlag;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }
}
