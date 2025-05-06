package io.github.maxwellnie.javormio;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author Maxwell Nie
 */
@SupportedAnnotationTypes("io.github.maxwellnie.javormio.common.annotation.proxy.ProxyDefine")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ProxyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
