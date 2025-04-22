package io.github.maxwellnie.javormio;

import io.github.maxwellnie.javormio.common.annotation.table.Table;
import io.github.maxwellnie.javormio.common.annotation.table.column.Column;
import io.github.maxwellnie.javormio.common.annotation.table.column.PrimaryKey;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Maxwell Nie
 */
@SupportedAnnotationTypes("io.github.maxwellnie.javormio.common.annotation.table.Table")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SPIProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Table.class)) { // 直接获取Table注解元素
            // 1. 类型校验
            if (element.getKind() != ElementKind.CLASS) { // 确保是类级别注解
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "@Table can only be applied to classes", element);
                continue;
            }
            //ReflectionUtils
            //meta_user.java
            /*
               package meta;
               public class meta_user extend BaseTable{
                   public String tableName ="tb_user";
                   public ColumnInfo userId = new ColumnInfo{
                         String name = "";
                         Class<?> type = io.github.xx.aa.User.class;
                         Class<?> typeHandler = null;
                         Method getter = ReflectionUtils.getMethod(io.github.xx.aa.User.class, "userId");
                   };
               }
             */
            /*
                public interface Meta{
                    meta_user meta_user = new meta_user();
                    meta_role meta_role = new meta_role();
                }
             */
            // 2. 类型转换
            TypeElement classElement = (TypeElement) element;

            // 3. 获取注解实例
            Table tableAnnotation = classElement.getAnnotation(Table.class);
            if (tableAnnotation == null) continue;
            // 4. 提取注解属性
            String tableName = tableAnnotation.value().isEmpty() ?
                    classElement.getSimpleName().toString() :
                    tableAnnotation.value();
            List<? extends Element> fields = classElement.getEnclosedElements()
                    .stream()
                    .filter(e -> e.getKind() == ElementKind.FIELD)
                    .collect(Collectors.toList());
            for (Element field : fields){
                ColumnInfo columnInfo = new ColumnInfo();
                Column column = field.getAnnotation(Column.class);
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                if (column != null){

                }else if(primaryKey != null){
                    columnInfo.setColumnName(field.getSimpleName().toString());
                }else {
                    Class<?> type = field.asType().getClass();
                    columnInfo.setColumnName(field.getSimpleName().toString());
                }
            }

            // 5. 生成元数据类（示例），如果文件存在，删除然后重新生成
            try (Writer writer = processingEnv.getFiler()
                    .createSourceFile("meta.TableMeta_" + classElement.getSimpleName())
                    .openWriter()) {
                writer.write(String.format(
                        "package meta;\n" +
                                "public class TableMeta_%s {\n" +
                                "    public static final String TABLE_NAME = \"%s\";\n" +
                                "    // 可添加字段元数据解析\n" +
                                "}",
                        classElement.getSimpleName(),
                        tableName
                ));
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Failed to generate meta class: " + e.getMessage());
            }finally {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Generated meta class for " + classElement.getSimpleName());
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Fields:");
                for (Element field : fields){
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                              field.getSimpleName());
                }
            }
        }
        return true;
    }

}
