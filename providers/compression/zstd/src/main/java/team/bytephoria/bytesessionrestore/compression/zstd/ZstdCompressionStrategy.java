package team.bytephoria.bytesessionrestore.compression.zstd;

import com.github.luben.zstd.Zstd;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.spi.compression.CompressionStrategy;

import java.util.Arrays;

public final class ZstdCompressionStrategy implements CompressionStrategy {

    private static final int LEVEL = 4;

    @Override
    public @NotNull String name() {
        return "ZSTD";
    }

    @Override
    public byte @NotNull [] compress(final byte @NotNull [] input) {
        final long bound = Zstd.compressBound(input.length);
        final byte[] compressedBuffer = new byte[(int) bound];

        final long size = Zstd.compress(compressedBuffer, input, LEVEL);
        if (Zstd.isError(size)) {
            throw new IllegalStateException("ZSTD compression failed: " + Zstd.getErrorName(size));
        }

        return Arrays.copyOf(compressedBuffer, (int) size);
    }

    @Override
    public byte @NotNull [] decompress(final byte @NotNull [] input) {
        final long originalSize = Zstd.getFrameContentSize(input);
        if (Zstd.isError(originalSize) || originalSize <= 0) {
            throw new IllegalStateException("Invalid or unknown ZSTD frame size.");
        }

        final byte[] decompressedBuffer = new byte[(int) originalSize];
        final long size = Zstd.decompress(decompressedBuffer, input);
        if (Zstd.isError(size)) {
            throw new IllegalStateException("ZSTD decompression failed: " + Zstd.getErrorName(size));
        }

        return Arrays.copyOf(decompressedBuffer, (int) size);
    }
}
