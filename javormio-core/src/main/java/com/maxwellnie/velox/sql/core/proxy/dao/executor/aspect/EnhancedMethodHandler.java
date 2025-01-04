package com.maxwellnie.velox.sql.core.proxy.dao.executor.aspect;

import com.maxwellnie.velox.sql.core.natives.exception.MethodNotSupportException;

/**
 * @author Maxwell Nie
 */
public class EnhancedMethodHandler extends AbstractMethodHandler {
    public EnhancedMethodHandler(long index, TargetMethodSignature signature) {
        super(index, null, signature);
    }

    @Override
    public Object handle(SimpleInvocation simpleInvocation) {
        throw new MethodNotSupportException("The \"handle(SimpleInvocation)\" is not support,Please read User Document.");
    }
}
