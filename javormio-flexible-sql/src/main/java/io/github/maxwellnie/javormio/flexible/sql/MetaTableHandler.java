package io.github.maxwellnie.javormio.flexible.sql;

import io.github.maxwellnie.javormio.common.annotation.table.Table;
import io.github.maxwellnie.javormio.common.annotation.table.column.Column;
import io.github.maxwellnie.javormio.common.annotation.table.column.PrimaryKey;
import io.github.maxwellnie.javormio.core.translation.table.column.ColumnInfo;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.ClassName;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.MetaColumn;
import io.github.maxwellnie.javormio.flexible.sql.plugin.table.MetaTable;
import io.github.maxwellnie.javormio.source.code.processor.CustomProcessor;
import io.github.maxwellnie.javormio.source.code.processor.SPIPlugin;
import org.apache.velocity.VelocityContext;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
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
@SPIPlugin(Table.class)
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
//            metaTable.packagePath = ;
            metaTable.metaColumns = typeElement.getEnclosedElements()
                    .stream()
                    .filter(e1 -> e1.getKind() == ElementKind.FIELD)
                    .map(e1 -> {
                        PrimaryKey primaryKey = e1.getAnnotation(PrimaryKey.class);
                        MetaColumn metaColumn = new MetaColumn();
                        Column column = e1.getAnnotation(Column.class);
                        if (column != null) {

                        } else if (primaryKey != null) {
                            metaColumn.fieldName  = e1.getSimpleName().toString();
                        } else {
                            Class<?> type = e1.asType().getClass();
                            metaColumn.fieldName  = e1.getSimpleName().toString();
                        }
                        return metaColumn;
                    })
                    .collect(Collectors.toList());
//            metaTable.defaultDataSourceName = tableAnnotation.defaultDataSourceName();

/*
            String path = "io.github.maxwellnie.javormio.flexible.sql.plugin.meta."+typeElement.getQualifiedName().toString();
            try {
                processingEnv.getFiler().createSourceFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 然后生成属性,写入文件
            List<? extends Element> fields = typeElement.getEnclosedElements();
            for (Element field : fields) {
                if (field.getKind() != ElementKind.FIELD) continue;

                String fieldName = field.getSimpleName().toString();
                String fieldType = field.asType().toString();
            }
            //，最后根据名字生成set，get方法。

            //根据类建立元数据
            //最后给velocity中添加元数据
            *
             * io.github.xx.aa.User
             * meta.io.github.xx.aa=>判断是否存在这个文件夹，没有就创建
             * （MetaUser）put=>meta.io.github.xx.aa文件夹
             * HashSet
             * hashSet.add(user.class);
             * if(hashset.contains(user.class))
             * MetaUser
            //ReflectionUtils
            //meta_user.java
               package meta;
               public class MetaUser extend BaseMetaTableInfo{
                   public String tableName ="tb_user";
                   public MetaColumnInfo userId = new MetaColumnInfo{
                         String name = "";
                         Class<?> type = io.github.xx.aa.User.class;
                         Class<?> typeHandler = null;
                         Method getter = ReflectionUtils.getMethod(io.github.xx.aa.User.class, "userId");
                   };
               }
                public interface Meta{
                    meta.io.github.xx.aa.MetaUser io_github_xx_aa_MetaUser = new meta.io.github.xx.aa.MetaUser();
                }
*/
            // 2. 类型转换


            List<? extends Element> fields = element.getEnclosedElements()
                    .stream()
                    .filter(e1 -> e1.getKind() == ElementKind.FIELD)
                    .collect(Collectors.toList());
/*            for (Element field : fields) {
                ColumnInfo columnInfo = new ColumnInfo();
                Column column = field.getAnnotation(Column.class);
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                if (column != null) {

                } else if (primaryKey != null) {
                    columnInfo.setColumnName(field.getSimpleName().toString());
                } else {
                    Class<?> type = field.asType().getClass();
                    columnInfo.setColumnName(field.getSimpleName().toString());
                }
            }*/

            VelocityContext context = new VelocityContext();
            context.put("metaTable",metaTable );



            // 5. 生成元数据类（示例），如果文件存在，删除然后重新生成
            try (Writer writer = processingEnv.getFiler()
                    .createSourceFile("meta.TableMeta_" + element.getSimpleName())
                    .openWriter()) {
                writer.write(String.format(
                        "package meta;\n" +
                                "public class TableMeta_%s {\n" +
                                "    public static final String TABLE_NAME = \"%s\";\n" +
                                "    // 可添加字段元数据解析\n" +
                                "}",
                        element.getSimpleName(),
                        tableName
                ));
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
}
