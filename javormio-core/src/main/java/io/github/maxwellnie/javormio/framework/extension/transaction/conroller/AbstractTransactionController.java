package io.github.maxwellnie.javormio.framework.extension.transaction.conroller;

import io.github.maxwellnie.javormio.framework.extension.transaction.TransactionObject;

/**
 * 事务控制器抽象类
 *
 * @author Maxwell Nie
 */
public abstract class AbstractTransactionController implements TransactionController {
    /**
     * 事务对象
     */
    protected TransactionObject transaction;

    /**
     * 构造函数
     *
     * @param transaction 事务对象
     */
    public AbstractTransactionController(TransactionObject transaction) {
        this.transaction = transaction;
    }
}
