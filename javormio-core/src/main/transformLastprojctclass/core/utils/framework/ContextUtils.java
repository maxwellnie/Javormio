package com.maxwellnie.velox.sql.core.utils.framework;

import com.maxwellnie.velox.sql.core.annotation.dao.BasicDaoDeclared;
import com.maxwellnie.velox.sql.core.natives.exception.ClassTypeException;
import com.maxwellnie.velox.sql.core.natives.exception.DaoImplClassException;
import com.maxwellnie.velox.sql.core.natives.exception.RegisterMethodException;
import com.maxwellnie.velox.sql.core.proxy.dao.executor.MethodMapRegister;

/**
 * @author Maxwell Nie
 */
public class ContextUtils {
    /**
     * 注册DaoImpl接口
     *
     * @param clazz
     * @throws ClassTypeException
     * @throws RegisterMethodException
     */
    public static void registerDaoImpl(Class<?> clazz, Object[] args) throws ClassTypeException, RegisterMethodException {
        assert clazz != null : "DaoImplInterface must not be null!";
        if (clazz.isAnnotationPresent(BasicDaoDeclared.class)) {
            BasicDaoDeclared basicDaoDeclared = clazz.getDeclaredAnnotation(BasicDaoDeclared.class);
            if (basicDaoDeclared.value() != null) {
                try {
                    MethodMapRegister methodMapRegister = basicDaoDeclared.value().newInstance();
                    methodMapRegister.registerEntityDefinedDaoImpl(clazz, args);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RegisterMethodException(e);
                }
            } else
                throw new DaoImplClassException("Your supported DaoImplInterface not set [" + MethodMapRegister.class.getName() + "]");
        }
    }

    /**
     * 注册DaoImpl接口
     *
     * @param daoClass
     * @param daoDefinedClass
     * @throws ClassTypeException
     * @throws RegisterMethodException
     */
    public static void registerDaoImpl(Class<?> daoClass, Class daoDefinedClass, Object[] args) throws ClassTypeException, RegisterMethodException {
        assert daoClass != null && daoDefinedClass != null: "DaoImplInterface and DaoDefinedClass must not be null!";
        if (daoClass.isAnnotationPresent(BasicDaoDeclared.class)) {
            BasicDaoDeclared basicDaoDeclared = daoClass.getDeclaredAnnotation(BasicDaoDeclared.class);
            if (basicDaoDeclared.value() != null) {
                try {
                    MethodMapRegister methodMapRegister = basicDaoDeclared.value().newInstance();
                    methodMapRegister.registerDaoImpl(daoClass, daoDefinedClass, args);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RegisterMethodException(e);
                }
            } else
                throw new DaoImplClassException("Your supported DaoImplInterface not set [" + MethodMapRegister.class.getName() + "]");
        }
    }
}
