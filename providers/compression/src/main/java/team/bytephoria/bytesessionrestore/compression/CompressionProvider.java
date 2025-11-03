package team.bytephoria.bytesessionrestore.compression;

import team.bytephoria.bytesessionrestore.compression.none.NoneCompressionStrategy;
import team.bytephoria.bytesessionrestore.compression.zstd.ZstdCompressionStrategy;
import team.bytephoria.bytesessionrestore.core.registry.CompressionRegistry;

public final class CompressionProvider {

    public static void registerAll() {
        CompressionRegistry.register(new NoneCompressionStrategy());
        CompressionRegistry.register(new ZstdCompressionStrategy());
    }

}
