package com.maxwellnie.velox.sql.core.ext.template.engine.parser;

import com.maxwellnie.velox.sql.core.ext.template.SqlTemplateInfo;
import com.maxwellnie.velox.sql.core.natives.database.table.TableInfo;

import java.lang.reflect.Method;

/**
 * @author Maxwell Nie
 */
public interface TemplateParser {
    /**
     * 解析模板
     *
     * @param template
     * @param method
     * @param tableInfo
     * @return SqlTemplateInfo
     */
    SqlTemplateInfo parse(String template, Method method, TableInfo tableInfo);
}
