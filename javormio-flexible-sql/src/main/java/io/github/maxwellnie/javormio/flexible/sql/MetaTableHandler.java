package io.github.maxwellnie.javormio.flexible.sql;

import io.github.maxwellnie.javormio.common.annotation.table.Table;
import io.github.maxwellnie.javormio.common.annotation.table.column.Column;
import io.github.maxwellnie.javormio.common.annotation.table.column.PrimaryKey;
import io.github.maxwellnie.javormio.common.java.table.column.ColumnType;
import io.github.maxwellnie.javormio.common.java.table.primary.KeyGenerator;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ClassName;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.MetaColumn;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.MetaTable;
import io.github.maxwellnie.javormio.source.code.processor.CustomProcessor;
import io.github.maxwellnie.javormio.source.code.processor.Libraries;
import io.github.maxwellnie.javormio.source.code.processor.SPIPlugin;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Maxwell Nie
 */
@SPIPlugin(Table.class)
@Libraries(VelocityLibrary.class)
public class MetaTableHandler implements CustomProcessor {

    @Override
    public void process(Set<? extends Element> elements, ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
        for (Element element : elements) {
            // 1. 类型校验
            if (element.getKind() != ElementKind.CLASS) { // 确保是类级别注解
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "@Table can only be applied to classes", element);
                return;
            }

            //提取包名，类名
            TypeElement typeElement = (TypeElement) element;
            String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
            String className = typeElement.getSimpleName().toString();

            // 3. 获取注解实例
            Table tableAnnotation = element.getAnnotation(Table.class);
            if (tableAnnotation == null) return;
            // 4. 提取注解属性
            String tableName = tableAnnotation.value().isEmpty() ?
                    element.getSimpleName().toString() :
                    tableAnnotation.value();
            //创建metatable对象
            MetaTable metaTable = new MetaTable();
            ClassName className1 = new ClassName();
            className1.name = className;
            metaTable.className = className1;
            metaTable.tableName =tableName;
            metaTable.packagePath = packageName;
            metaTable.defaultDataSourceName = tableAnnotation.defaultDataSourceName().isEmpty()
                    ? "default"
                    : tableAnnotation.defaultDataSourceName();
            metaTable.imports = new ArrayList<>(getDefaultImports());
            /*
            * 如果需要添加别的依赖，则添加到if中
            * */
//            if (){
//                metaTable.imports.add();
//            }
            metaTable.metaColumns = typeElement.getEnclosedElements()
                    .stream()
                    .filter(e1 -> e1.getKind() == ElementKind.FIELD)
                    .map(tableClass -> {
                        MetaColumn metaColumn = new MetaColumn();
                        Column column = tableClass.getAnnotation(Column.class);
                        PrimaryKey primaryKey = tableClass.getAnnotation(PrimaryKey.class);
                        metaColumn.fieldName = tableClass.getSimpleName().toString();
                        if (column != null) {
                            metaColumn.columnName = metaColumn.fieldName;
                            metaColumn.typeHandlerClassName = column.typeHandler().getName();  ;
                            metaColumn.columnType = ColumnType.NORMAL;
                        }
                        if (primaryKey != null) {
                            //需要验证是否无主键生成器
                            Class<? extends KeyGenerator> keyGeneratorClass = primaryKey.keyGenerator();
                            if (keyGeneratorClass != KeyGenerator.class)
                                metaColumn.keyGeneratorClassName = keyGeneratorClass.getName();
                            //掩码需要使用|运算符才不会在添加掩码时导致原掩码丢失
                            metaColumn.columnType = metaColumn.columnType | ColumnType.PRIMARY;
                        }
                        metaColumn.typeName = tableClass.asType().toString();
                        //加入_符号不符合Java命名规范
                        metaColumn.getterClassName = "get"  + metaColumn.fieldName;
                        metaColumn.setterClassName = "set"  + metaColumn.fieldName;
                        return metaColumn;
                    })
                    .collect(Collectors.toList());
//            metaTable.defaultDataSourceName = tableAnnotation.defaultDataSourceName();


            List<? extends Element> fields = element.getEnclosedElements()
                    .stream()
                    .filter(e1 -> e1.getKind() == ElementKind.FIELD)
                    .collect(Collectors.toList());
            VelocityContext context = new VelocityContext();
            context.put("metaTable",metaTable );

            Template template = Velocity.getTemplate("classpath:gen-code-templates/MetaTable.java.vm");
            String generatedPackageName = metaTable.packagePath + ".meta";
            String generatedClassName = "Meta" + metaTable.className.name;

            // 5. 生成元数据类（示例），如果文件存在，删除然后重新生成
            try (Writer writer = processingEnv.getFiler()
                    .createSourceFile(generatedPackageName+"."+generatedClassName)
                    .openWriter()) {

                template.merge(context, writer);
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Failed to generate meta class: " + e.getMessage());
            } finally {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Generated meta class for " + element.getSimpleName());
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Fields:");
                for (Element field : fields) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                            field.getSimpleName());
                }
            }
        }
    }
    private List<String> getDefaultImports(){
        return Arrays.asList(
                "io.github.maxwellnie.javormio.common.java.table.BaseMetaTableInfo",
                "io.github.maxwellnie.javormio.common.java.reflect.property.MetaField",
                "io.github.maxwellnie.javormio.common.java.table.column.ColumnInfo",
                "io.github.maxwellnie.javormio.common.java.table.primary.PrimaryInfo"
        );
    }
}
