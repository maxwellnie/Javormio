package com.maxwellnie.velox.sql.core.natives.database.mapping.returntype;

import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfoManager;
import com.maxwellnie.velox.sql.core.natives.database.table.column.ColumnInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.column.PrimaryInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.join.JoinInfo;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertorManager;
import com.maxwellnie.velox.sql.core.utils.base.TypeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertorManager.DEFAULT_CONVERTOR;

/**
 * 类型解析器，用于解析返回值类型
 *
 * @author Maxwell Nie
 */
public class DefaultReturnReturnTypeParser implements ReturnTypeParser {
    @Override
    public ReturnTypeMapping parse(Class<?> returnType, Class<?> entityClass) {
        return parse(returnType, TableInfoManager.getTableInfo(entityClass));
    }

    @Override
    public ReturnTypeMapping parse(Class<?> returnType, TableInfo tableInfo) {
        ReturnTypeMapping returnTypeMapping = new ReturnTypeMapping();
        returnTypeMapping.setType(returnType);
        if (returnType.isArray()) {
            if (returnType.equals(int[].class)) {
                returnTypeMapping.setTypeMapping(ReturnTypeInnerMapping.BATCH_ROW);
                return returnTypeMapping;
            }
            throw new UnsupportedOperationException("Array type[" + returnType + "] is not supported.");
        }
        if (TypeConvertorManager.getConvertor(returnType) != DEFAULT_CONVERTOR) {
            returnTypeMapping.setTypeMapping(ReturnTypeInnerMapping.READ_ROW);
            return returnTypeMapping;
        }
        Class<?> finalReturnType = getAdaptableType(returnType);
        boolean isCollection = TypeUtils.isCollection(finalReturnType);
        if (isCollection) {
            returnTypeMapping.setReturnManyObject(true);
        }
        if (!isCollection && finalReturnType == returnType) {
            finalReturnType = ArrayList.class;
        }
        ReturnTypeInnerMapping rootReturnTypeInnerMapping = getTypeMapping(tableInfo, tableInfo.getMappedClazz(), null, returnTypeMapping);
        rootReturnTypeInnerMapping.setCollection(true);
        returnTypeMapping.setType(finalReturnType);
        if (!tableInfo.getJoinInfos().isEmpty())
            returnTypeMapping.setHasJoin(true);
        rootReturnTypeInnerMapping.setNeedInstantiate(true);
        returnTypeMapping.setTypeMapping(rootReturnTypeInnerMapping);
        return returnTypeMapping;
    }

    /**
     * 获取类型映射
     *
     * @param tableInfo
     * @param finalReturnType
     * @param parentReturnTypeInnerMapping
     * @return
     */
    private ReturnTypeInnerMapping getTypeMapping(TableInfo tableInfo, Class<?> finalReturnType, ReturnTypeInnerMapping parentReturnTypeInnerMapping, ReturnTypeMapping returnTypeMapping) {
        ReturnTypeInnerMapping returnTypeInnerMapping = new ReturnTypeInnerMapping(finalReturnType);
        returnTypeInnerMapping.setParentTypeMapping(parentReturnTypeInnerMapping);
        returnTypeInnerMapping.setNeedInstantiate(true);
        List<ReturnTypeInnerMapping> innerReturnTypeInnerMappings = new ArrayList<>();
        if (tableInfo.hasPk()) {
            PrimaryInfo pkColumn = tableInfo.getPkColumn();
            ReturnTypeInnerMapping pkPropertyMapping = parserPropertyMapping(returnTypeInnerMapping, pkColumn);
            innerReturnTypeInnerMappings.add(pkPropertyMapping);
        }
        for (ColumnInfo columnInfo : tableInfo.getColumnMappedMap().values()) {
            ReturnTypeInnerMapping propertyMapping = parserPropertyMapping(returnTypeInnerMapping, columnInfo);
            innerReturnTypeInnerMappings.add(propertyMapping);
        }
        returnTypeMapping.setHasJoin(parseHasJoinedChildPropertyTypeMapping(tableInfo, returnTypeInnerMapping, innerReturnTypeInnerMappings, returnTypeMapping));
        returnTypeInnerMapping.setInnerTypeMapping(innerReturnTypeInnerMappings);
        return returnTypeInnerMapping;
    }

