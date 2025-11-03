package team.bytephoria.bytesessionrestore.core.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;
import team.bytephoria.bytesessionrestore.core.cache.UserCache;
import team.bytephoria.bytesessionrestore.core.model.LazySessionRecord;
import team.bytephoria.bytesessionrestore.core.model.UserAggregate;
import team.bytephoria.bytesessionrestore.spi.serializer.snapshot.SnapshotSerializer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class UserSessionService {

    private final SessionService sessionService;
    private final UserCache userCache;

    public UserSessionService(
            final @NotNull SessionService sessionService,
            final @NotNull UserCache userCache
    ) {
        this.sessionService = sessionService;
        this.userCache = userCache;
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createForUserAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType sessionEventType,
            final @NotNull T snapshot
    ) {
        return this.createForUserAsync(userId, userName, sessionEventType, snapshot, null);
    }

    public <T extends Snapshot> @NotNull CompletableFuture<LazySessionRecord> createForUserAsync(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionEventType sessionEventType,
            final @NotNull T snapshot,
            final @Nullable SnapshotSerializer<T> snapshotSerializer
    ) {
        return this.sessionService.createAsync(userId, userName, sessionEventType, snapshot, snapshotSerializer)
                .thenApply(lazySessionRecord -> {
                    final UserAggregate cachedUserAggregate = this.userCache.get(userId);
                    if (cachedUserAggregate != null) {
                        cachedUserAggregate.addSession(sessionEventType, lazySessionRecord);
                    }

                    return lazySessionRecord;
                });
    }
}
