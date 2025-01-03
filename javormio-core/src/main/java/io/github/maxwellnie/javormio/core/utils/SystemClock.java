package io.github.maxwellnie.javormio.core.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 系统时钟工具类，解决System.currentTimeMillis()被高并发调用时会出现速度下降的问题。<br/>
 * 每1ms获取一次当前时间，在下一个1ms内的时间为在上一个1ms的时间。
 * <br/>
 * <p>高并发问题原因如下：</p>
 * <p>System.currentTimeMillis()是通过调用操作系统的系统调用gettimeofday()方法，多线程下会导致调用频繁影响性能。</p>
 *
 * @author Maxwell Nie
 */
public class SystemClock {
    /**
     * 1毫秒周期
     */
    private final int period;
    /**
     * 当前时间
     */
    private final AtomicLong NOW;

    SystemClock(int period) {
        this.period = period;
        this.NOW = new AtomicLong(System.currentTimeMillis());
        scheduleClock();
    }

    public static SystemClock getClock() {
        return new SystemClock(1);
    }

    /**
     * 获取带时钟偏差（获取时间的间隔高于1ms）的时钟,对于性能需求极高的场景请使用这个“误差钟”
     *
     * @param period 时钟周期
     * @return SystemClock
     */
    public static SystemClock getDeviationClock(int period) {
        return new SystemClock(period);
    }

    public long now() {
        return NOW.get();
    }

    private void scheduleClock() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(
                r -> {
                    Thread t = new Thread(r, "SystemClock");
                    t.setDaemon(true);
                    return t;
                }
        );
        scheduler.scheduleAtFixedRate(() -> {
            this.NOW.set(System.currentTimeMillis());
        }, this.period, this.period, TimeUnit.MILLISECONDS);
        this.NOW.set(System.currentTimeMillis());
    }

    public long currentTimeMillis() {
        return this.NOW.get();
    }
}
