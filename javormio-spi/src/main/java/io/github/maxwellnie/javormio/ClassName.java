package io.github.maxwellnie.javormio;

/**
 * @author Maxwell Nie
 */
public class ClassName {
    String name;
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
