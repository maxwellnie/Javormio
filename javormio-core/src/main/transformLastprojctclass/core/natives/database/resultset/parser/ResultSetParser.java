package com.maxwellnie.velox.sql.core.natives.database.resultset.parser;

import com.maxwellnie.velox.sql.core.meta.MetaData;
import com.maxwellnie.velox.sql.core.meta.MetaObject;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeInnerMapping;
import com.maxwellnie.velox.sql.core.natives.exception.TypeMappingException;
import com.maxwellnie.velox.sql.core.natives.database.mapping.returntype.ReturnTypeMapping;
import com.maxwellnie.velox.sql.core.natives.database.resultset.hash.Hash;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertorManager;
import com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertor;
import com.maxwellnie.velox.sql.core.utils.base.TypeUtils;
import com.maxwellnie.velox.sql.core.utils.reflect.MetaField;
import com.maxwellnie.velox.sql.core.utils.reflect.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.maxwellnie.velox.sql.core.natives.type.convertor.TypeConvertorManager.DEFAULT_CONVERTOR;


/**
 * @author Maxwell Nie
 */
public class ResultSetParser {
    private static final String VALUE = "_meta_object";
    private static final String HASH = "_hash_code";
    private static final ConcurrentHashMap<ReturnTypeInnerMapping, Hash> HASH_KEY_MAP = new ConcurrentHashMap<>();

