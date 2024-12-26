package io.github.maxwellnie.javormio.core.java.reflect.property;

import java.util.function.Function;

/**
 * 值读取器，这是一个函数型的读取器，可以读取任何类型的值，包括集合，数组等
 *
 * @author Maxwell Nie
 */
public class ValueReader {
    /**
     * 读取器，根据配置读取值
     */
    private Function<Configuration, Value> reader;
    /**
     * 属性对象
     */
    private Property property;
    /**
     * 读取属性值的必要参数
     */
    private Object param;
    /**
     * 键值，用于在TypeMapping中定位
     */
    private Object key;

    public ValueReader(Function<Configuration, Value> reader, Property property, Object param, Object key) {
        this.reader = reader;
        this.property = property;
        this.param = param;
        this.key = key;
    }

    /**
     * 读取值
     *
     * @param bean
     * @return Value
     */
    public Value read(Object bean) {
        return reader.apply(new Configuration(property, param, bean));
    }

    public Function<Configuration, Value> getReader() {
        return reader;
    }

    public Property getProperty() {
        return property;
    }

    public Object getParam() {
        return param;
    }

    public Object getKey() {
        return key;
    }

    /**
     * 配置类，用于配置读取器
     */
    public static class Configuration {
        /**
         * 属性对象
         */
        private Property property;
        /**
         * 读取属性值的必要参数
         */
        private Object param;
        /**
         * bean对象
         */
        private Object bean;

        public Configuration(Property property, Object param, Object bean) {
            this.property = property;
            this.param = param;
            this.bean = bean;
        }

        public Property getProperty() {
            return property;
        }

        public void setProperty(Property property) {
            this.property = property;
        }

        public Object getParam() {
            return param;
        }

        public void setParam(Object param) {
            this.param = param;
        }

        public Object getBean() {
            return bean;
        }

        public void setBean(Object bean) {
            this.bean = bean;
        }

        public Object autoRead() throws ValueReaderException {
            if (property == null)
                throw new ValueReaderException("property is null");
            if (bean == null)
                throw new ValueReaderException("bean is null");
            return property.getValue(bean, param);
        }
    }

    /**
     * 值对象
     */
    public static class Value {
        /**
         * 空值
         */
        public static final Value NULL = new Value(null, null);
        /**
         * 值
         */
        private final Object value;
        private final PropertyType type;

        public Value(Object value, PropertyType type) {
            this.value = value;
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public boolean isCollection() {
            return PropertyType.COLLECTION.equals(type);
        }

        public boolean isArray() {
            return PropertyType.ARRAY.equals(type);
        }
    }

}
