package io.github.maxwellnie.javormio.common.java.table.column;

/**
 * @author Maxwell Nie
 */
public interface ColumnType {
    int PRIMARY = 1;
    int INDEX = 2;
    int FOREIGN = 3;
    int UNIQUE = 4;
    int NORMAL = 5;
    int ESCAPING = 6;
}
