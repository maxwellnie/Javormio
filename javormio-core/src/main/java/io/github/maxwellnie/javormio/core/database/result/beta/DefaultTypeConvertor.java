package io.github.maxwellnie.javormio.core.database.result.beta;

import io.github.maxwellnie.javormio.core.OperationContext;
import io.github.maxwellnie.javormio.core.database.result.ConvertException;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.ReflectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Stack;

/**
 * @author Maxwell Nie
 */
public class DefaultTypeConvertor implements TypeConvertor {
    @Override
    public Object convert(TypeMappingTree typeMappingTree, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws ConvertException, SQLException {
        if (resultSet == null || typeMappingTree == null)
            throw new ConvertException("resultSet or typeMapping is null");
        if (typeMappingTree.complexQuery)
            return null;
        return simpleConvert(typeMappingTree.head, typeMappingTree.tail, columnIndexMap, context, resultSet);
    }
    /**
     * 链接子对象到父对象
     * @param parentObject
     * @param parentMapping
     * @param key
     * @param childObject
     */
    public void linkedChildToParentObject(Object parentObject, TypeMapping parentMapping, Object key, Object childObject) {
        parentMapping.getProperty().setValue(parentObject, childObject, key);
    }

    /**
     * 非一对多结果集转换
     * @param headTypeMappingNode
     * @param tailTypeMappingNode
     * @param columnIndexMap
     * @param context
     * @param resultSet
     * @return Object
     * @throws ConvertException
     * @throws SQLException
     */
    public Object simpleConvert(TypeMappingTree.TypeMappingNode headTypeMappingNode, TypeMappingTree.TypeMappingNode tailTypeMappingNode, Map<String, Integer> columnIndexMap, OperationContext context, ResultSet resultSet) throws ConvertException, SQLException {
        Stack<TypeMappingAndObject> nodeObjectStack = new Stack<>();
        TypeMappingTree.TypeMappingNode currentTypeMappingNode = headTypeMappingNode;
        /**
         * 前置根类型映射节点对象
         */
        Object previousRootObject = null;
        while (resultSet.next()) {
            /**
             * 处理除根节点外的所有子节点
             */
            while (currentTypeMappingNode.next != null) {
                /**
                 * 当前类型映射节点
                 */
                TypeMapping typeMapping = currentTypeMappingNode.typeMapping;
                /**
                 * 依据当前类型映射节点构建的对象
                 */
                Object o;
                /**
                 * 当前类型映射节点类型的反射对象
                 */
                Reflection<?> reflection = typeMapping.getReflection();
                /**
                 * 判断当前节点是否需要将栈中的对象链接到当前节点的对象，若需要则链接，反之从结果集读取对象后压入对象栈中
                 */
                if (currentTypeMappingNode.isNeedToMerge) {
                    try {
                        o = reflection.getObjectFactory().produce();
                    } catch (ReflectionException | NoSuchMethodException e) {
                        throw new ConvertException(e);
                    }
                    linkedChildrenToParentObject(nodeObjectStack, o, typeMapping);
                } else {
                    /**
                     * 读取当前类型映射对应列的数据并放入栈中
                     */
                    /**
                     * 读取columnIndex缓存，若无则获取当前column的index并放入缓存
                     */
                    String columnName = typeMapping.getColumnName();
                    int columnIndex = columnIndexMap.get(columnName);
                    if (columnIndex == 0)
                        columnIndexMap.put(columnName, columnIndex = resultSet.findColumn(columnName));
                    o = typeMapping
                            .getTypeHandler()
                            .getValue(resultSet, columnIndex);
                }
                nodeObjectStack.push(new TypeMappingAndObject(currentTypeMappingNode, o));
                currentTypeMappingNode = currentTypeMappingNode.next;
            }
            /**
             * 处理根节点
             */
            if (previousRootObject == null)
                try {
                    previousRootObject = tailTypeMappingNode.typeMapping.getReflection().getObjectFactory().produce();
                } catch (ReflectionException | NoSuchMethodException e) {
                    throw new ConvertException(e);
                }
           linkedChildrenToParentObject(nodeObjectStack, previousRootObject, tailTypeMappingNode.typeMapping);
        }
        return previousRootObject;
    }

    /**
     * 链接多个子对象到父对象
     * @param nodeObjectStack
     * @param o
     * @param parentTypeMapping
     */
    private void linkedChildrenToParentObject(Stack<TypeMappingAndObject> nodeObjectStack, Object o, TypeMapping parentTypeMapping) {
        TypeMappingAndObject previousNode = null;
        /**
         * 链接子对象到父对象
         */
        while ((previousNode = nodeObjectStack.pop()) != null)
            linkedChildToParentObject(o, parentTypeMapping, previousNode.typeMappingNode.key, previousNode.object);
    }

    class TypeMappingAndObject{
        TypeMappingTree.TypeMappingNode typeMappingNode;
        Object object;

        public TypeMappingAndObject(TypeMappingTree.TypeMappingNode typeMappingNode, Object object) {
            this.typeMappingNode = typeMappingNode;
            this.object = object;
        }
    }
}
