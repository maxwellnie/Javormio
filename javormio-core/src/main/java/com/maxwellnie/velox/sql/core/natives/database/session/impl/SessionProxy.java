package com.maxwellnie.velox.sql.core.natives.database.session.impl;

import com.maxwellnie.velox.sql.core.cache.transactional.CacheTransactional;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.session.SessionFactory;
import com.maxwellnie.velox.sql.core.natives.database.transaction.Transaction;
import com.maxwellnie.velox.sql.core.natives.task.TaskQueue;


/**
 * Session代理，阻止了提交和回滚
 *
 * @author Maxwell Nie
 */
public class SessionProxy implements Session {
    private final Session session;
    private final SessionFactory sessionFactory;

    public SessionProxy(Session session, SessionFactory sessionFactory) {
        this.session = session;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean getAutoCommit() {
        return session.getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean flag) {
        session.setAutoCommit(flag);
    }

    @Override
    public void close() {
        session.close();
    }

    @Override
    public void close(boolean commit) {
        session.close(commit);
    }

    @Override
    public void commit() {
        // forbid commit
    }

    @Override
    public void rollback() {
        // forbid rollback
    }

    @Override
    public Transaction getTransaction() {
        return session.getTransaction();
    }

    @Override
    public CacheTransactional getDirtyManager() {
        return session.getDirtyManager();
    }

    @Override
    public boolean isClosed() {
        return session.isClosed();
    }

    @Override
    public TaskQueue getTaskQueue() {
        return session.getTaskQueue();
    }

    public Session getTarget() {
        return session;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
