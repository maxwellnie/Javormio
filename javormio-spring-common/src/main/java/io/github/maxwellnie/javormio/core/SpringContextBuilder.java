package io.github.maxwellnie.javormio.core;

import io.github.maxwellnie.javormio.common.java.jdbc.datasource.DynamicDataSource;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;

/**
 * @author Maxwell Nie
 */
public class SpringContextBuilder implements ContextBuilder {
    private final DynamicDataSource dynamicDataSource;
    private final Context context = new Context();

    public SpringContextBuilder(DynamicDataSource dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
        context.dynamicDataSource = dynamicDataSource;
    }
    public SpringContextBuilder addTypeHandler(Object type, TypeHandler<?> typeHandler) {
        context.typeHandlerPool.put(type, typeHandler);
        return this;
    }

    @Override
    public Context build() {
        return context;
    }
}
