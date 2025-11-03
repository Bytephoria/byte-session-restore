package team.bytephoria.bytesessionrestore.core.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.spi.compression.CompressionStrategy;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CompressionRegistry {

    private static final Map<String, CompressionStrategy> STRATEGIES = new ConcurrentHashMap<>();

    public static void register(final @NotNull CompressionStrategy compressionStrategy) {
        STRATEGIES.put(compressionStrategy.name(), compressionStrategy);
    }

    public static @NotNull Optional<CompressionStrategy> find(final @NotNull String key) {
        return Optional.ofNullable(STRATEGIES.get(key.toUpperCase()));
    }

    public static @Nullable CompressionStrategy get(final @NotNull String key) {
        return STRATEGIES.get(key);
    }

    public static @NotNull CompressionStrategy getOrDefault(final @NotNull String key) {
        return find(key).orElse(STRATEGIES.get("NONE"));
    }

}
