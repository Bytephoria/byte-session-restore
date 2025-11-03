package team.bytephoria.bytesessionrestore.spi.compression;

import org.jetbrains.annotations.NotNull;

/**
 * Defines a compression algorithm for encoding and decoding byte arrays.
 * <p>
 * Implementations must be thread-safe, deterministic, and registered
 * under a unique {@link #name()}.
 */
public interface CompressionStrategy {

    /**
     * @return unique name for this strategy (e.g. "ZSTD", "GZIP", "NONE")
     */
    @NotNull String name();

    /**
     * Compresses the provided data.
     *
     * @param input uncompressed bytes
     * @return compressed bytes
     */
    byte @NotNull [] compress(byte @NotNull [] input);

    /**
     * Decompresses the provided data.
     *
     * @param input compressed bytes
     * @return original uncompressed bytes
     */
    byte @NotNull [] decompress(byte @NotNull [] input);
}
