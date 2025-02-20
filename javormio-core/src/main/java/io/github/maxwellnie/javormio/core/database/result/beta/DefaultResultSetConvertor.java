package io.github.maxwellnie.javormio.core.database.result.beta;

import io.github.maxwellnie.javormio.core.OperationContext;
import io.github.maxwellnie.javormio.core.database.result.ConvertException;
import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.java.reflect.ReflectionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Maxwell Nie
 */
public class DefaultResultSetConvertor implements ResultSetConvertor {
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
        /**
         * 工作栈
         */
        Stack<TypeMappingAndObject> nodeObjectStack = new Stack<>();
        /**
         * 缓存对象栈，用于缓存对象栈中已经存在的前置对象，避免重复创建对象
         */
        Map<TypeMappingTree.TypeMappingNode, TypeMappingAndObject> typeMappingAndObjectMap = new LinkedHashMap<>();
        /**
         * 当前处理的节点
         */
        TypeMappingTree.TypeMappingNode currentTypeMappingNode = headTypeMappingNode;
        /**
         * 暂存栈，用于暂存未处理对象，在链接相关子对象到父对象后，将此栈中元素写入对象栈中
         */
        Stack<TypeMappingAndObject> tempStack = new Stack<>();
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
                    linkedChildrenToParentObject(tempStack, nodeObjectStack, o, currentTypeMappingNode);
                } else {
                    /**
                     * 读取当前类型映射对应列的数据并放入栈中
                     */
                    /**
                     * 读取columnIndex缓存，若无则获取当前column的index并放入缓存
                     */
                    String columnName = typeMapping.getColumnName();
                    int columnIndex;
                    if (!columnIndexMap.containsKey(columnName))
                        columnIndexMap.put(columnName, columnIndex = resultSet.findColumn(columnName));
                    else
                        columnIndex = columnIndexMap.get(columnName);
                    o = typeMapping
                            .getTypeHandler()
                            .getValue(resultSet, columnIndex);
                }
                TypeMappingAndObject typeMappingAndObject = typeMappingAndObjectMap.computeIfAbsent(currentTypeMappingNode, (n)->new TypeMappingAndObject(n, o));
                typeMappingAndObject.object = o;
                nodeObjectStack.push(typeMappingAndObject);
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
            linkedChildrenToParentObject(tempStack, nodeObjectStack, previousRootObject, tailTypeMappingNode);
        }
        return previousRootObject;
    }

    /**
     * 链接多个子对象到父对象
     * @param tempStack
     * @param nodeObjectStack
     * @param o
     * @param parentTypeMappingNode
     */
    private void linkedChildrenToParentObject(Stack<TypeMappingAndObject> tempStack, Stack<TypeMappingAndObject> nodeObjectStack, Object o, TypeMappingTree.TypeMappingNode parentTypeMappingNode) {
        TypeMappingAndObject previousNode;
        if (!nodeObjectStack.isEmpty())
            previousNode = nodeObjectStack.peek();
        else
            return;
        /**
         * 链接子对象到父对象
         */
        while (previousNode != null){
            if (previousNode.typeMappingNode.parent == parentTypeMappingNode){
                linkedChildToParentObject(o, parentTypeMappingNode.typeMapping, previousNode.typeMappingNode.key, previousNode.object);
                previousNode = nodeObjectStack.pop();
            }else {
                previousNode = nodeObjectStack.pop();
                tempStack.push(previousNode);
            }
        }
        while ((previousNode = tempStack.pop()) != null)
            nodeObjectStack.push(previousNode);
    }

    static class TypeMappingAndObject{
        TypeMappingTree.TypeMappingNode typeMappingNode;
        Object object;

        public TypeMappingAndObject(TypeMappingTree.TypeMappingNode typeMappingNode, Object object) {
            this.typeMappingNode = typeMappingNode;
            this.object = object;
        }
    }
}
