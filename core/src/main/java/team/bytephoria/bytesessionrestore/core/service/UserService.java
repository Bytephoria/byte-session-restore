package team.bytephoria.bytesessionrestore.core.service;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.dto.SessionDto;
import team.bytephoria.bytesessionrestore.api.dto.UserDto;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.api.model.session.SessionMeta;
import team.bytephoria.bytesessionrestore.api.model.session.SessionRecord;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;
import team.bytephoria.bytesessionrestore.core.cache.UserCache;
import team.bytephoria.bytesessionrestore.core.io.SnapshotIO;
import team.bytephoria.bytesessionrestore.core.model.LazySessionRecord;
import team.bytephoria.bytesessionrestore.core.model.UserAggregate;
import team.bytephoria.bytesessionrestore.core.concurrent.AsyncExecutor;
import team.bytephoria.bytesessionrestore.spi.storage.UserStorage;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class UserService {

    private final UserCache userCache;
    private final UserStorage userStorage;

    public UserService(
            final @NotNull UserCache userCache,
            final @NotNull UserStorage userStorage
    ) {
        this.userCache = userCache;
        this.userStorage = userStorage;
    }

    public @NotNull CompletableFuture<UserAggregate> loadAsync(final @NotNull UUID userId) {
        final UserAggregate cached = this.userCache.get(userId);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }
        
        return AsyncExecutor.supplyAsync(() -> {
            final UserDto userDTO = this.userStorage.collectSessions(userId);
            if (userDTO == null) {
                return null;
            }

            final Map<SessionEventType, List<LazySessionRecord>> sessionMap = new EnumMap<>(SessionEventType.class);
            for (final SessionDto sessionDTO : userDTO.sessionDto()) {
                sessionMap
                        .computeIfAbsent(sessionDTO.eventType(), ignored -> new ArrayList<>())
                        .add(this.toLazySessionRecord(sessionDTO));
            }

            final UserAggregate aggregate = new UserAggregate(userId, sessionMap);
            this.userCache.put(userId, aggregate);
            return aggregate;
        });
    }

    private @NotNull LazySessionRecord toLazySessionRecord(final @NotNull SessionDto sessionDTO) {
        final SessionRecord record = new SessionRecord(
                new SessionMeta(
                        sessionDTO.eventType(),
                        sessionDTO.createdAt(),
                        sessionDTO.snapshotType(),
                        sessionDTO.version(),
                        sessionDTO.compression()
                ),
                sessionDTO.payload()
        );

        return new LazySessionRecord(record, () -> this.deserializeSafe(record));
    }

    private @NotNull Snapshot deserializeSafe(final @NotNull SessionRecord record) {
        final SessionMeta sessionMeta = record.meta();
        return SnapshotIO.INSTANCE.deserialize(record.payload(), sessionMeta.snapshotType(), sessionMeta.compression());
    }
}
