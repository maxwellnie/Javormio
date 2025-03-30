package io.github.maxwellnie.javormio.core.api;

import io.github.maxwellnie.javormio.core.Context;
import io.github.maxwellnie.javormio.core.api.dynamic.DynamicSql;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Maxwell Nie
 */
public class T {
    public static void main(String[] args) throws InterruptedException {
        Context context = new Context();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8, 10000, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        List<DynamicSql> list = new LinkedList<>();
        Runnable runnable = () -> {
            context.newDynamicSql(String.class);
        };

        for (int i = 0; i < 100000000; i++) {
            threadPoolExecutor.execute(runnable);
        }
    }
}
