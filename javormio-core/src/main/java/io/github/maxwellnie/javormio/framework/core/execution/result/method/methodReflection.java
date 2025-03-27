package io.github.maxwellnie.javormio.framework.core.execution.result.method;

import io.github.maxwellnie.javormio.framework.core.execution.result.TypeMapping;
import io.github.maxwellnie.javormio.framework.common.java.reflect.Reflection;

/**
 * @author Maxwell Nie
 * @author yurongqi
 */
public interface methodReflection {
    Reflection<?> getReflection(TypeMapping typeMapping);
    void setNullReflection();
}
