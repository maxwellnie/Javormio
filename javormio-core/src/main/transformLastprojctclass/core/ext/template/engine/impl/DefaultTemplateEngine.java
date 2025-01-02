package com.maxwellnie.velox.sql.core.ext.template.engine.impl;

import com.maxwellnie.velox.sql.core.ext.template.engine.TemplateEngine;
import com.maxwellnie.velox.sql.core.ext.template.engine.parser.TemplateParser;
import com.maxwellnie.velox.sql.core.ext.template.engine.parser.impl.DefaultTemplateParser;

/**
 * @author Maxwell Nie
 */
public class DefaultTemplateEngine implements TemplateEngine {
    @Override
    public TemplateParser getParser() {
        return new DefaultTemplateParser();
    }
}
