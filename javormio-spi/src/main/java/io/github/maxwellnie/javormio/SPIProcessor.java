package io.github.maxwellnie.javormio;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * @author Maxwell Nie
 */
@SupportedAnnotationTypes("io.github.maxwellnie.javormio.SPI")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SPIProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "found @Log at " + element);
                try (Writer writer = processingEnv.getFiler()
                        .createSourceFile("meta."+element.getSimpleName())
                        .openWriter()) {
                    writer.write("package meta; public class "+element.getSimpleName()+"{int a = 1;}");
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }

        return true;
    }
}
