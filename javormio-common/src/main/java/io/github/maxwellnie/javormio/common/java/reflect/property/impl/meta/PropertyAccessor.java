package io.github.maxwellnie.javormio.common.java.reflect.property.impl.meta;

import io.github.maxwellnie.javormio.common.java.reflect.property.PropertyException;

/**
 * 属性访问器，可以获取或设置bean的属性值
 *
 * @author Maxwell Nie
 */
public class PropertyAccessor {
    private final MetaProperty metaProperty;
    private final Object bean;
    private final Object param;

    public PropertyAccessor(MetaProperty metaProperty, Object bean, Object param) {
        this.metaProperty = metaProperty;
        this.bean = bean;
        this.param = param;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public Object getBean() {
        return bean;
    }

    public Object getParam() {
        return param;
    }

    public PropertyAccessor getChild(String key, Object param) {
        if (metaProperty == null)
            return null;
        MetaProperty result = metaProperty.getChild(key);
        return new PropertyAccessor(result, getPropertyValue(bean, param), param);
    }

    public Object getPropertyValue(Object bean, Object param) {
        if (metaProperty != null && metaProperty.getProperty() != null && bean != null)
            return metaProperty.getProperty().getValue(bean, param);
        else
            return null;
    }

    public Result findByWay(Node node) throws PropertyException {
        return findByWay(node, null);
    }

    public Result findByWay(Node node, Result result) throws PropertyException {
        while (node != null) {
            Object bean = this.bean;
            MetaProperty metaProperty = this.metaProperty;

            if (result != null) {
                bean = result.value;
                metaProperty = result.currentMetaProperty;
            } else {
                result = new Result(node, metaProperty, null, bean);
            }

            if (bean == null) {
                throw new PropertyException("bean is null,But LinedWay is not be end." + "Node Line Way:\n" + new NodePrinter(node));
            }

            if (metaProperty == null || !metaProperty.hasChildren()) {
                return result;
            }

            if (metaProperty.getProperty() == null) {
                throw new PropertyException("The line way is not complete!Because Node[" + node.key + "] haven't Property.Node Line Way:\n" + new NodePrinter(node));
            }

            if (node.key == null) {
                return result;
            }

            MetaProperty child = metaProperty.getChild(node.getKey());

            if (child == null) {
                throw new PropertyException("Can't find property by key: " + node.getKey() + " in " + bean + ",Node Line Way:\n" + new NodePrinter(node));
            }

            result = result.modify(node, child, metaProperty, metaProperty.getProperty().getValue(bean, node.param));
            node = node.next;
        }
        return result;
    }

    /**
     * 寻找结果
     */
    public static class Result {
        Node node;
        MetaProperty currentMetaProperty;
        MetaProperty parentProperty;
        Object value;

        public Result(Node node, MetaProperty currentMetaProperty, MetaProperty parentProperty, Object value) {
            this.node = node;
            this.currentMetaProperty = currentMetaProperty;
            this.parentProperty = parentProperty;
            this.value = value;
        }

        Result modify(Node node, MetaProperty currentMetaProperty, MetaProperty parentProperty, Object value) {
            this.node = node;
            this.currentMetaProperty = currentMetaProperty;
            this.parentProperty = parentProperty;
            this.value = value;
            return this;
        }
    }

    /**
     * 链表节点
     */
    public static class Node {
        Node previous;
        Node next;
        String key;
        Object param;

        public Node(Node previous, Node next, String key, Object param) {
            this.previous = previous;
            this.next = next;
            this.key = key;
            this.param = param;
        }

        public Node() {
        }

        public Node getPrevious() {
            return previous;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Object getParam() {
            return param;
        }

        public void setParam(Object param) {
            this.param = param;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    /**
     * 链式节点打印器
     */
    static class NodePrinter {
        Node node;

        NodePrinter(Node node) {
            this.node = node;
        }

        /**
         * 打印节点链
         *
         * @return String
         */
        String print(int max) {
            Node currNode = node;
            while (node.previous != null)
                node = node.previous;
            boolean overflow = false;
            StringBuilder sb = new StringBuilder("=>[\n");
            while (node != null && node != currNode) {
                if (sb.length() > max)
                    overflow = true;
                else {
                    sb.append("key=")
                            .append(node.key)
                            .append(",")
                            .append("param=")
                            .append(node.param)
                            .append("\n");
                    ;
                }
                node = node.next;
            }
            if (node != currNode)
                return "Node[" + currNode.key + "] is not found.";
            if (overflow) {
                sb.append("...\n")
                        .append("key=")
                        .append(currNode.key)
                        .append(",")
                        .append("param=")
                        .append(currNode.param)
                        .append("\n");
            }
            return sb.append("]").toString();
        }

        String print() {
            return print(1500);
        }

        @Override
        public String toString() {
            return print();
        }
    }
}
