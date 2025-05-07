package io.github.maxwellnie.javormio;

import org.apache.velocity.app.Velocity;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.InputStream;
import java.util.*;


/**
 * @author Maxwell Nie
 */
public class CoreProcessor extends AbstractProcessor {
    private final Map<String, List<ElementHandler>> elementHandlersMap;
    private final Set<String> supportedAnnotationTypes;
    private final Set<String> supportedOptions;
    private SourceVersion supportedSourceVersion;
    private final Properties velocityProperties;

    public CoreProcessor() {
        elementHandlersMap = new HashMap<>();
        List<ElementHandler> tableHandlers = new LinkedList<>();
        tableHandlers.add(new MetaTableHandler());
        elementHandlersMap.put("io.github.maxwellnie.javormio.common.annotation.table.Table", tableHandlers);
        // typeElementHandlers.put("io.github.maxwellnie.javormio.common.annotation.proxy.ProxyDefine", new ProxyHandler());
        supportedOptions = new HashSet<>();
        supportedAnnotationTypes = new HashSet<>();
        supportedAnnotationTypes.addAll(elementHandlersMap.keySet());
        supportedSourceVersion = SourceVersion.RELEASE_8;
        velocityProperties  = new Properties();
        velocityProperties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        loadConfigurations();
        initVelocity();
    }

    /**
     * 初始化velocity
     */
    private void initVelocity() {
        Velocity.init(velocityProperties);
    }

    /**
     * 加载配置
     */
    private void loadConfigurations() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("CoreProcessor.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            String classNameStr = properties.getProperty("configuration-class");
            String[] classNames = classNameStr == null ? null : classNameStr.split(",");
            if (classNames != null) {
                for (String className : classNames) {
                    if (className != null) {
                        Class<?> clazz = Class.forName(className);
                        if (Configuration.class.isAssignableFrom(clazz)) {
                            Configuration configuration = (Configuration) clazz.getConstructor().newInstance();
                            Optional.of(configuration.getElementHandlersMap())
                                    .ifPresent(m->{
                                        for(Map.Entry<String, List<ElementHandler>> entry: m.entrySet()){
                                            List<ElementHandler> elementHandlers = elementHandlersMap.get(entry.getKey());
                                            if (elementHandlers != null)
                                                elementHandlers.addAll(entry.getValue());
                                            else
                                                elementHandlersMap.put(entry.getKey(), entry.getValue());
                                        }
                                    });
                            Optional.of(configuration.getSupportedOptions())
                                    .ifPresent(supportedOptions::addAll);
                            Optional.of(configuration.getSupportedSourceVersion())
                                    .ifPresent(sourceVersion -> supportedSourceVersion = sourceVersion);
                            Optional.of(configuration.getElementHandlersMap())
                                    .ifPresent(map -> supportedAnnotationTypes.addAll(map.keySet()));
                            Optional.of(configuration.getVelocityProperties())
                                    .ifPresent(this.velocityProperties::putAll);
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            List<ElementHandler> elementHandlers = this.elementHandlersMap.get(annotation.getQualifiedName().toString());
            if (elementHandlers != null) {
                for (ElementHandler elementHandler : elementHandlers)
                    elementHandler.handle(roundEnv.getElementsAnnotatedWith(annotation), processingEnv, roundEnv);
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "No handler for annotation " + annotation.getQualifiedName(), annotation);
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return supportedSourceVersion;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return supportedOptions;
    }
}
