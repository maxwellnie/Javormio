package io.github.maxwellnie.javormio.framework.core.db.transaction;

import io.github.maxwellnie.javormio.framework.common.cache.CacheKey;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

/**
 * Transaction object
 *
 * @author Maxwell Nie
 */
public interface TransactionObject {
    /**
     * Append an atomic transaction
     *
     * @param atomicTransaction
     */
    void appendAtomicTransaction(AtomicTransaction atomicTransaction);

    /**
     * Get all atomic transactions
     *
     * @return Collection<AtomicTransaction>
     */
    Collection<AtomicTransaction> getAtomicTransactions();

    /**
     * Get the top atomic transaction
     *
     * @return AtomicTransaction
     */
    AtomicTransaction peekAtomicTransaction();

    /**
     * Remove an atomic transaction
     *
     * @param atomicTransaction
     */
    void removeAtomicTransaction(AtomicTransaction atomicTransaction);

    /**
     * Remove the top atomic transaction
     *
     * @return AtomicTransaction
     */
    AtomicTransaction popAtomicTransaction();

    /**
     * Get the expire time
     *
     * @return long
     */
    long expireTime();

    /**
     * Get the create time
     *
     * @return long
     */
    long createTime();

    /**
     * Get the properties
     *
     * @return Map<String, Object>
     */
    Map<String, Object> getProperties();

    /**
     * Clear all atomic transactions
     */
    void clearAtomicTransactions();

    /**
     * Clear all
     */
    void clear();

    class AtomicTransaction {
        Connection connection;
        DataSource dataSource;
        CacheKey hashCacheKey;

        public AtomicTransaction(Connection connection, DataSource dataSource, CacheKey hashCacheKey) {
            this.connection = connection;
            this.dataSource = dataSource;
            this.hashCacheKey = hashCacheKey;
        }

        public Connection getConnection() {
            return connection;
        }

        public DataSource getDataSource() {
            return dataSource;
        }

        public CacheKey getCacheKey() {
            return hashCacheKey;
        }
    }
}
