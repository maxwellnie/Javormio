package io.github.maxwellnie.javormio.common.java.jdbc.transaction;

/**
 * Transaction object, used to manage the transaction of the current area
 *
 * @author Maxwell Nie
 */
public interface TransactionObject {
    /**
     * Create a new transaction object in current area
     * <p>The "area" can be a thread, a request, a session, a transaction, etc.<p/>
     * @throws IllegalArgumentException If an error occurs when creating the transaction object
     * @throws InternalTransactionTimeoutException If the transaction object cannot be created within the specified timeout
     */
    void begin() throws InternalTransactionTimeoutException;

    /**
     * Commit the transaction
     * @throws InternalTransactionTimeoutException If the transaction cannot be committed within the specified timeout
     * @throws InternalTransactionCommitException If an error occurs when committing the transaction
     */
    void commit() throws InternalTransactionTimeoutException,  InternalTransactionCommitException;
    /**
     * Rollback the transaction
     * @throws InternalTransactionTimeoutException If the transaction cannot be rolled back within the specified timeout
     * @throws InternalTransactionRollbackException If an error occurs when rolling back the transaction
     */
    void rollback() throws InternalTransactionTimeoutException, InternalTransactionRollbackException;
    /**
     * Set the transaction to rollback only
     * @throws InternalTransactionTimeoutException If the transaction cannot be set to rollback only within the specified timeout
     */
    void setRollbackOnly() throws InternalTransactionTimeoutException;
    /**
     * Set the timeout for the transaction
     * @param seconds The timeout in seconds
     * @throws IllegalArgumentException If the timeout is less than 0
     *
     */
    void setTimeout(int seconds);
}
