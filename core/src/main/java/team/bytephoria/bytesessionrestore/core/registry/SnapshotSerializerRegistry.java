package team.bytephoria.bytesessionrestore.core.registry;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.spi.serializer.snapshot.SnapshotSerializer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SnapshotSerializerRegistry {

    private static final Map<String, SnapshotSerializer<?>> SERIALIZERS = new HashMap<>();

    public static void register(final @NotNull String key, final @NotNull SnapshotSerializer<?> snapshotSerializer) {
        SERIALIZERS.put(key, snapshotSerializer);
    }

    public static void register(final @NotNull SnapshotSerializer<?> serializer) {
        SERIALIZERS.put(serializer.typeId(), serializer);
    }

    public static @NotNull SnapshotSerializer<?> get(final @NotNull String typeId) {
        return SERIALIZERS.get(typeId);
    }

    public static @NotNull Optional<SnapshotSerializer<?>> find(final @NotNull String typeId) {
        return Optional.ofNullable(SERIALIZERS.get(typeId));
    }

    @Contract(pure = true)
    public static @NotNull Collection<String> keys() {
        return SERIALIZERS.keySet();
    }

}