    /**
     * 解析关联的属性类型映射
     *
     * @param tableInfo
     * @param returnTypeInnerMapping
     * @param innerReturnTypeInnerMappings
     * @return
     */
    private boolean parseHasJoinedChildPropertyTypeMapping(TableInfo tableInfo, ReturnTypeInnerMapping returnTypeInnerMapping, List<ReturnTypeInnerMapping> innerReturnTypeInnerMappings, ReturnTypeMapping returnTypeMapping) {
        boolean hasJoined = !tableInfo.getJoinInfos().isEmpty();
        for (JoinInfo joinInfo : tableInfo.getJoinInfos()) {
            TableInfo slaveTableInfo;
            if (joinInfo.isNotNested()) {
                slaveTableInfo = TableInfoManager.getTableInfo(tableInfo.getMappedClazz().getName() + " - " + joinInfo.getSlaveTableName());
                for (ColumnInfo columnInfo : slaveTableInfo.getColumnMappedMap().values()) {
                    ReturnTypeInnerMapping propertyMapping = parserPropertyMapping(returnTypeInnerMapping, columnInfo);
                    innerReturnTypeInnerMappings.add(propertyMapping);
                }
                return false;
            }
            slaveTableInfo = TableInfoManager.getTableInfo(joinInfo.getSlaveTable());
            Class<?> fieldAdaptTableClass = getAdaptableType(joinInfo.getField().getType());
            boolean isCollection = TypeUtils.isCollection(fieldAdaptTableClass);
            ReturnTypeInnerMapping slaveReturnTypeInnerMapping;
            if (isCollection && joinInfo.isManyToMany()) {
                slaveReturnTypeInnerMapping = getJoinedWrapperTypeMapping(returnTypeInnerMapping, joinInfo, slaveTableInfo, fieldAdaptTableClass, returnTypeMapping);
            } else {
                slaveReturnTypeInnerMapping = getTypeMapping(slaveTableInfo, joinInfo.getSlaveTable(), returnTypeInnerMapping, returnTypeMapping);
            }
            if (slaveReturnTypeInnerMapping.isCollection()) {
                MetaData fieldsMetaData = tableInfo.getOtherInfo().getProperty("fields");
                for (ReturnTypeInnerMapping parentPropertyReturnTypeInnerMapping : innerReturnTypeInnerMappings) {
                    if (parentPropertyReturnTypeInnerMapping.getMetaField().getField() == fieldsMetaData.getProperty(joinInfo.getMasterTableField()))
                        parentPropertyReturnTypeInnerMapping.setJoinedFlag(true);
                }
            }
            innerReturnTypeInnerMappings.add(slaveReturnTypeInnerMapping);
        }
        return hasJoined;
    }

    /**
     * 获取关联表的包装器的类型映射
     *
     * @param parentReturnTypeInnerMapping
     * @param joinInfo
     * @param slaveTableInfo
     * @param fieldAdaptTableClass
     * @param returnTypeMapping
     * @return
     */
    private ReturnTypeInnerMapping getJoinedWrapperTypeMapping(ReturnTypeInnerMapping parentReturnTypeInnerMapping, JoinInfo joinInfo, TableInfo slaveTableInfo, Class<?> fieldAdaptTableClass, ReturnTypeMapping returnTypeMapping) {
        ReturnTypeInnerMapping slaveReturnTypeInnerMapping = new ReturnTypeInnerMapping(fieldAdaptTableClass);
        slaveReturnTypeInnerMapping.setParentTypeMapping(parentReturnTypeInnerMapping);
        slaveReturnTypeInnerMapping.setMetaField(joinInfo.getField());
        slaveReturnTypeInnerMapping.setNeedInstantiate(true);
        slaveReturnTypeInnerMapping.setCollection(true);
        List<ReturnTypeInnerMapping> slaveTypeMappingInnerReturnTypeInnerMappings = new ArrayList<>();
        slaveTypeMappingInnerReturnTypeInnerMappings.add(getTypeMapping(slaveTableInfo, joinInfo.getSlaveTable(), slaveReturnTypeInnerMapping, returnTypeMapping));
        slaveReturnTypeInnerMapping.setInnerTypeMapping(slaveTypeMappingInnerReturnTypeInnerMappings);
        return slaveReturnTypeInnerMapping;
    }

    /**
     * 获取属性映射
     *
     * @param returnTypeInnerMapping
     * @param columnInfo
     * @return
     */
    private ReturnTypeInnerMapping parserPropertyMapping(ReturnTypeInnerMapping returnTypeInnerMapping, ColumnInfo columnInfo) {
        if (columnInfo.getTypeConvertor() != null) {
            ReturnTypeInnerMapping propertyReturnTypeInnerMapping = new ReturnTypeInnerMapping(columnInfo.getColumnMappedField().getType());
            propertyReturnTypeInnerMapping.setTypeConvertor(columnInfo.getTypeConvertor());
            propertyReturnTypeInnerMapping.setColumnName(columnInfo.getColumnName());
            propertyReturnTypeInnerMapping.setMetaField(columnInfo.getColumnMappedField());
            propertyReturnTypeInnerMapping.setParentTypeMapping(returnTypeInnerMapping);
            return propertyReturnTypeInnerMapping;
        }
        return null;
    }

    /**
     * 获取适配的返回类型
     *
     * @param returnType
     * @return
     */
    private Class<?> getAdaptableType(Class<?> returnType) {
        if (returnType.isInterface()) {
            if (List.class.isAssignableFrom(returnType))
                return ArrayList.class;
            else if (Set.class.isAssignableFrom(returnType))
                return HashSet.class;
            else
                return ArrayList.class;
        } else {
            if (List.class.isAssignableFrom(returnType))
                return ArrayList.class;
            else if (Set.class.isAssignableFrom(returnType))
                return HashSet.class;
            else {
                return returnType;
            }
        }
    }
}
