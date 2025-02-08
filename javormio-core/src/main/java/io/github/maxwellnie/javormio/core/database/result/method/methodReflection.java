package io.github.maxwellnie.javormio.core.database.result.method;

import io.github.maxwellnie.javormio.core.database.result.TypeMapping;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;

/**
 * @author Maxwell Nie
 * @author yurongqi
 */
public interface methodReflection {
    Reflection<?> getReflection(TypeMapping typeMapping);
    void setNullReflection();
}
