package com.maxwellnie.velox.sql.core.utils.reflect.property.impl.meta;

import com.maxwellnie.velox.sql.core.natives.database.mapping.TypeMapping;
import com.maxwellnie.velox.sql.core.natives.stream.Node;
import com.maxwellnie.velox.sql.core.utils.reflect.property.ValueReader;
import org.slf4j.LoggerFactory;

/**
 * 此工具仅用于寻找TypeMapping中的特定的节点，并返回一个NodeMetaProperty对象
 * @param <T>
 * @author Maxwell Nie
 */
public class MetaProperty<T extends TypeMapping<T>> {
    T typeMapping;
    Object bean;
    Object key;
    public MetaProperty(T typeMapping, Object bean, Object key) {
        this.typeMapping = typeMapping;
        this.bean = bean;
        this.key = key;
    }

    /**
     * 根据路径链，找到对应的节点所对应属性的值读取器的包装器
     * @param wayLinkedNode
     * @return NodeMetaProperty
     * @param <C>
     */
    public <C extends Node<ValueReader>> Result<T, ValueReader> searchByWay(C wayLinkedNode){
        if(typeMapping.getInnerTypeMappings() == null)
            return result(typeMapping, typeMapping, bean, key, wayLinkedNode);
        T child = typeMapping.getChild(wayLinkedNode.getValue().getParam());
        Node<ValueReader> next = wayLinkedNode.getNext();
        if(next != null && child != null){
            NodeMetaProperty<T, ValueReader> nodeMetaProperty = new NodeMetaProperty<>(child, typeMapping.getProperty(bean, wayLinkedNode.getValue().getParam()), next.getValue().getKey(), next);
            return nodeMetaProperty.searchByWay(next);
        }
        return result(typeMapping, typeMapping, bean, key, wayLinkedNode);
    }
    private<R> Result<T,ValueReader> result(T typeMapping, T basicTypeMapping,Object bean, Object key, Node<ValueReader> node) {
        Result<T,ValueReader> result = new Result<>(typeMapping, basicTypeMapping, bean, key, node);
        if(basicTypeMapping == typeMapping){
            if(!typeMapping.isBasicType()) {
                T child = typeMapping.getChild(node.getValue().getKey());
                if (child != null)
                    result.basicTypeMapping = child;
            }
        }else {
            result.basicTypeMapping = basicTypeMapping;
        }
        return result;
    }
    public T getTypeMapping() {
        return typeMapping;
    }

    public Object getKey() {
        return key;
    }

    public Object getBean() {
        return bean;
    }
    public Object setProperty(Object value) {
        return typeMapping.setProperty(bean, key, value);
    }
    public static class Result<T extends TypeMapping<T>, R>{
        T propertyTypeMapping;
        T basicTypeMapping;
        Object bean;
        Object key;
        Node<R> node;
        public Result(T propertyTypeMapping, T basicTypeMapping, Object bean, Object key, Node<R> node) {
            this.propertyTypeMapping = propertyTypeMapping;
            this.basicTypeMapping = basicTypeMapping;
            this.bean = bean;
            this.key = key;
            this.node = node;
        }

        public T getPropertyTypeMapping() {
            return propertyTypeMapping;
        }

        public T getBasicTypeMapping() {
            return basicTypeMapping;
        }

        public Object getBean() {
            return bean;
        }

        public Object getKey() {
            return key;
        }

        public Node<R> getNode() {
            return node;
        }
    }
}
