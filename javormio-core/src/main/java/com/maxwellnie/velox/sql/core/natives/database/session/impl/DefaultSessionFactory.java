package com.maxwellnie.velox.sql.core.natives.database.session.impl;


import com.maxwellnie.velox.sql.core.natives.database.context.Context;
import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.session.SessionFactory;

/**
 * Session工厂，生产出一个Session实例
 *
 * @author Maxwell Nie
 */
public class DefaultSessionFactory implements SessionFactory {
    private final Context context;

    public DefaultSessionFactory(Context context) {
        this.context = context;
    }

    @Override
    public Session produce() {
        return produce(false);
    }

    @Override
    public Session produce(boolean autoCommit) {
        return new DefaultSession(
                context.getTransactionFactory().
                        produce(autoCommit, context.getLevel()), autoCommit, context.getTaskQueue());
    }

    @Override
    public Context getHolderObject() {
        return this.context;
    }
}
