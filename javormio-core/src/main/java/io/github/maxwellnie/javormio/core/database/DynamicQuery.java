package io.github.maxwellnie.javormio.core.database;

import java.util.HashMap;

/**
 * @author Maxwell Nie
 */
public class DynamicQuery {
    /**
     * 不走cache
     * @param sql
     * @param cacheName
     * @param args
     * @return HashMap<String, Object>
     */
    HashMap<String, Object> dynamicGetMap(String sql, String cacheName, Object ...args){
        return null;
    }
}
