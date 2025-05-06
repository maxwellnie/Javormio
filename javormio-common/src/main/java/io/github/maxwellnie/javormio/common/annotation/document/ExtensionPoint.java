package io.github.maxwellnie.javormio.common.annotation.document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识框架中可被第三方扩展的核心扩展点
 *
 * <p>被标注的类表示允许通过继承或组合方式进行功能扩展，
 * 框架在扫描扩展时会自动识别带有该注解的基类。</p>
 *
 * @author Maxwell Nie
 * @since 1.0.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ExtensionPoint {
    /**
     * 扩展点说明（可选）
     */
    String value() default "";
}
