package io.github.maxwellnie.javormio.flexible.sql.plugin.table;


import java.util.List;

/**
 * @author Maxwell Nie
 */
public class MetaTable {
    public ClassName className;
    public String defaultDataSourceName;
    public String tableName;
    public List<MetaColumn> metaColumns;
    public String packagePath;
    public List<String> imports;

}
