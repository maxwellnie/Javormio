package io.github.maxwellnie.javormio;

import javax.lang.model.SourceVersion;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 配置类，配置核心注解处理器，需要创建CoreProcessor.properties文件并设置configuration-class
 *
 * @author Maxwell Nie
 */
public interface Configuration {
    /**
     * 元素处理器
     *
     * @return Map
     */
    Map<String, List<ElementHandler>> getElementHandlersMap();

    /**
     * Velocity配置
     *
     * @return Properties
     */
    Properties getVelocityProperties();

    /**
     * 支持的源版本
     *
     * @return SourceVersion
     */
    SourceVersion getSupportedSourceVersion();

    /**
     * 支持的选项
     *
     * @return Set
     */
    Set<String> getSupportedOptions();
}
