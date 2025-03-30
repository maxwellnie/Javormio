package io.github.maxwellnie.javormio.core.api;

/**
 * 动态查询，支持关于各种复杂查询
 *
 * @author Maxwell Nie
 */
public class DynamicQuery {
    /**
     * 执行SQL并将结果集转换为弱类型
     *
     * @param sql       SQL语句
     * @param cacheName 缓存名称
     * @param args      参数
     * @return WeakType
     */
    public WeakType dynamicQuery(String sql, String cacheName, Object... args) {
        return null;
    }
}
