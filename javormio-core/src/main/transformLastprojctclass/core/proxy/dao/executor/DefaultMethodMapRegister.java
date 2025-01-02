package com.maxwellnie.velox.sql.core.proxy.dao.executor;

import com.maxwellnie.velox.sql.core.annotation.dao.SQL;
import com.maxwellnie.velox.sql.core.annotation.dao.SQLMethod;
import com.maxwellnie.velox.sql.core.config.simple.SingletonConfiguration;
import com.maxwellnie.velox.sql.core.ext.executor.QuickCustomMethodExecutor;
import com.maxwellnie.velox.sql.core.ext.template.SQLTemplateParser;
import com.maxwellnie.velox.sql.core.ext.template.SqlTemplateInfo;
import com.maxwellnie.velox.sql.core.ext.template.engine.TemplateEngine;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfoManager;
import com.maxwellnie.velox.sql.core.natives.exception.DaoRegisterException;
import com.maxwellnie.velox.sql.core.natives.exception.RegisterMethodException;
import com.maxwellnie.velox.sql.core.natives.database.context.Context;
import com.maxwellnie.velox.sql.core.utils.framework.MethodExecutorUtils;
import com.maxwellnie.velox.sql.core.utils.reflect.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 默认的方法映射注册器
 *
 * @author Maxwell Nie
 */
public class DefaultMethodMapRegister implements MethodMapRegister {
    @Override
    public void registerEntityDefinedDaoImpl(Class<?> clazz, Object[] args) {
        assert clazz != null;
        for (Method method : ReflectionUtils.getInterfaceAllDeclaredMethods(clazz)) {
            if (method.isAnnotationPresent(SQLMethod.class) && !method.isDefault()) {
                SQLMethod SQLMethod = method.getDeclaredAnnotation(SQLMethod.class);
                if (SQLMethod.value() != null) {
                    try {
                        Context.MethodMappedManager methodMappedManager = (Context.MethodMappedManager) args[1];
                        MethodExecutor methodExecutor = SQLMethod.value().getConstructor().newInstance();
                        methodExecutor.setMethodMappedManager(methodMappedManager);
                        methodMappedManager.registeredMapped(MethodExecutorUtils.getMethodDeclaredName(method), methodExecutor);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        throw new RegisterMethodException("The executor " + SQLMethod.value() + " of method " + method + "  cannot be instantiated.", e);
                    }
                }
            }
        }
    }

    @Override
    public void registerDaoImpl(Class<?> daoImpl, Class<?> daoDefinedClass, Object[] args) {
        TemplateEngine templateEngine = SingletonConfiguration.getInstance().getTemplateEngine();
        assert daoImpl != null && daoDefinedClass != null;
        assert templateEngine != null : "The template engine cannot be null.Please set a Template instance.";
        Context.MethodMappedManager methodMappedManager = (Context.MethodMappedManager) args[1];
        for (Method method : daoDefinedClass.getMethods()) {
            if (method.isAnnotationPresent(SQLMethod.class) && !method.isDefault()) {
                SQLMethod SQLMethod = method.getDeclaredAnnotation(SQLMethod.class);
                if (SQLMethod.value() != null) {
                    try {
                        MethodExecutor methodExecutor = SQLMethod.value().getConstructor().newInstance();
                        methodExecutor.setMethodMappedManager(methodMappedManager);
                        methodMappedManager.registeredMapped(MethodExecutorUtils.getMethodDeclaredName(method), methodExecutor);
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        throw new RegisterMethodException("The executor " + SQLMethod.value() + " of method " + method + "  cannot be instantiated.", e);
                    }
                }
            }else if(method.isAnnotationPresent(SQL.class)){
                SQL sql = method.getDeclaredAnnotation(SQL.class);
                Class<?> executor = sql.executor();
                MethodExecutor methodExecutor;
                if(executor != null && executor != QuickCustomMethodExecutor.class){
                    try {
                        methodExecutor = (MethodExecutor) ReflectionUtils.newInstance(executor);
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new DaoRegisterException(e);
                    }
                    methodMappedManager.registeredMapped(MethodExecutorUtils.getMethodDeclaredName(method), methodExecutor);
                }else {
                    Class<?> entityClass = (Class<?>) args[0];
                    TableInfo tableInfo = TableInfoManager.getTableInfo(entityClass);
                    methodExecutor = SQLTemplateParser.getInstance().parse(method, daoDefinedClass, tableInfo);
                    methodMappedManager.registeredMapped(((QuickCustomMethodExecutor)methodExecutor).getName(), methodExecutor);
                }
            }
        }
    }
}
