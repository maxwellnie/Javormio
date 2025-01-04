package com.maxwellnie.velox.sql.core.ext.template.engine;

import com.maxwellnie.velox.sql.core.ext.template.engine.parser.TemplateParser;

/**
 * @author Maxwell Nie
 */
public interface TemplateEngine {
    TemplateParser getParser();
}
