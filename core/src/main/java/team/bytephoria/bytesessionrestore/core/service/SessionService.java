package team.bytephoria.bytesessionrestore.core.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.api.model.session.SessionMeta;
import team.bytephoria.bytesessionrestore.api.model.session.SessionRecord;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;
import team.bytephoria.bytesessionrestore.core.io.SnapshotIO;
import team.bytephoria.bytesessionrestore.core.model.LazySessionRecord;
import team.bytephoria.bytesessionrestore.core.registry.SnapshotSerializerRegistry;
import team.bytephoria.bytesessionrestore.core.concurrent.AsyncExecutor;
import team.bytephoria.bytesessionrestore.spi.compression.CompressionStrategy;
import team.bytephoria.bytesessionrestore.spi.serializer.snapshot.SnapshotSerializer;
import team.bytephoria.bytesessionrestore.spi.storage.UserStorage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class SessionService {

    private final UserStorage userStorage;
    private final CompressionStrategy defaultCompression;

    public SessionService(
            final @NotNull UserStorage userStorage,
            final @NotNull CompressionStrategy defaultCompression
    ) {
        this.userStorage = userStorage;
        this.defaultCompression = defaultCompression;
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot
    ) {
        return this.createAsync(userId, userName, eventType, snapshot, null, null);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot,
            final @Nullable CompressionStrategy compression
    ) {
        return this.createAsync(userId, userName, eventType, snapshot, null, compression);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot,
            final @Nullable SnapshotSerializer<T> serializer
    ) {
        return this.createAsync(userId, userName, eventType, snapshot, serializer, null);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot,
            final @Nullable SnapshotSerializer<T> serializer,
            final @Nullable CompressionStrategy compression
    ) {
        return this.createInternal(userId, userName, eventType, snapshot, serializer, compression);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createDirectAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot
    ) {
        return this.createDirectAsync(userId, userName, eventType, snapshot, null, null);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createDirectAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot,
            final @Nullable CompressionStrategy compression
    ) {
        return this.createDirectAsync(userId, userName, eventType, snapshot, null, compression);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createDirectAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot,
            final @Nullable SnapshotSerializer<T> serializer
    ) {
        return this.createDirectAsync(userId, userName, eventType, snapshot, serializer, null);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createDirectAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot,
            final @Nullable SnapshotSerializer<T> serializer,
            final @Nullable CompressionStrategy compression
    ) {
        return this.createInternal(userId, userName, eventType, snapshot, serializer, compression);
    }

    @SuppressWarnings("unchecked")
    private <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createInternal(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType eventType,
            final @NotNull T snapshot,
            final @Nullable SnapshotSerializer<T> optionalSerializer,
            final @Nullable CompressionStrategy optionalCompression
    ) {
        final SnapshotSerializer<T> serializer = optionalSerializer != null
                ? optionalSerializer
                : (SnapshotSerializer<T>) SnapshotSerializerRegistry.get(snapshot.typeId());

        final CompressionStrategy compression = optionalCompression != null
                ? optionalCompression
                : this.defaultCompression;

        return AsyncExecutor.supplyAsync(() -> {
            final SessionMeta meta = new SessionMeta(
                    eventType,
                    Timestamp.from(Instant.now()),
                    serializer.typeId(),
                    snapshot.version(),
                    compression.name()
            );

            final byte[] payload = SnapshotIO.INSTANCE.serialize(snapshot, serializer, compression);
            final SessionRecord sessionRecord = new SessionRecord(meta, payload);

            this.userStorage.storeSession(userId, userName, sessionRecord);

            return new LazySessionRecord(sessionRecord, () ->
                    SnapshotIO.INSTANCE.deserialize(sessionRecord.payload(), meta.snapshotType(), meta.compression())
            );
        });
    }
}
