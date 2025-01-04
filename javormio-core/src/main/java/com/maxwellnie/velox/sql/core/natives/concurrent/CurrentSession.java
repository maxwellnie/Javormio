package com.maxwellnie.velox.sql.core.natives.concurrent;

import com.maxwellnie.velox.sql.core.natives.database.session.Session;
import com.maxwellnie.velox.sql.core.natives.database.session.SessionFactory;
import com.maxwellnie.velox.sql.core.natives.database.session.impl.SessionProxy;
import com.maxwellnie.velox.sql.core.natives.exception.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Stack;

/**
 * 当前线程所持有的Session
 * <p>修改底层为栈结构，以适用于多层嵌套的事务</p>
 *
 * @author Maxwell Nie
 */
public class CurrentSession {
    private static final Logger logger = LoggerFactory.getLogger(CurrentSession.class);
    private static final ThreadLocal<Stack<SessionProxy>> SessionThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> enabledProxySpringTransaction = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> suspendState = new ThreadLocal<>();

    /**
     * 提交顶层事务
     */
    public static void commitTop() {
        if (isOpenProxyTransaction())
            Optional.ofNullable(getTopSession()).ifPresent((o) -> o.getTarget().commit());
    }

    /**
     * 释放顶层Session
     */
    public static void closeTop() {
        if (isOpenProxyTransaction()) {
            if (SessionThreadLocal.get().pop() == null)
                throw new SessionException("Current session is null");
            if (SessionThreadLocal.get().isEmpty())
                closeProxyTransaction();
        }
    }

    /**
     * 关闭全部Session
     *
     * @param consumer
     */
    public static void close(SessionProxyConsumer consumer) {
        if (isOpenProxyTransaction()) {
            Stack<SessionProxy> stack = getCurrentSessionStack();
            while (!stack.isEmpty()) {
                SessionProxy sessionProxy = stack.pop();
                SessionFactory sessionFactory = sessionProxy.getSessionFactory();
                if (sessionProxy != null && sessionProxy.getTarget() != null && sessionFactory != null)
                    consumer.accept(sessionProxy, sessionFactory);
                else
                    logger.error("Session or sessionFactory is null");
            }
            closeProxyTransaction();
        }
    }

    /**
     * 关闭事务代理
     */
    public static void closeProxyTransaction() {
        if (isOpenProxyTransaction()) {
            SessionThreadLocal.remove();
            enabledProxySpringTransaction.remove();
        }
    }

    /**
     * 回滚顶层事务
     */
    public static void rollbackTop() {
        if (isOpenProxyTransaction())
            Optional.ofNullable(getTopSession()).ifPresent((o) -> o.getTarget().rollback());
    }

    /**
     * 开启事务代理
     */
    public static void openProxyTransaction() {
        enabledProxySpringTransaction.set(Boolean.TRUE);
    }

    /**
     * 是否开启事务代理
     *
     * @return boolean
     */
    public static boolean isOpenProxyTransaction() {
        return enabledProxySpringTransaction.get() != null && enabledProxySpringTransaction.get();
    }

    public static boolean isNotTransactional() {
        return !isOpenProxyTransaction() || isSuspend();
    }

    /**
     * 代理事务
     *
     * @param session
     * @param sessionFactory
     * @return SessionProxy
     */
    public static SessionProxy proxyCurrentSpringTransaction(Session session, SessionFactory sessionFactory) {
        if (isOpenProxyTransaction()) {
            SessionProxy proxySession = new SessionProxy(session, sessionFactory);
            Stack<SessionProxy> stack = getCurrentSessionStack();
            stack.push(proxySession);
            logger.debug(stack.toString());
            return proxySession;
        }
        return null;
    }

    /**
     * 获取当前线程的Session栈
     *
     * @return Stack<SessionProxy>
     */
    static Stack<SessionProxy> getCurrentSessionStack() {
        if (isOpenProxyTransaction()) {
            Stack<SessionProxy> stack = SessionThreadLocal.get();
            if (stack != null)
                return stack;
            stack = new Stack<>();
            SessionThreadLocal.set(stack);
            return stack;
        }
        return null;
    }

    /**
     * 获取当前线程的Session
     *
     * @return SessionProxy
     */
    public static SessionProxy getTopSession() {
        if (isOpenProxyTransaction() && SessionThreadLocal.get() != null) {
            return SessionThreadLocal.get().peek();
        }
        return null;
    }

    public static void suspend() {
        suspendState.set(true);
    }

    public static boolean isSuspend() {
        return suspendState.get() != null && suspendState.get();
    }

    public static void resume() {
        if (suspendState.get() != null && enabledProxySpringTransaction.get())
            suspendState.set(Boolean.FALSE);
    }

    /**
     * Session代理对象消费者
     */
    @FunctionalInterface
    public static interface SessionProxyConsumer {
        void accept(SessionProxy sessionProxy, SessionFactory sessionFactory);
    }
}
