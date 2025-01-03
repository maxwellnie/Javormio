package io.github.maxwellnie.javormio.core.database.transaction.conroller;

import io.github.maxwellnie.javormio.core.cache.transactional.CacheTransactional;
import io.github.maxwellnie.javormio.core.database.jdbc.datasource.DynamicMultipleDataSource;
import io.github.maxwellnie.javormio.core.database.transaction.TransactionObject;
import io.github.maxwellnie.javormio.core.database.transaction.TransactionObject.AtomicTransaction;
import io.github.maxwellnie.javormio.core.utils.SystemClock;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 将原子事务合并提交或是回滚，并不在意其中部分事务是否操作成功
 *
 * @author Maxwell Nie
 */
public class MergeTransactionController extends AbstractTransactionController {
    private final SystemClock systemClock;
    boolean isClosed = false;

    public MergeTransactionController(DynamicMultipleDataSource dataSource, SystemClock systemClock) {
        super(dataSource.getTransactionObject());
        this.systemClock = systemClock;
    }

    public MergeTransactionController(TransactionObject transaction, SystemClock systemClock) {
        super(transaction);
        this.systemClock = systemClock;
    }

    @Override
    public void commit() throws SQLException {
        CacheTransactional transactional = (CacheTransactional) transaction.getProperties().get("txCache");
        try {
            check();
            boolean ableToFlush = transactional != null && transactional.isUpdated();
            if (ableToFlush)
                transactional.clear();
            Collection<AtomicTransaction> atomicTransactions = transaction.getAtomicTransactions();
            Iterator<AtomicTransaction> atomicTransactionIterator = atomicTransactions.iterator();
            while (atomicTransactionIterator.hasNext()) {
                atomicTransactionIterator.next().getConnection().commit();
            }
            if (ableToFlush)
                transactional.commit();
        } catch (Throwable e) {
            if (transactional != null)
                transactional.rollback();
        } finally {
            transaction.clear();
        }
    }

    @Override
    public void rollback() throws SQLException {
        try {
            check();
            Collection<AtomicTransaction> atomicTransactions = transaction.getAtomicTransactions();
            Iterator<AtomicTransaction> atomicTransactionIterator = atomicTransactions.iterator();
            while (atomicTransactionIterator.hasNext()) {
                atomicTransactionIterator.next().getConnection().rollback();
            }
        } finally {
            CacheTransactional transactional = (CacheTransactional) transaction.getProperties().get("txCache");
            if (transactional != null)
                transactional.rollback();
            transaction.clear();
        }

    }

    @Override
    public void close(boolean waive) throws SQLException {
        if (isClosed)
            return;
        isClosed = true;
        Collection<AtomicTransaction> atomicTransactions = transaction.getAtomicTransactions();
        if (waive) {
            if (atomicTransactions.isEmpty())
                return;
            Iterator<AtomicTransaction> atomicTransactionIterator = atomicTransactions.iterator();
            while (atomicTransactionIterator.hasNext()) {
                atomicTransactionIterator.next().getConnection().rollback();
            }
        } else {
            if (!atomicTransactions.isEmpty())
                throw new SQLException("The next transaction will be rejected but one or more transactions have not complete.");
        }
    }

    private void check() throws SQLException {
        if (isClosed())
            throw new SQLException("Transaction is closed");
        long expectedTime = transaction.createTime() + transaction.expireTime() * 1000;
        if (expectedTime < systemClock.now())
            throw new SQLException("Transaction is expired");
    }

    @Override
    public boolean isClosed() throws SQLException {
        return isClosed;
    }
}
