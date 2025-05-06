package io.github.maxwellnie.javormio.common.cache;

import java.util.HashSet;

/**
 * @author Maxwell Nie
 */
public class CacheValue {
    private Object value;
    private String key;
    private HashSet<CacheKey> linkedKeys;
}
