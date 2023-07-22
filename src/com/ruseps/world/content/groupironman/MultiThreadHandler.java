package com.ruseps.world.content.groupironman;


import java.util.concurrent.*;

public class MultiThreadHandler {
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    public static void submit(Runnable runnable) {
        executor.execute(runnable);
    }

    public static <V> V submitCallable(Callable<V> callable) {
        try {
            return executor.submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void submit(Runnable runnable, int seconds) {
        scheduledExecutor.schedule(runnable, seconds, TimeUnit.SECONDS);
    }

    public static void submitAtFixedRate(Runnable runnable, int seconds) {
        scheduledExecutor.scheduleAtFixedRate(runnable, 0, seconds, TimeUnit.SECONDS);
    }
}