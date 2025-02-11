package io.github.maxwellnie.javormio.core.database.result;

import io.github.maxwellnie.javormio.core.java.reflect.ObjectFactory;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.ReflectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 结果集转换器
 *
 * @author Maxwell Nie
 */
public class ResultSetConvertorByNie implements ResultSetConvertor {
    /*方法存疑，重新编写
    private Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) throws ConvertException {
        try {
            Reflection<?> reflection = ReflectionUtils.getReflection(typeMapping.getType());
            ObjectFactory<?> entityObjectFactory = reflection.getObjectFactory();
            Object parent = null;
            MetaProperty metaProperty = null;
            boolean isEntity = typeMapping.isComplex() && typeMapping.isEntity();
            if (!isEntity) {
                parent = entityObjectFactory.produce();
                metaProperty = typeMapping.getMetaProperty();
                typeMapping = typeMapping
                        .getChildren()
                        .values()
                        .iterator()
                        .next();
            }
            int rowIndex = 0;
            if (resultSet.isClosed())
                throw new ConvertException("resultSet is closed.");
            while (resultSet.next()) {
                Object instance = entityObjectFactory.produce();
                int columnIndex = 1;
                for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()) {
                    String fieldName = child.getKey();
                    TypeMapping childTypeMapping = child.getValue();
                    Object columnValue = childTypeMapping
                            .getTypeHandler()
                                .getValue(resultSet, columnIndex);
                    if (columnValue != null)
                        childTypeMapping
                                .getMetaProperty()
                                    .getProperty()
                                        .setValue(instance, fieldName, columnValue);
                    columnIndex++;
                }
                if (parent != null && !isEntity) {
                    switch (metaProperty.getPropertyType()) {
                        case ARRAY:
                        case LIST:
                        case MAP:
                            parent = metaProperty.getProperty().setValue(parent, rowIndex, instance);
                            break;
                        case SET:
                            parent = metaProperty.getProperty().setValue(parent, null, instance);
                            break;
                        default:
                            throw new ConvertException("The type [" + metaProperty.getType() + "] is not support.");
                    }
                } else {
                    return instance;
                }
                rowIndex++;
            }
            return parent;
        } catch (SQLException | NoSuchMethodException | ReflectionException e) {
            throw new ConvertException(e);
        }
    }
    */
    /**
     * 单表查询
     *
     * @param resultSet
     * @param typeMapping
     * @return Object
     */
    private Object simpleConvert(ResultSet resultSet, TypeMapping typeMapping) throws ConvertException {
        LinkedHashMap<String, Integer> columnIndexMap = new LinkedHashMap<>();
        try {
            if (resultSet.isClosed())
                throw new ConvertException("resultSet is closed.");
            return convertObject(null, resultSet, typeMapping, columnIndexMap);
        } catch (SQLException e) {
            throw new ConvertException(e);
        }
    }
    /**
     * 矛盾点：双指针————列索引和行索引，建议隐式移动行列指针
     */
    private Object convertObject(Object parent, ResultSet resultSet, TypeMapping typeMapping, LinkedHashMap<String, Integer> columnIndexMap) throws ConvertException {
        Object object = null;
        try {
            if (!typeMapping.isEntity()){
                if (typeMapping.isComplex()){
                    object = buildOtherObject(resultSet, typeMapping);
                }else {
                    object = typeMapping
                            .getTypeHandler()
                            .getValue(resultSet, columnIndexMap
                                    .get(typeMapping.getColumnName())
                            );
                    return object;
                }
            }
        }catch (SQLException e){

        }
        return object;
    }
    /**
     * 读取一行数据
     *
     * @param resultSet
     * @param typeMapping
     * @return Object
     */
    private Object readRowObject(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }
    public void linkedToParentObject(Object parentObject, TypeMapping parentMapping, Object childObject, TypeMapping childMapping) {

    }
    public Object c_(ResultSet resultSet, TypeMapping typeMapping, Stack<Object> objectStack, Stack<TypeMapping> typeMappingStack, LinkedHashMap<String, Integer> columnIndexMap) throws ConvertException {
        Object object = null;
        try {
            if(typeMapping.isComplex() && !typeMapping.isEntity()){
                object = typeMapping
                        .getReflection()
                        .getObjectFactory()
                        .produce();
                for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()){
                    Object childObject = c_(resultSet, child.getValue(), objectStack, typeMappingStack, columnIndexMap);
                    linkedToParentObject(objectStack.peek(), typeMapping, childObject, child.getValue());
                }
            } else if (typeMapping.isEntity()) {
                object = typeMapping
                        .getReflection()
                        .getObjectFactory()
                        .produce();
                for (Map.Entry<String, TypeMapping> child : typeMapping.getChildren().entrySet()){
                    Object childObject = c_(resultSet, child.getValue(), objectStack, typeMappingStack, columnIndexMap);
                    linkedToParentObject(objectStack.peek(), typeMapping, childObject, child.getValue());
                }
                if (resultSet.next()){

                }
            }else {
                int columnIndex = columnIndexMap.computeIfAbsent(typeMapping.getColumnName(), (key) -> {
                    try {
                        return resultSet.findColumn(key);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                object = typeMapping
                        .getTypeHandler()
                        .getValue(resultSet, columnIndex);
            }
        }catch (NoSuchMethodException | SQLException | ReflectionException e){
            throw new ConvertException(e);
        }
        return object;
    }
    /**
     * 构建实体对象
     *
     * @param resultSet
     * @param typeMapping
     * @return Object
     */
    private Object buildEntityObject(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }

    /**
     * 构建其他对象
     *
     * @param resultSet
     * @param typeMapping
     * @return Object
     */
    private Object buildOtherObject(ResultSet resultSet, TypeMapping typeMapping) {
        return null;
    }
    /**
     * 多表查询
     * <p>算法思想：动态规划</p>
     * <p>首先我们将结果集的列看成是一个N维向量，在读取一行数据时将列数据暂时缓存。
     * 接着，计算主表列HASH值并缓存（向量分量值）直到该行全部计算完毕，然后对上一行主表列数据HASH值作对比（计算向量是否相同，也可以是叉乘）；
     * 若相同则合并子表对象，若不同则创建新的主表实体对象并将子表对象赋值到主表实体对象上。</p>
     *
     * @param resultSet
     * @param typeMapping
     * @return Object
     */
    private Object multipleTableConvert(ResultSet resultSet, TypeMapping typeMapping) {
        Map<TypeMapping, Object> incomplete = new LinkedHashMap<>();
        Stack<TypeMapping> typeMappingStack = new Stack<>();

        return null;
    }

    @Override
    public Object convert(ResultSet resultSet, TypeMapping typeMapping, boolean multipleTable) throws ConvertException {
        if (resultSet == null || typeMapping == null)
            throw new ConvertException("resultSet or typeMapping is null");
        if (multipleTable)
            return multipleTableConvert(resultSet, typeMapping);
        return simpleConvert(resultSet, typeMapping);
    }

    @Override
    public Object convert(List<ResultSet> resultSet, TypeMapping typeMapping, boolean multipleTable) throws ConvertException {
        return null;
    }
}
