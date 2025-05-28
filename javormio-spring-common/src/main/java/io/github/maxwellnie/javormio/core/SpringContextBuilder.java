package io.github.maxwellnie.javormio.core;

import io.github.maxwellnie.javormio.common.java.jdbc.datasource.DataBaseModelManager;
import io.github.maxwellnie.javormio.common.java.type.TypeHandler;

/**
 * @author Maxwell Nie
 */
public class SpringContextBuilder implements ContextBuilder {
    private final DataBaseModelManager dataBaseModelManager;
    private final Context context = new Context();

    public SpringContextBuilder(DataBaseModelManager dataBaseModelManager) {
        this.dataBaseModelManager = dataBaseModelManager;
        context.dataBaseModelManager = dataBaseModelManager;
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
