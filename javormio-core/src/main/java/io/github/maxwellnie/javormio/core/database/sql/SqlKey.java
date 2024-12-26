package io.github.maxwellnie.javormio.core.database.sql;

import java.util.Objects;

/**
 * @author Maxwell Nie
 */
public class SqlKey {
    private final String template;
    private final String keywords;
    private final Type type;

    public SqlKey(String template, String keywords, Type type) {
        this.template = template;
        this.keywords = keywords;
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public String getKeywords() {
        return keywords;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlKey that = (SqlKey) o;
        return Objects.equals(template, that.template) && Objects.equals(keywords, that.keywords) && type == that.type;
    }

    @Override
    public String toString() {
        return "SqlKey{" +
                "template='" + template + '\'' +
                ", keywords='" + keywords + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(template, keywords, type);
    }

    /**
     * @author Maxwell Nie
     */
    public static enum Type {
        JDBC,
        TEMPLATE
    }
}
