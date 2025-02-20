package io.github.maxwellnie.javormio.core.database.result.beta;

import io.github.maxwellnie.javormio.core.database.result.TypeMapping;

/**
 * @author Maxwell Nie
 */
public class TypeMappingTree {
    boolean complexQuery;
    TypeMappingNode head;
    TypeMappingNode tail;

    public TypeMappingTree(boolean complexQuery, TypeMappingNode head, TypeMappingNode tail) {
        this.complexQuery = complexQuery;
        this.head = head;
        this.tail = tail;
    }

    public TypeMappingNode getTail() {
        return tail;
    }

    public void setTail(TypeMappingNode tail) {
        this.tail = tail;
    }

    public boolean isComplexQuery() {
        return complexQuery;
    }

    public void setComplexQuery(boolean complexQuery) {
        this.complexQuery = complexQuery;
    }

    public TypeMappingNode getHead() {
        return head;
    }

    public void setHead(TypeMappingNode head) {
        this.head = head;
    }

    public static class TypeMappingNode {
        TypeMapping typeMapping;
        TypeMappingNode previous;
        TypeMappingNode next;
        boolean isNeedToMerge;
        String key;
        TypeMappingNode parent;

        public TypeMappingNode(TypeMapping typeMapping, TypeMappingNode previous, TypeMappingNode next, boolean isNeedToMerge, String key, TypeMappingNode parent) {

            this.typeMapping = typeMapping;
            this.previous = previous;
            this.next = next;
            this.isNeedToMerge = isNeedToMerge;
            this.key = key;
            this.parent = parent;
        }

        public TypeMappingNode getParent() {
            return parent;
        }

        public void setParent(TypeMappingNode parent) {
            this.parent = parent;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean isNeedToMerge() {
            return isNeedToMerge;
        }

        public void setNeedToMerge(boolean needToMerge) {
            isNeedToMerge = needToMerge;
        }

        public TypeMapping getTypeMapping() {
            return typeMapping;
        }

        public void setTypeMapping(TypeMapping typeMapping) {
            this.typeMapping = typeMapping;
        }

        public TypeMappingNode getPrevious() {
            return previous;
        }

        public void setPrevious(TypeMappingNode previous) {
            this.previous = previous;
        }

        public TypeMappingNode getNext() {
            return next;
        }

        public void setNext(TypeMappingNode next) {
            this.next = next;
        }
    }
}
