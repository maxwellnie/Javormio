package io.github.maxwellnie.javormio;


import java.util.List;

/**
 * @author Maxwell Nie
 */
public class MetaTable {
    public ClassName className;
    public String defaultDataSourceName;
    public String tableName;
    public List<Column> columns;
    public String packagePath;
    public List<String> imports;

}
