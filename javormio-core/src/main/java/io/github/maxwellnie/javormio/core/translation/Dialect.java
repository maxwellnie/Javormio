package io.github.maxwellnie.javormio.core.translation;

import io.github.maxwellnie.javormio.common.annotation.document.ExtensionPoint;
import io.github.maxwellnie.javormio.core.translation.sql.SqlFragment;

/**
 * 数据库方言处理器，用于对sql的各个片段进行处理
 *
 * @author Maxwell Nie
 */
@ExtensionPoint
public interface Dialect {
    /**
     * 在sql片段创建前对sql片段进行处理
     * <p>默认的处理：&#xA;
     * 当处于SELECT_FRAGMENT&LIMIT_FRAGMENT时，在SELECT构建前注入默认的SQL;
     * 处于LIMIT_FRAGMENT时，创建LIMIT语句；
     * </p>
     * @param sqlFragment sql片段
     * @param mode        处理模式
     * @param params      参数，如果是LIMIT_FRAGMENT模式，则参数为limit与offset的值
     * @return 处理后的sql片段
     */
    SqlFragment beforeSqlBuild(SqlFragment sqlFragment, int mode, Object... params);
    /**
     * 在sql片段创建后对sql片段进行处理
     *
     * @param sqlFragment sql片段
     * @param mode        处理模式
     * @param params      参数
     * @return 处理后的sql片段
     * */
    SqlFragment afterSqlBuild(SqlFragment sqlFragment, int mode, Object... params);
    /**
     * sql片段的处理模式
     */
    int SELECT_FRAGMENT = 1;
    int UPDATE_FRAGMENT = 2;
    int DELETE_FRAGMENT = 3;
    int INSERT_FRAGMENT = 4;
    int LIMIT_FRAGMENT = 5;
    int OTHER = 6;//如果处理模式不属于以上5种模式，那么需要用户自行定义处理模式标志位及处理逻辑
}
