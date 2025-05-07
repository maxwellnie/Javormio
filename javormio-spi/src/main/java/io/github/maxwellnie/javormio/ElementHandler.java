package io.github.maxwellnie.javormio;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.Set;

/**
 * 元素处理器
 *
 * @author Maxwell Nie
 */
public interface ElementHandler {
    /**
     * 处理被遍历到的元素
     *
     * @param element
     * @param processingEnv
     * @param roundEnv
     */
    void handle(Set<? extends Element> element, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv);
}
