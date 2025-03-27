package io.github.maxwellnie.javormio.framework.core.executor.result.method;

import io.github.maxwellnie.javormio.framework.core.executor.result.TypeMapping;
import io.github.maxwellnie.javormio.framework.common.java.reflect.Reflection;

/**
 * @author Maxwell Nie
 * @author yurongqi
 */
public interface methodReflection {
    Reflection<?> getReflection(TypeMapping typeMapping);
    void setNullReflection();
}
