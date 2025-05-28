package io.github.maxwellnie.javormio.common.java.sql.dialect;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.common.java.sql.ColumnEscapeKit;
import io.github.maxwellnie.javormio.common.java.sql.ansi.DDLKit;
import io.github.maxwellnie.javormio.common.java.sql.ansi.DMLKit;
import io.github.maxwellnie.javormio.common.java.sql.ansi.TCLKit;

/**
 * 数据库方言处理器，用于对sql的各个片段进行处理
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public interface Dialect {
    /**
     * 获取DML工具
     * @return DML工具
     * */
    DMLKit getDMLKit();
    /**
     * 获取DDL工具
     * @return DDL工具
     * */
    DDLKit getDDLKit();
    /**
     * 获取TCL工具
     * @return TCL工具
     * */
    TCLKit getTCLKit();
    /**
     * 获取列名转义工具
     * @return 列名转义工具
     * */
    ColumnEscapeKit getEscapeKit();
    /**
     * 获取数据库类型名称
     * @return 数据库类型名称
     * */
    String getDatabaseTypeName();
}
