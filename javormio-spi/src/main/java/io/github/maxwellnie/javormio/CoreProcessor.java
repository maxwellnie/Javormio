package io.github.maxwellnie.javormio;

import org.apache.velocity.app.Velocity;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Maxwell Nie
 */
public class CoreProcessor extends AbstractProcessor {
    private final Map<String, ElementHandler> typeElementHandlers;
    private final Set<String> supportedAnnotationTypes;
    private final Set<String> supportedOptions;
    private SourceVersion supportedSourceVersion;
    private Configuration configuration;

    public CoreProcessor() {
        typeElementHandlers = new HashMap<>();
        typeElementHandlers.put("io.github.maxwellnie.javormio.common.annotation.table.Table", new MetaTableHandler());
        // typeElementHandlers.put("io.github.maxwellnie.javormio.common.annotation.proxy.ProxyDefine", new ProxyHandler());
        supportedOptions = new HashSet<>();
        supportedAnnotationTypes = new HashSet<>();
        supportedAnnotationTypes.addAll(typeElementHandlers.keySet());
        supportedSourceVersion = SourceVersion.RELEASE_8;
        loadConfigurations();
        initVelocity();
    }

    /**
     * 初始化velocity
     */
    private void initVelocity() {
        Properties properties = configuration == null? null : configuration.getVelocityProperties();
        if (properties == null) {
            properties = new Properties();
            properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        }
        Velocity.init(properties);
    }
    /**
     * 加载配置
     */
    private void loadConfigurations() {
        try(InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("CoreProcessor.properties")){
            Properties properties = new Properties();
            properties.load(inputStream);
            String className = properties.getProperty("configuration-class");
            if (className != null){
                Class<?> clazz = Class.forName(className);
                if (Configuration.class.isAssignableFrom(clazz)){
                    configuration = (Configuration) clazz.getConstructor().newInstance();
                    Optional.of(configuration.getElementHandlers()).ifPresent(typeElementHandlers::putAll);
                    Optional.of(configuration.getSupportedOptions()).ifPresent(supportedOptions::addAll);
                    Optional.of(configuration.getSupportedSourceVersion()).ifPresent(sourceVersion -> supportedSourceVersion = sourceVersion);
                    Optional.of(configuration.getElementHandlers()).ifPresent(map -> supportedAnnotationTypes.addAll(map.keySet()));
                }
            }
        }catch (Exception ignored){

        }
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations){
            ElementHandler elementHandler = typeElementHandlers.get(annotation.getQualifiedName().toString());
            if (elementHandler != null){
                elementHandler.handle(roundEnv.getElementsAnnotatedWith(annotation), processingEnv, roundEnv);
            }else {
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
