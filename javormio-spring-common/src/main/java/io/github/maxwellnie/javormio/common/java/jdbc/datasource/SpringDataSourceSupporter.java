package io.github.maxwellnie.javormio.common.java.jdbc.datasource;

import io.github.maxwellnie.javormio.common.annotation.proxy.Interceptor;
import io.github.maxwellnie.javormio.common.annotation.proxy.ProxyDefine;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.InvocationLine;
import io.github.maxwellnie.javormio.common.java.proxy.invocation.InvokerContext;

import java.sql.Connection;

/**
 * 数据源支持者
 * @author Maxwell Nie
 */
@ProxyDefine(interfaces = {DataBaseModelManager.class}, index = 0)
public class SpringDataSourceSupporter {
    @Interceptor(DataBaseModelManager.class)
    public Connection getConnection(InvocationLine invocationLine, InvokerContext<DataBaseModelManager> invokerContext) {
        return new SpringConnectionWrapper(invokerContext.getTarget().getCurrentDataSource().getDataSource());
    }
}
