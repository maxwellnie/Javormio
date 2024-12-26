package io.github.maxwellnie.javormio.core.database.transaction.conroller;

import io.github.maxwellnie.javormio.core.database.transaction.TransactionObject;

/**
 * @author Maxwell Nie
 */
public abstract class AbstractTransactionController implements TransactionController {
    protected TransactionObject transaction;

    public AbstractTransactionController(TransactionObject transaction) {
        this.transaction = transaction;
    }
}
