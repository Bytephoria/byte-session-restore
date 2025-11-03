package team.bytephoria.bytesessionrestore.core.concurrent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.function.Supplier;

public final class AsyncExecutor {

    private static final int POOL_SIZE = 2;
    private static final int QUEUE_CAPACITY = 1024;
    private static final long KEEP_ALIVE_SECONDS = 30L;

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            POOL_SIZE,
            POOL_SIZE,
            KEEP_ALIVE_SECONDS,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(QUEUE_CAPACITY),
            runnable -> {
                final Thread thread = new Thread(runnable, "BSR-Worker");
                thread.setDaemon(true);
                return thread;
            },
            new ThreadPoolExecutor.AbortPolicy()
    );

    private AsyncExecutor() {}

    @Contract("_ -> new")
    public static @NotNull CompletableFuture<Void> runAsync(final @NotNull Runnable runnable) {
        return CompletableFuture.runAsync(runnable, EXECUTOR);
    }

    @Contract("_ -> new")
    public static <T> @NotNull CompletableFuture<T> supplyAsync(final @NotNull Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, EXECUTOR);
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
        try {
            final boolean terminated = EXECUTOR.awaitTermination(5, TimeUnit.SECONDS);
            if (!terminated) {
                EXECUTOR.shutdownNow();
            }
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            EXECUTOR.shutdownNow();
        }
    }
}
