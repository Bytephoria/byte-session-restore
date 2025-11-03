package team.bytephoria.bytesessionrestore.spi.serializer.snapshot;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;

/**
 * Handles binary serialization and deserialization of {@link Snapshot} objects.
 * <p>
 * Each serializer must define a stable {@link #typeId()} and be registered
 * in the {@code SnapshotSerializerRegistry}.
 */
public interface SnapshotSerializer<T extends Snapshot> {

    /** @return unique identifier for this serializer (e.g. "COMMON_PLAYER_SNAPSHOT") */
    @NotNull String typeId();

    /**
     * Converts a snapshot into a binary payload.
     *
     * @param snapshot snapshot instance
     * @return serialized byte array
     */
    byte @NotNull [] serialize(final @NotNull T snapshot);

    /**
     * Restores a snapshot from its serialized form.
     *
     * @param bytes serialized data
     * @return deserialized snapshot
     */
    @NotNull T deserialize(final byte @NotNull [] bytes);
}
