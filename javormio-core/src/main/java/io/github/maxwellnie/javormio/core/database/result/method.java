package io.github.maxwellnie.javormio.core.database.result;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Maxwell Nie
 * @author yurongqi
 */
public class method<T> {
    private final Deque<T> stack;

    public method(){
        this.stack = new ArrayDeque<>();
    }

    /**
     * 将元素压入栈顶
     *
     * @param element 要压入栈顶的元素
     */
    public void push(T element) {
        stack.push(element);
    }

    /**
     * 移除并返回栈顶元素
     *
     * @return 栈顶元素，如果栈为空则返回 null
     */
    public T pop() {
        return stack.pop();
    }

    /**
     * 返回栈顶元素但不移除
     *
     * @return 栈顶元素，如果栈为空则返回 null
     */
    public T peek() {
        return stack.peek();
    }

    /**
     * 检查栈是否为空
     *
     * @return 如果栈为空则返回 true，否则返回 false
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * 返回栈中的元素数量
     *
     * @return 栈中的元素数量
     */
    public int size() {
        return stack.size();
    }

}
