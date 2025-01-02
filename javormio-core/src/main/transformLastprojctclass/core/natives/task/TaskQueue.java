package com.maxwellnie.velox.sql.core.natives.task;

import com.maxwellnie.velox.sql.core.cache.key.CacheKey;

import java.io.Serializable;

/**
 * 任务队列
 * @author Maxwell Nie
 */
public interface TaskQueue extends Serializable {
    /**
     * 请求排队
     * @param group
     * @param cacheKey
     * @param task
     */
    void require(String group, CacheKey cacheKey, Runnable task);
}
