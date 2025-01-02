package com.maxwellnie.velox.sql.core.natives.database.session;

import com.maxwellnie.velox.sql.core.natives.database.context.Context;
import com.maxwellnie.velox.sql.core.natives.resource.ResourceHolder;

/**
 * @author Maxwell Nie
 */
public interface SessionFactory extends ResourceHolder<Context> {
    Session produce();

    Session produce(boolean autoCommit);
}
