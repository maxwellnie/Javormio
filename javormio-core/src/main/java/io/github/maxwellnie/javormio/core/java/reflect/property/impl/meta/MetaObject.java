package io.github.maxwellnie.javormio.core.java.reflect.property.impl.meta;

import io.github.maxwellnie.javormio.core.java.reflect.property.PropertyException;

/**
 * 元对象
 *
 * @author Maxwell Nie
 */
public class MetaObject {
    PropertyAccessor accessor;

    MetaObject(PropertyAccessor accessor) {
        this.accessor = accessor;
    }

    /**
     * 获取元对象
     *
     * @param property 属性
     * @param bean     对象
     * @return MetaObject
     */
    public static MetaObject get(MetaProperty property, Object bean) {
        return new MetaObject(new PropertyAccessor(property, bean, null));
    }

    /**
     * 根据链路找到属性值
     *
     * @param linkedWayHead 链表头节点
     * @return Object
     */
    public Object get(PropertyAccessor.Node linkedWayHead) throws PropertyException {
        PropertyAccessor.Result result = accessor.findByWay(linkedWayHead);
        if (result == null)
            throw new PropertyException("Can't find the property value by the way.");
        return result.value;
    }

    public void set(PropertyAccessor.Node linkedWayHead, String key, Object param, Object value) throws PropertyException {
        PropertyAccessor.Result result = accessor.findByWay(linkedWayHead);
        if (result == null)
            throw new PropertyException("Can't find the property value by the way.");
        if (!result.currentMetaProperty.hasChildren())
            throw new PropertyException("The property[" + key + "] is not a found in " + this.accessor.getBean());
        MetaProperty metaProperty = result.currentMetaProperty.getMetaProperties().get(key);
        if (metaProperty == null || metaProperty.getProperty() == null)
            throw new PropertyException("The property[" + key + "] is null.");
        if (result.value == null)
            throw new PropertyException("The property[" + key + "]'s value is null.");
        metaProperty.getProperty().setValue(result.value, param, value);
    }
}
