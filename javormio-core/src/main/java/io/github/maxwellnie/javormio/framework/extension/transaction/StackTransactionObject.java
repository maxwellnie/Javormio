package io.github.maxwellnie.javormio.framework.extension.transaction;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 事务对象（栈实现）
 *
 * @author Maxwell Nie
 */
public class StackTransactionObject implements TransactionObject {
    /**
     * 原子事务栈
     */
    Stack<AtomicTransaction> atomicTransactions = new Stack<>();
    /**
     * 过期时间
     */
    private long expireTime;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 属性
     */
    private Map<String, Object> properties = new LinkedHashMap<>();

    public StackTransactionObject(long expireTime, long createTime) {
        this.expireTime = expireTime;
        this.createTime = createTime;
    }

    public StackTransactionObject() {
    }

    public void appendAtomicTransaction(AtomicTransaction atomicTransaction) {
        this.atomicTransactions.push(atomicTransaction);
    }

    public Collection<AtomicTransaction> getAtomicTransactions() {
        return atomicTransactions;
    }

    public AtomicTransaction peekAtomicTransaction() {
        return atomicTransactions.peek();
    }

    public void removeAtomicTransaction(AtomicTransaction atomicTransaction) {
        atomicTransactions.remove(atomicTransaction);
    }

    public AtomicTransaction popAtomicTransaction() {
        return atomicTransactions.pop();
    }

    @Override
    public long expireTime() {
        return expireTime;
    }

    @Override
    public long createTime() {
        return createTime;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void clearAtomicTransactions() {
        atomicTransactions.clear();
    }

    @Override
    public void clear() {
        clearAtomicTransactions();
        properties.clear();
    }

}
