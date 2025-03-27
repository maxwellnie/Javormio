package io.github.maxwellnie.javormio.framework.common.annotation.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于绑定SqlMethod所映射的方法参数<br/>
 * <code>
 * public void updateById(@Parameter(name = "id") int id);
 * </code>
 *
 * @author Maxwell Nie
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    /**
     * 参数名
     *
     * @return String
     */
    String name();
}
