package com.maxwellnie.velox.sql.core.utils.framework;

import com.maxwellnie.velox.sql.core.natives.database.sql.SqlType;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.DeleteMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.InsertMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.QueryMethodExecutor;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.impl.UpdateMethodExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Maxwell Nie
 */
public class MethodExecutorUtils {
    public static MethodExecutor getDefaultMethodExecutor(SqlType sqlType) {
        switch (sqlType) {
            case QUERY:
                return new QueryMethodExecutor();
            case INSERT:
            case BATCH_INSERT:
                return new InsertMethodExecutor();
            case DELETE:
            case BATCH_DELETE:
                return new DeleteMethodExecutor();
            case UPDATE:
                return new UpdateMethodExecutor();
            default:
                throw new RuntimeException("Not supported SqlType =>[" + sqlType + "]");
        }
    }

    /**
     * 获取可以被方法映射管理器识别到的方法的名字getId[class c.c.c.A, class c.c.c.f.B]
     *
     * @param method
     * @return
     */
    public static String getMethodDeclaredName(Method method) {
        return method.getName() + Arrays.toString(method.getParameterTypes());
    }

    /**
     * 获取可以被方法映射管理器识别到的方法的名字getId[class c.c.c.A, class c.c.c.f.B]
     *
     * @param method
     * @param argsClass
     * @return
     */
    public static String getMethodDeclaredName(String method, Class<?>[] argsClass) {
        return method + Arrays.toString(argsClass);
    }
}
