package io.github.maxwellnie.javormio.core.database;

import io.github.maxwellnie.javormio.core.database.name.NameHandler;

/**
 * @author Maxwell Nie
 */
public interface Dialect {
    NameHandler getNameHandler();
}
