package team.bytephoria.bytesessionrestore.core.io;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;
import team.bytephoria.bytesessionrestore.core.registry.CompressionRegistry;
import team.bytephoria.bytesessionrestore.core.registry.SnapshotSerializerRegistry;
import team.bytephoria.bytesessionrestore.spi.compression.CompressionStrategy;
import team.bytephoria.bytesessionrestore.spi.serializer.snapshot.SnapshotSerializer;

public final class SnapshotIO {

    public static final SnapshotIO INSTANCE = new SnapshotIO();

    private SnapshotIO() {
    }

    public <T extends Snapshot> byte[] serialize(
            final @NotNull T snapshot,
            final @NotNull SnapshotSerializer<? super T> serializerProvider,
            final @NotNull CompressionStrategy compressionStrategy
    ) {
        try {
            final byte[] raw = serializerProvider.serialize(snapshot);
            return compressionStrategy.compress(raw);
        } catch (Exception e) {
            throw new RuntimeException("Serialize snapshot error", e);
        }
    }

    public @NotNull Snapshot deserialize(
            final byte @NotNull [] bytes,
            final @NotNull SnapshotSerializer<?> snapshotSerializer,
            final @NotNull CompressionStrategy compressionStrategy
    ) {
        final byte[] decompressed = compressionStrategy.decompress(bytes);
        return snapshotSerializer.deserialize(decompressed);
    }

    public @NotNull Snapshot deserialize(
            final byte @NotNull [] bytes,
            final @NotNull String snapshotType,
            final @NotNull String compressionName
    ) {
        final CompressionStrategy compressionStrategy = CompressionRegistry.getOrDefault(compressionName);
        final SnapshotSerializer<?> snapshotSerializer = SnapshotSerializerRegistry
                .find(snapshotType)
                .orElseThrow(() -> new IllegalStateException("Unknown snapshot type: " + snapshotType));

        return this.deserialize(bytes, snapshotSerializer, compressionStrategy);
    }
}
