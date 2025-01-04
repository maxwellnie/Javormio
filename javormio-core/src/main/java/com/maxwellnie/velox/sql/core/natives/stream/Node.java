package com.maxwellnie.velox.sql.core.natives.stream;

/**
 * @author Maxwell Nie
 */
public class Node<T> {
    private Node<T> next;
    private Node<T> previous;
    private T value;

    public Node() {
    }

    public Node(Node<T> next, Node<T> previous, T value) {
        this.next = next;
        this.previous = previous;
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "next=" + next +
                ", previous=" + previous +
                ", value=" + value +
                '}';
    }
}
