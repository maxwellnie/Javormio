package io.github.maxwellnie.javormio.core.execution.result.method;

import io.github.maxwellnie.javormio.common.java.reflect.Reflection;
import io.github.maxwellnie.javormio.core.execution.result.TypeMapping;

/**
 * @author Maxwell Nie
 * @author yurongqi
 */
public interface methodReflection {
    Reflection<?> getReflection(TypeMapping typeMapping);

    void setNullReflection();
}