    /**
     * 转换结果集到对象
     */
    public Object parseResultSet(ResultSet resultSet, ReturnTypeMapping returnTypeMapping) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try {
            ReturnTypeInnerMapping returnTypeInnerMapping = returnTypeMapping.getTypeMapping();
            if (returnTypeInnerMapping.getParentTypeMapping() == null) {
                if (returnTypeInnerMapping == ReturnTypeInnerMapping.BATCH_ROW)
                    return getBatchAffectedRows(resultSet);
                else if (returnTypeInnerMapping == ReturnTypeInnerMapping.READ_ROW)
                    return getAffectedRows(resultSet, returnTypeMapping);
                else {
                    Object resultObject = ReflectionUtils.newInstance(returnTypeMapping.getType());
                    if (returnTypeMapping.isHasJoin())
                        resultObject = joinedConvert(resultSet, returnTypeMapping, resultObject);
                    else
                        resultObject = simpleConvert(resultSet, returnTypeMapping, resultObject);
                    if (returnTypeMapping.isReturnManyObject())
                        return resultObject;
                    else
                        return ((List<?>) resultObject).get(0);
                }
            } else
                throw new TypeMappingException("Error ReturnTypeInnerMapping [" + returnTypeInnerMapping + "]");
        } finally {
            resultSet.close();
        }
    }

    /**
     * 处理简单映射
     */
    private Object simpleConvert(ResultSet resultSet, ReturnTypeMapping returnTypeMapping, Object resultObject) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ReturnTypeInnerMapping returnTypeInnerMapping = returnTypeMapping.getTypeMapping();
        Object resultForObject = resultObject;
        while (!resultSet.isClosed() && resultSet.next()) {
            Object instance = ReflectionUtils.newInstance(returnTypeInnerMapping.getType());
            MetaObject metaObject = MetaObject.of(instance);
            for (ReturnTypeInnerMapping propertyReturnTypeInnerMapping : returnTypeInnerMapping.getInnerTypeMapping()) {
                Object object = propertyReturnTypeInnerMapping.getTypeConvertor().convert(resultSet, propertyReturnTypeInnerMapping.getColumnName());
                metaObject.setFieldValue(propertyReturnTypeInnerMapping.getMetaField().getName(), object);
            }
            resultForObject = addValueToParentObject(resultObject, instance);
        }
        return resultForObject;
    }

    /**
     * 处理关联数据
     *
     * @param resultSet
     * @param returnTypeMapping
     * @param resultObject
     * @return
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Object joinedConvert(ResultSet resultSet, ReturnTypeMapping returnTypeMapping, Object resultObject) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<Hash, MetaData> incompleteBuiltObjects = new LinkedHashMap<>();
        Object preResultObject = resultObject;
        ReturnTypeInnerMapping returnTypeInnerMapping = returnTypeMapping.getTypeMapping();
        Hash hash = typeMappingHashKey(returnTypeInnerMapping);
        while (!resultSet.isClosed() && resultSet.next()) {
            MetaData parentObjectMetaData = incompleteBuiltObjects.get(hash);
            preResultObject = handleRowData(resultSet, incompleteBuiltObjects, preResultObject, returnTypeInnerMapping, parentObjectMetaData, null);
        }
        incompleteBuiltObjects.clear();
        return preResultObject;
    }

    /**
     * 处理行数据
     *
     * @param resultSet
     * @param incompleteBuiltObjects
     * @param preResultObject
     * @param returnTypeInnerMapping
     * @param parentObjectMetaData
     * @param field
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private Object handleRowData(ResultSet resultSet, Map<Hash, MetaData> incompleteBuiltObjects, Object preResultObject, ReturnTypeInnerMapping returnTypeInnerMapping, MetaData parentObjectMetaData, MetaField field) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException {
        MetaObject parentMetaObject = null;
        Object rowObject;
        Object resultObject = preResultObject;
        if (parentObjectMetaData != null) {
            parentMetaObject = parentObjectMetaData.getProperty(VALUE);
            rowObject = readObject(resultSet, parentMetaObject, parentObjectMetaData.getProperty(HASH), typeMappingHashKey(returnTypeInnerMapping), returnTypeInnerMapping, incompleteBuiltObjects);
        } else {
            rowObject = readObject(resultSet, null, null, typeMappingHashKey(returnTypeInnerMapping), returnTypeInnerMapping, incompleteBuiltObjects);
        }
        if ((parentObjectMetaData == null) || (rowObject != null && rowObject != parentMetaObject.getObj())) {
            resultObject = linkRowObjectToResultObject(field, rowObject, preResultObject);
        }
        return resultObject;
    }

    /**
     * 生成类型映射的HashKey
     *
     * @param returnTypeInnerMapping
     * @return
     */
    private Hash typeMappingHashKey(ReturnTypeInnerMapping returnTypeInnerMapping) {
        if (HASH_KEY_MAP.containsKey(returnTypeInnerMapping))
            return HASH_KEY_MAP.get(returnTypeInnerMapping);
        Hash hash;
        if (returnTypeInnerMapping.getParentTypeMapping() == null) {
            hash = Hash.create(returnTypeInnerMapping);
        } else {
            hash = Hash.create();
            ReturnTypeInnerMapping currentReturnTypeInnerMapping = returnTypeInnerMapping;
            while (currentReturnTypeInnerMapping.getParentTypeMapping() != null) {
                hash.addValue(currentReturnTypeInnerMapping);
                currentReturnTypeInnerMapping = currentReturnTypeInnerMapping.getParentTypeMapping();
            }
        }
        HASH_KEY_MAP.put(returnTypeInnerMapping, hash);
        return hash;
    }

    /**
     * 链接行数据到父对象
     *
     * @param field
     * @param rowObject
     * @param resultForObject
     * @return Object
     * @throws IllegalAccessException
     */
    private Object linkRowObjectToResultObject(MetaField field, Object rowObject, Object resultForObject) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object preResultForObject = resultForObject;
        if (field == null)
            preResultForObject = addValueToParentObject(preResultForObject, rowObject);
        else
            addFieldValueToParentObject(preResultForObject, rowObject, field);
        return preResultForObject;
    }

    /**
     * 读取行数据
     *
     * @param resultSet
     * @param parentObject
     * @param typeMappingHash
     * @param returnTypeInnerMapping
     * @param incompleteBuiltObjects
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private Object readObject(ResultSet resultSet, MetaObject parentObject, Hash parentObjectHash, Hash typeMappingHash, ReturnTypeInnerMapping returnTypeInnerMapping, Map<Hash, MetaData> incompleteBuiltObjects) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException {
        Object object = ReflectionUtils.newInstance(returnTypeInnerMapping.getType());
        MetaObject metaObject = MetaObject.of(object);
        Hash hash = Hash.create();
        Object result;
        if (parentObject != null) {
            result = parentObject.getObj();
            handleProperties(resultSet, returnTypeInnerMapping, incompleteBuiltObjects, object, metaObject, hash);
            if (hash.equals(parentObjectHash) && !hash.equals(Hash.NO_HASH_KEY)) {
                mergeChildObjectAndParentObject(parentObject, metaObject, returnTypeInnerMapping.getInnerTypeMapping());
            } else {
                result = metaObject.getObj();
                createNewIncompleteBuiltObject(metaObject, hash, incompleteBuiltObjects, typeMappingHash);
            }
        } else {
            handleProperties(resultSet, returnTypeInnerMapping, incompleteBuiltObjects, object, metaObject, hash);
            createNewIncompleteBuiltObject(metaObject, hash, incompleteBuiltObjects, typeMappingHash);
            result = metaObject.getObj();
        }
        return result;
    }

    /**
     * 处理属性
     *
     * @param resultSet
     * @param typeMapping
     * @param incompleteBuiltObjects
     * @param object
     * @param metaObject
     * @param hash
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private void handleProperties(ResultSet resultSet, ReturnTypeInnerMapping typeMapping, Map<Hash, MetaData> incompleteBuiltObjects, Object object, MetaObject metaObject, Hash hash) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, SQLException {
        for (ReturnTypeInnerMapping returnTypeInnerMapping : typeMapping.getInnerTypeMapping()) {
            if (returnTypeInnerMapping.isNeedInstantiate() && !returnTypeInnerMapping.getInnerTypeMapping().isEmpty()) {
                Hash childHash = typeMappingHashKey(returnTypeInnerMapping);
                MetaData parentObjectMetaData = incompleteBuiltObjects.get(childHash);
                handleRowData(resultSet, incompleteBuiltObjects, object, returnTypeInnerMapping, parentObjectMetaData, returnTypeInnerMapping.getMetaField());
            } else {
                Object unitValue = getUnitValue(resultSet, returnTypeInnerMapping);
                metaObject.setFieldValue(returnTypeInnerMapping.getMetaField().getName(), unitValue);
                if (returnTypeInnerMapping.isJoinedFlag())
                    hash.addValues(returnTypeInnerMapping.getMetaField().getName(), unitValue);
            }
        }
    }

    /**
     * 创建未完成的对象
     *
     * @param metaObject
     * @param hash
     * @param incompleteBuiltObjects
     * @param parentObjectHash
     */
    private void createNewIncompleteBuiltObject(MetaObject metaObject, Hash hash, Map<Hash, MetaData> incompleteBuiltObjects, Hash parentObjectHash) {
        MetaData metaData = MetaData.ofEmpty();
        metaData.addProperty(VALUE, metaObject);
        metaData.addProperty(HASH, hash);
        incompleteBuiltObjects.put(parentObjectHash, metaData);
    }

    /**
     * 合并子对象和父对象
     *
     * @param parentObject
     * @param childObject
     * @param propertyMappings
     */
    private void mergeChildObjectAndParentObject(MetaObject parentObject, MetaObject childObject, List<ReturnTypeInnerMapping> propertyMappings) {
        for (ReturnTypeInnerMapping propertyMapping : propertyMappings) {
            Class<?> type = propertyMapping.getType();
            if (isNotEntityMetaData(parentObject)) {
                if (propertyMapping.isNeedInstantiate()) {
                    ((Collection) parentObject.getObj()).addAll((Collection) childObject.getObj());
                }
                continue;
            }
            Object parentPropertyObject = parentObject.getFieldValue(propertyMapping.getMetaField().getName());
            Object childPropertyObject = childObject.getFieldValue(propertyMapping.getMetaField().getName());
            if (childPropertyObject == null) ;
            else if (propertyMapping.isNeedInstantiate()) {
                if (TypeUtils.isCollection(type))
                    ((Collection) parentPropertyObject).addAll((Collection) childPropertyObject);
                else
                    parentObject.setFieldValue(propertyMapping.getMetaField().getName(), childPropertyObject);
            }
        }
    }

    private boolean isNotEntityMetaData(MetaObject metaObject) {
        if (TypeUtils.isCollection(metaObject.getObj().getClass()) || TypeUtils.isArray(metaObject.getObj().getClass())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加子对象到父对象
     *
     * @param parentObject
     * @param object
     * @param field
     * @throws IllegalAccessException
     */
    private void addFieldValueToParentObject(Object parentObject, Object object, MetaField field) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> type = field.getType();
        Object fieldObject = field.get(parentObject);
        if (fieldObject == null) {
            field.set(parentObject, object);
        } else if (TypeUtils.isCollection(type)) {
            ((Collection) fieldObject).add(object);
        } else
            field.set(parentObject, object);
    }

    /**
     * 添加子对象到父对象
     *
     * @param parentObject
     * @param object
     * @return
     */
    private Object addValueToParentObject(Object parentObject, Object object) {
        Class<?> type = parentObject.getClass();
        Object resultForParentObject = parentObject;
        if (TypeUtils.isCollection(type))
            ((Collection) parentObject).add(object);
        else
            resultForParentObject = object;
        return resultForParentObject;
    }

    /**
     * 获取单元值
     *
     * @param resultSet
     * @param returnTypeInnerMapping
     * @return
     * @throws SQLException
     */
    private Object getUnitValue(ResultSet resultSet, ReturnTypeInnerMapping returnTypeInnerMapping) throws SQLException {
        if (returnTypeInnerMapping.getInnerTypeMapping() == null) {
            return returnTypeInnerMapping.getTypeConvertor().convert(resultSet, returnTypeInnerMapping.getColumnName());
        } else {
            return null;
        }
    }

    /**
     * 获取受影响行数
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Object getAffectedRows(ResultSet resultSet, ReturnTypeMapping returnTypeMapping) throws SQLException {
        TypeConvertor<?> typeConvertor = TypeConvertorManager.getConvertor(returnTypeMapping.getType());
        if (resultSet == null || resultSet.isClosed() || !resultSet.next() || typeConvertor == DEFAULT_CONVERTOR) {
            return typeConvertor.getEmpty();
        } else
            return typeConvertor.convert(resultSet, 1);
    }

    /**
     * 获取批量操作受影响行数
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private int[] getBatchAffectedRows(ResultSet resultSet) throws SQLException {
        if (resultSet == null || resultSet.isClosed() || !resultSet.next())
            return new int[0];
        else {
            int[] batchAffectedRows = new int[0];
            while (resultSet.next()) {
                int[] newBatchAffectedRows = new int[batchAffectedRows.length + 1];
                if (batchAffectedRows.length > 0)
                    System.arraycopy(batchAffectedRows, 0, newBatchAffectedRows, 0, batchAffectedRows.length);
                newBatchAffectedRows[newBatchAffectedRows.length - 1] = resultSet.getInt(1);
                batchAffectedRows = newBatchAffectedRows;
            }
            return batchAffectedRows;
        }
    }
}