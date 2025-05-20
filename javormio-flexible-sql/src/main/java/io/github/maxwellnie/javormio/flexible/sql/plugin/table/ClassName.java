package io.github.maxwellnie.javormio.flexible.sql.plugin.table;

/**
 * @author Maxwell Nie
 */
public class ClassName {
    public String name;

    public String getSimpleName() {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    @Override
    public String toString() {
        return "ClassName{" +
                "name='" + name + '\'' +
                '}';
    }

}
