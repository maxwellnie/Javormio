package io.github.maxwellnie.javormio.core.database.result.alpha;

import io.github.maxwellnie.javormio.core.OperationContext;
import io.github.maxwellnie.javormio.core.database.result.ConvertException;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.java.reflect.ReflectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 * @author Maxwell Nie
 */
public class DefaultResultSetConvertor implements ResultSetConvertor {
    @Override
    public Object convert(TypeMappingTree typeMappingTree, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws ConvertException, SQLException {
        if (resultSet == null || typeMappingTree == null || typeMappingTree.rootTypeMapping == null)
            throw new ConvertException("ResultSet or TypeMapping is null");
        if (resultSet.isClosed())
            throw new ConvertException("ResultSet is closed.");
        if (typeMappingTree.complexQuery)
            return complexConvert(typeMappingTree.rootTypeMapping, typeMappingTree.masterTypeMapping, columnIndexMap, context, resultSet))
        ;
        return simpleConvert(typeMappingTree.rootTypeMapping, columnIndexMap, context, resultSet);
    }

    private Object complexConvert(TypeMapping rootTypeMapping, TypeMapping masterTypeMapping, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws SQLException, ConvertException {
        Map<Integer, Object> previousObjectsHashMap = new LinkedHashMap<>();
        Object previousRootObject = null;
        while (resultSet.next()) {
            if (previousRootObject != null) {
                for (Map.Entry<String, TypeMapping> child : rootTypeMapping.getChildren().entrySet()) {
                    linkedChildToParentObject(previousRootObject, rootTypeMapping, child.getKey(), buildUnitObject(child.getValue(), columnIndexMap, context, resultSet));
                }
            } else
                previousRootObject = buildUnitObject(rootTypeMapping, rootTypeMapping == masterTypeMapping, masterTypeMapping, previousObjectsHashMap, columnIndexMap, context, resultSet);
        }
        return previousRootObject;
    }

    private Object buildUnitObject(TypeMapping typeMapping, boolean record, TypeMapping masterTypeMapping, Map<Integer, Object> previousObjectsHashMap, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws ConvertException, SQLException {
        try {
            if (typeMapping.isComplex()) {
                Object unitObject = previousObjectsHashMap.get(typeMapping.hashCode());
                if (!record && unitObject == null)
                    unitObject = previousObjectsHashMap.put(typeMapping.hashCode(), typeMapping
                            .getReflection()
                            .getObjectFactory()
                            .produce()
                    );
                int hash = 0;
                LinkedList<TypeMappingAndObject> nodeObjectList = new LinkedList<>();
                for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()) {
                    if (record) {
                        TypeMappingAndObject typeMappingAndObject = new TypeMappingAndObject(
                                child.getValue(),
                                buildUnitObject(
                                        child.getValue(),
                                        typeMapping == masterTypeMapping,
                                        masterTypeMapping,
                                        previousObjectsHashMap,
                                        columnIndexMap,
                                        context,
                                        resultSet
                                )
                        );
                        nodeObjectList.add(typeMappingAndObject);
                        if (!child.getValue().isComplex())
                            hash = Objects.hash(hash, typeMappingAndObject.object);
                    } else
                        linkedChildToParentObject(
                                unitObject,
                                typeMapping,
                                child.getKey(),
                                buildUnitObject(
                                        child.getValue(),
                                        typeMapping == masterTypeMapping,
                                        masterTypeMapping,
                                        previousObjectsHashMap,
                                        columnIndexMap,
                                        context,
                                        resultSet
                                )
                        );
                }
                return unitObject;
            } else {
                /**
                 * 读取当前类型映射对应列的数据并放入栈中
                 */
                /**
                 * 读取columnIndex缓存，若无则获取当前column的index并放入缓存
                 */
                int columnIndex = getColumnIndex(typeMapping, columnIndexMap, resultSet);
                return typeMapping
                        .getTypeHandler()
                        .getValue(resultSet, columnIndex);
            }
        } catch (ReflectionException | NoSuchMethodException e) {
            throw new ConvertException(e);
        }
    }

    private Object simpleConvert(TypeMapping rootTypeMapping, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws ConvertException, SQLException {
        Object previousRootObject = null;
        while (resultSet.next()) {
            if (previousRootObject != null) {
                for (Map.Entry<String, TypeMapping> child : rootTypeMapping.getChildren().entrySet()) {
                    linkedChildToParentObject(previousRootObject, rootTypeMapping, child.getKey(), buildUnitObject(child.getValue(), columnIndexMap, context, resultSet));
                }
            } else
                previousRootObject = buildUnitObject(rootTypeMapping, columnIndexMap, context, resultSet);
        }
        return previousRootObject;
    }

    /**
     * 链接子对象到父对象
     *
     * @param parentObject
     * @param parentMapping
     * @param key
     * @param childObject
     */
    public void linkedChildToParentObject(Object parentObject, TypeMapping parentMapping, Object key, Object childObject) {
        parentMapping.getProperty().setValue(parentObject, childObject, key);
    }

    private Object buildUnitObject(TypeMapping typeMapping, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws ConvertException, SQLException {
        try {
            if (typeMapping.isComplex()) {
                Object unitObject = typeMapping
                        .getReflection()
                        .getObjectFactory()
                        .produce();
                for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()) {
                    linkedChildToParentObject(unitObject, typeMapping, child.getKey(), buildUnitObject(child.getValue(), columnIndexMap, context, resultSet));
                }
                return unitObject;
            } else {
                /**
                 * 读取当前类型映射对应列的数据并放入栈中
                 */
                /**
                 * 读取columnIndex缓存，若无则获取当前column的index并放入缓存
                 */
                int columnIndex = getColumnIndex(typeMapping, columnIndexMap, resultSet);
                return typeMapping
                        .getTypeHandler()
                        .getValue(resultSet, columnIndex);
            }
        } catch (ReflectionException | NoSuchMethodException e) {
            throw new ConvertException(e);
        }
    }

    private int getColumnIndex(TypeMapping typeMapping, Map<String, Integer> columnIndexMap, ResultSet resultSet) throws SQLException {
        String columnName = typeMapping.getColumnName();
        int columnIndex;
        if (!columnIndexMap.containsKey(columnName))
            columnIndexMap.put(columnName, columnIndex = resultSet.findColumn(columnName));
        else
            columnIndex = columnIndexMap.get(columnName);
        return columnIndex;
    }

    static class TypeMappingAndObject {
        TypeMapping typeMapping;
        Object object;

        public TypeMappingAndObject(TypeMapping typeMapping, Object object) {
            this.typeMapping = typeMapping;
            this.object = object;
        }
    }
}
