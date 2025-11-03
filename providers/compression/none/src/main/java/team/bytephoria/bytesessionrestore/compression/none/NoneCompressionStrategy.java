package team.bytephoria.bytesessionrestore.compression.none;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.spi.compression.CompressionStrategy;

public final class NoneCompressionStrategy implements CompressionStrategy {

    @Contract(pure = true)
    @Override
    public @NotNull String name() {
        return "NONE";
    }

    @Override
    public byte @NotNull [] compress(final byte @NotNull [] input) {
        return input;
    }

    @Override
    public byte @NotNull [] decompress(final byte @NotNull [] input) {
        return input;
    }
}
