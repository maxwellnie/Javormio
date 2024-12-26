package io.github.maxwellnie.javormio.core.database;

import io.github.maxwellnie.javormio.core.database.table.TableInfo;
import io.github.maxwellnie.javormio.core.java.reflect.Reflection;

/**
 * @author Maxwell Nie
 */
public interface TableParser {
    TableInfo[] parse(Class<?>[] clazz);

    default TableInfo get()  {
        throw new RuntimeException("TableParser.get() is not support,please implement it in implement class.");
    }

    TableInfo parse(Class<?> clazz);

    TableInfo parse(Reflection<?> reflection);
}
