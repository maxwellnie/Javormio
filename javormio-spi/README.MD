# 介绍
本插件是Javormio框架的一个扩展插件，基于Java Annotation Processor功能来处理源代码

# 使用方法
1. 添加依赖
```xml
<dependency>
    <groupId>com.javormio</groupId>
    <artifactId>javormio-processor</artifactId>
    <version>1.0.0.SNAPSHOT</version>
</dependency>
```
2. 添加注解
```java
public @interface Annotation1 {
    String value();
}
```
3. 添加处理器

```java
import io.github.maxwellnie.javormio.ElementHandler;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Annotation1Processor implements ElementHandler{
    @Override
    public void handle(Set<? extends Element> element, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv){
        for (Element element : elements) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "@Annotation1: " + element.getSimpleName() +", element);
        }
    }
}
```
4. 添加配置类

```java
import io.github.maxwellnie.javormio.ElementHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Annotation1ProcessorConfiguration implements Configuration {
    @Override
    public Map<String, List<ElementHandler>> getElementHandlersMap() {
        HashMap<String, List<ElementHandler>> map = new HashMap<>();
        map.put(Annotation1.class.getName(), Collections.singletonList(new Annotation1Processor()));
        return map;
    }
}
```
5. 添加配置文件CoreProcessor.properties

```java
configuration-class=Annotation1ProcessorConfiguration
```
6. 运行
```shell
mvn clean compile
```
7. 运行结果
```shell
[INFO] --- javormio-processor:1.0.0.SNAPSHOT:process (default) @ javormio-processor-demo ---
[INFO] @Annotation1: Annotation1Processor
```