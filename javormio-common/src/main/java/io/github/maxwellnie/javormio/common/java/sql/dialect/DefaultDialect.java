package io.github.maxwellnie.javormio.common.java.sql.dialect;

import io.github.maxwellnie.javormio.common.java.sql.ColumnEscapeKit;
import io.github.maxwellnie.javormio.common.java.sql.ansi.DDLKit;
import io.github.maxwellnie.javormio.common.java.sql.ansi.DMLKit;
import io.github.maxwellnie.javormio.common.java.sql.ansi.TCLKit;

import java.util.HashSet;

/**
 * @author Maxwell Nie
 */
public class DefaultDialect implements Dialect{
    DMLKit dmlKit = (sqlFragment, offset, limit, isPrepared) -> {
        if (isPrepared)
            return sqlFragment.append(" LIMIT ? OFFSET ?");
        else
            return sqlFragment.append(" LIMIT ").append(offset).append("OFFSET ").append(limit);
    };
    DDLKit ddlKit = null;
    TCLKit tclKit = null;
    ColumnEscapeKit  escapeKit = new ColumnEscapeKit() {
        final HashSet<String> keyWords = new HashSet<>();
        {
            keyWords.add("password");
            keyWords.add("user");
            keyWords.add("group");
            keyWords.add("order");
            keyWords.add("limit");
            keyWords.add("offset");
            keyWords.add("select");
            keyWords.add("update");
            keyWords.add("delete");
            keyWords.add("insert");
            keyWords.add("into");
            keyWords.add("values");
            keyWords.add("set");
            keyWords.add("from");
            keyWords.add("where");
            keyWords.add("by");
            keyWords.add("having");
            keyWords.add("asc");
            keyWords.add("desc");
            //补全关键字
        }
        @Override
        public String escape(String columnName) {
            return "`" + columnName + "`";
        }

        @Override
        public boolean isEscape(String columnName) {
            return keyWords.contains(columnName.toLowerCase());
        }
    };
    @Override
    public DMLKit getDMLKit() {
        return dmlKit;
    }

    @Override
    public DDLKit getDDLKit() {
        return ddlKit;
    }

    @Override
    public TCLKit getTCLKit() {
        return tclKit;
    }

    @Override
    public ColumnEscapeKit getEscapeKit() {
        return escapeKit;
    }

    @Override
    public String getDatabaseTypeName() {
        return "UNKNOWN";
    }
}
