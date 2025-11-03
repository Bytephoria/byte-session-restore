package team.bytephoria.bytesessionrestore.spi.storage.config;

/**
 * Represents JDBC pool configuration shared across all SQL storage implementations.
 * <p>
 * Acts as a reusable configuration value object that defines how a
 * connection pool should behave (timeouts, pool size, etc.).
 * </p>
 */
public final class JdbcPoolConfig {

    private final long connectionTimeoutMs;
    private final long maxLifetimeMs;
    private final int maxPoolSize;
    private final int minIdle;

    public JdbcPoolConfig(
            long connectionTimeoutMs,
            long maxLifetimeMs,
            int maxPoolSize,
            int minIdle
    ) {
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.maxLifetimeMs = maxLifetimeMs;
        this.maxPoolSize = maxPoolSize;
        this.minIdle = minIdle;
    }

    public long connectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public long maxLifetimeMs() {
        return maxLifetimeMs;
    }

    public int maxPoolSize() {
        return maxPoolSize;
    }

    public int minIdle() {
        return minIdle;
    }
}
