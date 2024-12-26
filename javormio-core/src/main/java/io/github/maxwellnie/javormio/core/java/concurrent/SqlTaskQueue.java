package io.github.maxwellnie.javormio.core.java.concurrent;

import io.github.maxwellnie.javormio.core.cache.HashCacheKey;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * 与Sql操作有关的任务队列
 *
 * @author Maxwell Nie
 */
public interface SqlTaskQueue extends Serializable {
    /**
     * 请求排队
     *
     * @param group
     * @param hashCacheKey
     * @param task
     * @return Future
     */
    Future<Object> require(String group, HashCacheKey hashCacheKey, Runnable task);
}
