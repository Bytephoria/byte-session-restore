package team.bytephoria.bytesessionrestore.core.service;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.core.concurrent.AsyncExecutor;

import java.util.Collection;
import java.util.UUID;

public final class SessionCleanupService {

    private final SessionLimiter limiter;
    private final int maxPerType;

    public SessionCleanupService(
            final @NotNull SessionLimiter sessionLimiter,
            final int maxPerType
    ) {
        this.limiter = sessionLimiter;
        this.maxPerType = maxPerType;
    }

    public void cleanupOnline(final @NotNull Collection<UUID> onlinePlayers) {
        onlinePlayers.forEach(id -> AsyncExecutor.runAsync(() -> this.limiter.enforce(id, this.maxPerType)));
    }
}
