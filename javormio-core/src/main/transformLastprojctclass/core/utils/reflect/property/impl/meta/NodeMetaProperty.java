package com.maxwellnie.velox.sql.core.utils.reflect.property.impl.meta;

import com.maxwellnie.velox.sql.core.natives.database.mapping.TypeMapping;
import com.maxwellnie.velox.sql.core.natives.stream.Node;

/**
 * 值读取器的包装器
 * @author Maxwell Nie
 */
public class NodeMetaProperty <T extends TypeMapping<T>,B> extends MetaProperty<T> {
    private Node<B> node;
    public NodeMetaProperty(T typeMapping, Object bean, Object param, Node<B> node) {
        super(typeMapping, bean, param);
        this.node = node;
    }

    public Node<B> getNode() {
        return node;
    }
}
